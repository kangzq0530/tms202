package com.msemu.world.client.field;

import com.msemu.commons.data.templates.field.FieldTemplate;
import com.msemu.commons.data.templates.field.Foothold;
import com.msemu.commons.data.templates.field.Portal;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.enums.FieldLimit;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.thread.EventManager;
import com.msemu.commons.utils.Rand;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.drops.LP_DropEnterField;
import com.msemu.core.network.packets.outpacket.drops.LP_DropLeaveField;
import com.msemu.core.network.packets.outpacket.field.LP_AffectedAreaCreated;
import com.msemu.core.network.packets.outpacket.field.LP_FootHoldMove;
import com.msemu.core.network.packets.outpacket.field.LP_SetQuickMoveInfo;
import com.msemu.core.network.packets.outpacket.stage.LP_SetField;
import com.msemu.core.network.packets.outpacket.user.LP_UserEnterField;
import com.msemu.core.network.packets.outpacket.user.LP_UserLeaveField;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.stats.TemporaryStatManager;
import com.msemu.world.client.field.forceatoms.ForceAtomHandler;
import com.msemu.world.client.field.forceatoms.types.ForceAtomInfo;
import com.msemu.world.client.field.lifes.*;
import com.msemu.world.client.field.spawns.AbstractSpawnPoint;
import com.msemu.world.client.field.spawns.NpcSpawnPoint;
import com.msemu.world.constants.GameConstants;
import com.msemu.world.data.FieldData;
import com.msemu.world.data.ItemData;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.msemu.commons.data.enums.SkillStat.time;

/**
 * Created by Weber on 2018/4/11.
 */
public class Field {
    @Getter
    protected final int fieldId;
    @Getter
    protected final Map<FieldObjectType, LinkedHashMap<Integer, AbstractFieldObject>> fieldObjects;
    @Getter
    protected final Map<FieldObjectType, ReentrantReadWriteLock> fieldObjectLocks;
    @Getter
    protected final List<AbstractSpawnPoint> spawnPoints = new ArrayList<>();
    @Getter
    protected final List<FieldObj> objects;
    @Getter
    protected final Map<AbstractFieldObject, ScheduledFuture> objectSchedules;
    @Getter
    protected final FootholdTree footholdTree;
    @Getter
    protected final AtomicInteger spawnedMobOnField = new AtomicInteger(0);
    @Setter
    protected AtomicInteger objectIDCounter = new AtomicInteger(1000000);
    @Getter
    @Setter
    protected FieldCustom fieldCustom;
    @Getter
    @Setter
    protected boolean isUserFirstEnter;
    @Getter
    @Setter
    protected LocalDateTime lastSpawnEliteMobTime = LocalDateTime.MIN, lastSpawnTime = LocalDateTime.MIN;
    @Getter
    protected ForceAtomHandler forceAtomHandler = new ForceAtomHandler();

    public Field(FieldTemplate fieldData) {
        this.fieldId = fieldData.getId();
        this.objects = new ArrayList<>();
        this.objectSchedules = new HashMap<>();
        Position lBound = new Position(fieldData.getRect().getLeft(), fieldData.getRect().getTop());
        Position rBound = new Position(fieldData.getRect().getRight(), fieldData.getRect().getBottom());
        this.footholdTree = new FootholdTree(lBound, rBound);
        fieldData.getFootholds().forEach(this.footholdTree::insertFoothold);
        EnumMap<FieldObjectType, LinkedHashMap<Integer, AbstractFieldObject>> objectsMap = new EnumMap<>(FieldObjectType.class);
        EnumMap<FieldObjectType, ReentrantReadWriteLock> objectLockMap = new EnumMap<>(FieldObjectType.class);
        Arrays.stream(FieldObjectType.values()).forEach(type -> {
            objectsMap.put(type, new LinkedHashMap<>());
            objectLockMap.put(type, new ReentrantReadWriteLock());
        });
        this.fieldObjects = Collections.unmodifiableMap(objectsMap);
        this.fieldObjectLocks = Collections.unmodifiableMap(objectLockMap);
    }

    /**
     *  Return the fieldData of this field.
     * @return the field data
     */
    public FieldTemplate getFieldData() {
        return FieldData.getInstance().getFieldTemplate(getFieldId());
    }

    /**
     *  Return the width of this field
     * @return the width of this field
     */
    public int getWidth() {
        return getFieldData().getRect().getWidth();
    }

    /**
     * Return the height of this field
     * @return the height of this field
     */
    public int getHeight() {
        return getFieldData().getRect().getHeight();
    }

    /**
     *  Return the portals in this field
     * @return portals
     * @see Portal
     */
    public Set<Portal> getPortals() {
        return getFieldData().getPortals();
    }

    /**
     *  Return the mobRate of this field
     * @return mobRate
     */
    public int getMobRate() {
        return getFieldData().getMobRate();
    }

    /**
     *  Return the onUsrEnter script name of this field
     * @return script name of onUserEnter of this field
     */
    public String getOnUserEnter() {
        return getFieldData().getOnUserEnter();
    }

    /**
     *  Return the OnFirstUserEnter script name of this field
     * @return cript name of OnFirstUserEnter of this field
     */
    public String getOnFirstUserEnter() {
        return getFieldData().getOnFirstUserEnter();
    }

    /**
     *  Returns weather if the onUserFirstEnter is exists
     * @return weather if the onUserFirstEnter is exists
     */
    protected boolean hasUserFirstEnterScript() {
        return getOnFirstUserEnter() != null && !getOnFirstUserEnter().equalsIgnoreCase("");
    }

    /**
     *  Return the party bonus rate of this field
     * @return the party bonus rate
     */
    public int getPartyBonusRate() {
        return getFieldData().getPartyBonusR();
    }

    /**
     *  Return the fixed mob capacity of this field
     * @return
     */
    public int getFixedMob() {
        return getFieldData().getFixedMobCapacity();
    }

    /**
     * Returns a Portal object in this field. The name argument is a specifier that is related to portal name
     *
     * @param name the portal name
     * @return the portal with the specified name
     * @see Portal
     */
    public Portal getPortalByName(String name) {
        return getPortals().stream().filter(portal -> portal.getName().equals(name)).findAny().orElse(null);
    }


    /**
     * Returns a Portal object in this field. The fieldId argument is a specifier that is related to portal fieldId
     *
     * @param id
     * @return the portal with the specified fieldId
     * @see Portal
     */
    public Portal getPortalByID(int id) {
        return getPortals().stream().filter(portal -> portal.getId() == id).findAny().orElse(null);
    }

    /**
     * Returns a Foothold object that is below a specified position
     *
     * @param pos position to find below foothold
     * @return the foothold object
     * @see Position
     * @see Foothold
     */
    public Foothold findFootHoldBelow(Position pos) {
        return getFootholdTree().findBelow(pos, false);
    }

    /**
     *  Returns a Foothold object with a specified Id
     * @param fh foothold Id
     * @return the specified foothold id
     */
    public Foothold getFootholdById(int fh) {
        return getFootholdTree().getFootholdById(fh);
    }

    public void enter(Character chr, Portal portal, boolean characterData) {
        chr.setPosition(portal.getPosition());
        chr.setField(this);
        chr.getClient().write(new LP_SetField(chr, this, chr.getClient().getChannel(), false, portal.getId(), characterData, chr.hasBuffProtector(),
                portal.getId(), false, 100, null, true, -1));
        this.addCharacter(chr);
    }


    public void leave(Character chr) {
        removeCharacter(chr);
        chr.setField(null);
    }

    private void addCharacter(Character chr) {
        updateCharacterPosition(chr);
        if (!getAllCharacters().contains(chr)) {
            addFieldObject(chr);
            if (!isUserFirstEnter() && hasUserFirstEnterScript()) {
                chr.getScriptManager().startScript(getFieldId(), getOnFirstUserEnter(), ScriptType.FIELD);
            }
            if (chr.getParty() != null) {
                chr.getParty().updateFull();
            }
            List<QuickMoveNpcInfo> quickMoveNpcInfos = QuickMoveInfo.getVisibleQuickMoveNpcs(getFieldId());
            chr.write(new LP_SetQuickMoveInfo(quickMoveNpcInfos));
            // TODO LP_IncJudgementStack

            // TODO mapEffect (song , broadcast)

            FieldTemplate ft = getFieldData();
            EventManager em = EventManager.getInstance();
            if (ft.getTimeLimit() > 0 && ft.getReturnMap() > 0) {
                //TODO 計時回地圖
            } else if (ft.getTimeLimit() > 0 && ft.getForcedReturn() > 0) {
                //TODO 計時回地圖
            }

            // LP_BlowWeather

            List<FieldObj> movingPlatforms = getObjects().stream()
                    .filter(FieldObj::isMove).collect(Collectors.toList());
            if (!movingPlatforms.isEmpty()) {
                chr.write(new LP_FootHoldMove(movingPlatforms));
            }


            //TODO  處理事件腳本

            chr.getForcedStatManager().onEnterField(getFieldId());

            //叫出花狐 龍

            // 強化任意門剩餘次數
        }
        getAllMobs().forEach(this::updateMobController);
        broadcastPacket(new LP_UserEnterField(chr), chr);
    }





    @Override
    public String toString() {
        return String.format("[地圖] %s(%d)s", getFieldData().getName(), getFieldData().getId());
    }

    public void broadcastPacket(OutPacket<GameClient> outPacket) {
        for (Character c : getAllCharacters()) {
            c.getClient().write(outPacket);
        }
    }

    public void broadcastPacket(OutPacket<GameClient> outPacket, Character exceptChr) {
        getAllCharacters().stream().filter(chr -> !chr.equals(exceptChr)).forEach(
                chr -> chr.write(outPacket)
        );
    }

    public void spawnLife(Life life) {
        if ( life.getField() != null)
            return;
        life.setField(this);
        this.addFieldObject(life);
        switch (life.getFieldObjectType()) {
            case MOB:
                break;
            case SUMMON:
                break;
            case NPC:
                break;
            case CHARACTER:
                break;
        }
    }

    public void spawnAffectedArea(AffectedArea affectedArea) {
        this.addFieldObject(affectedArea);
        SkillInfo si = SkillData.getInstance().getSkillInfoById(affectedArea.getSkillID());
        if (si != null) {
            int duration = si.getValue(time, affectedArea.getSlv()) * 1000;
            ScheduledFuture sf = EventManager.getInstance().addEvent(() -> {
                removeObjectSchedule(affectedArea, true);
                removeFieldObject(affectedArea);
            }, duration);
            addObjectSchedule(affectedArea, sf);
        }
        broadcastPacket(new LP_AffectedAreaCreated(affectedArea));
        getAllCharacters().forEach(chr -> affectedArea.getField().checkCharInAffectedAreas(chr));
        getAllMobs().forEach(mob -> affectedArea.getField().checkMobInAffectedAreas(mob));
    }


    public int getNewObjectID() {
        return objectIDCounter.getAndIncrement();
    }


    public void removeDropFromUser(Drop drop) {
        AbstractFieldObject object = getFieldObjectsByKey(FieldObjectType.DROP, drop.getObjectId());
        broadcastPacket(new LP_DropLeaveField(DropLeaveType.PickedUpByUser, drop.getObjectId(), drop.getPickupId(), (short) 0, 0, 0));
        removeObjectSchedule(object, true);
        removeFieldObject(object);
    }

    public void addObjectSchedule(AbstractFieldObject object, ScheduledFuture scheduledFuture) {
        getObjectSchedules().put(object, scheduledFuture);
    }

    public void removeObjectSchedule(AbstractFieldObject object, boolean cancel) {
        if (!getObjectSchedules().containsKey(object)) {
            return;
        }
        if (!cancel) {
            getObjectSchedules().get(object).cancel(false);
        }
        getObjectSchedules().remove(object);
    }


    public void checkMobInAffectedAreas(Mob mob) {
        getAllAffectedAreas().stream().filter(aa -> aa.getRect().hasPositionInside(mob.getPosition())).forEach(aa -> {
            aa.handleMobInside(mob);
        });
    }

    public void checkCharInAffectedAreas(Character chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        for (AffectedArea aa : getAllAffectedAreas()) {
            boolean isInsideAA = aa.getRect().hasPositionInside(chr.getPosition());
            if (isInsideAA) {
                aa.handleCharInside(chr);
            } else if (tsm.hasAffectedArea(aa)) {
                tsm.removeAffectedArea(aa);
            }
        }
    }

    protected void broadcastWithPredicate(OutPacket<GameClient> outPacket, Predicate<? super Character> predicate) {
        getAllCharacters().stream().filter(predicate).forEach(chr -> chr.write(outPacket));
    }

    /**
     * Drops a {@link Drop} at a given Position. Calculates the Position that the Drop should land at.
     *
     * @param drop     The Drop that should be dropped.
     * @param position The Position it is dropped from.
     */
    public void drop(Drop drop, Position position) {
        int x = position.getX();
        Position posTo = new Position(x, findFootHoldBelow(position).getYFromX(x));
        drop(drop, position, posTo, 0);
    }

    public void drop(Drop drop, Position posFrom, Position posTo, int ownerId) {
        ScheduledFuture sf = EventManager.getInstance().addEvent(() -> {
            addFieldObject(drop);
            broadcastPacket(new LP_DropEnterField(drop, posFrom, posTo, ownerId));
        }, Rand.get(0, 200));
        addObjectSchedule(drop, sf);
    }


    public void drop(DropInfo dropInfo, Position posFrom, Position posTo, int ownerID) {
        int itemID = dropInfo.getItemID();
        Item item;
        Drop drop = new Drop(-1);
        drop.setPosition(posTo);
        drop.setOwnerID(ownerID);
        if (itemID != 0) {
            item = ItemData.getInstance().createItem(itemID);
            if( item.getType() == Item.Type.ITEM) {
                item.setQuantity(Rand.get(dropInfo.getMinQuantity(), dropInfo.getMaxQuantity()));
                int slotMax = item.getTemplate().getSlotMax();
                if (slotMax > 0)
                    item.setQuantity(Math.min(slotMax, item.getQuantity()));
            }
            drop.setItem(item);
        } else {
            drop.setMoney(Rand.get(dropInfo.getMinQuantity(), dropInfo.getMaxQuantity()));
        }
        drop(drop, posFrom, posTo, ownerID);
    }

    /**
     * Drops a Set of {@link DropInfo}s from a base Position.
     *
     * @param dropInfoList The Set of DropInfos.
     * @param position  The Position the initial Drop comes from.
     * @param ownerID   The owner's character ID.
     */
    public void drop(List<DropInfo> dropInfoList, Position position, int ownerID) {
        drop(dropInfoList, findFootHoldBelow(position), position, ownerID);
    }

    /**
     * Drops a Set of {@link DropInfo}s, locked to a specific {@link Foothold}.
     * Not all drops are guaranteed to be dropped, as this method calculates whether or not a Drop should drop, according
     * to the DropInfo's prop chance.
     *
     * @param dropInfoList The Set of DropInfos that should be dropped.
     * @param fh        The Foothold this Set of DropInfos is bound to.
     * @param position  The Position the Drops originate from.
     * @param ownerID   The ID of the owner of all drops.
     */
    public void drop(List<DropInfo> dropInfoList, Foothold fh, Position position, int ownerID) {
        int x = position.getX();
        int minX = fh.getX1();
        int maxX = fh.getX2();
        int diff = 0;
        for (DropInfo dropInfo : dropInfoList) {
            if (dropInfo.willDrop()) {
                x = (x + diff) > maxX ? maxX - 10 : (x + diff) < minX ? minX + 10 : x + diff;
                Position posTo = new Position(x, fh.getYFromX(x));
                drop(dropInfo, position, posTo, ownerID);
                diff = diff < 0 ? Math.abs(diff - GameConstants.DROP_DIFF) : -(diff + GameConstants.DROP_DIFF);
            }
        }
    }

    public List<Portal> getPortalsInRange(Rect rect) {
        List<Portal> portals = new ArrayList<>();
        for (Portal portals2 : getPortals()) {
            int x = portals2.getPosition().getX();
            int y = portals2.getPosition().getY();
            if (x >= rect.getLeft() && y >= rect.getTop()
                    && x <= rect.getRight() && y <= rect.getBottom()) {
                portals.add(portals2);
            }
        }
        return portals;
    }

    public Character getCharacterByName(String name) {
        return getAllCharacters().stream().filter(chr -> chr.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void execUserEnterScript(Character chr) {
        if (getOnUserEnter() == null || getOnUserEnter().equalsIgnoreCase("")) {
            return;
        }
        String script = getOnUserEnter();
        chr.getScriptManager().startScript(getFieldId(), script, ScriptType.FIELD);
    }

    public int getReturnMap() {
        return getFieldData().getReturnMap();
    }

    public void startTimeLimitTask(int time, Field toField) {


    }

    public void reset() {
        reset(true);
    }

    public void reset(boolean respawn) {

    }

    public void respawn(boolean force) {
        respawn(LocalDateTime.now(), force);
    }

    public void respawn(LocalDateTime now, boolean force) {
        getSpawnPoints().stream()
                .filter(spawnPoint -> spawnPoint instanceof NpcSpawnPoint)
                .forEach(spawnPoint -> {
                    if (spawnPoint.shouldSpawn(now)) {
                        spawnPoint.spawn(this);
                    }
                });
        int mobSpawnCount = (int) getSpawnPoints()
                .stream()
                .filter(spawnPoint -> !(spawnPoint instanceof NpcSpawnPoint)).count();
        int maxSpawned = (mobSpawnCount >= 20 || getPartyBonusRate() > 0) ? Math.round(mobSpawnCount / getMobRate())
                : (int) Math.ceil(mobSpawnCount * getMobRate());

        if (getFixedMob() > 0)
            maxSpawned = getFixedMob();

        maxSpawned = Math.max(2, maxSpawned);
        maxSpawned = Math.min(10, maxSpawned);

        maxSpawned -= spawnedMobOnField.get();

        List<AbstractSpawnPoint> randomMobSpawn = getSpawnPoints()
                .stream()
                .filter(spawnPoint -> !(spawnPoint instanceof NpcSpawnPoint) && spawnPoint.shouldSpawn(now))
                .collect(Collectors.toList());
        Collections.shuffle(randomMobSpawn);

        int hasMobSpawned = 0;

        for (AbstractSpawnPoint spawnPoint : randomMobSpawn) {
            spawnPoint.spawn(this);
            hasMobSpawned++;
            if (hasMobSpawned >= maxSpawned)
                break;
        }

    }

    /**
     *  Returns a character that it has minimize number of controlled mob
     * @param mob Mob
     * @return new mob Controller
     */
    public Character findNextMobController(final Mob mob) {
        AtomicReference<Integer> minControlledMobCount = new AtomicReference<>(-1);
        AtomicReference<Character> newController = new AtomicReference<>(null);
        getFieldObjectWriteLock(FieldObjectType.CHARACTER).lock();
        try {
            getFieldObjects().get(FieldObjectType.CHARACTER).values().stream()
                    .map(object -> (Character) object)
                    .forEach(chr -> {
                        if ((chr.getControlledMobs().size() < minControlledMobCount.get() || minControlledMobCount.get() == -1)
                                && chr.getPosition().distanceSq(mob.getPosition()) <= GameConstants.maxViewRangeSq()) {
                            minControlledMobCount.set(chr.getControlledMobs().size());
                            newController.set(chr);
                        }
                    });
        } finally {
            getFieldObjectWriteLock(FieldObjectType.CHARACTER).unlock();
        }
        return newController.get();
    }

    public void updateMobController(Mob mob) {
        if (!mob.isAlive() || mob.isEscort()) {
            return;
        }
        if (mob.getController() != null) {
            mob.getController().stopControllingMob(mob);
        }
        final Character newController = findNextMobController(mob);
        if (newController != null) {
            if (mob.isFirstAttack()) {
                newController.controlMob(mob, MobControlLevel.FOLLOW.getValue());
            } else {
                newController.controlMob(mob, MobControlLevel.NORMAL.getValue());
            }
        }
    }

    public Lock getFieldObjectReadLock(FieldObjectType type) {
        return getFieldObjectLocks().get(type).readLock();
    }

    public Lock getFieldObjectWriteLock(FieldObjectType type) {
        return getFieldObjectLocks().get(type).writeLock();
    }

    public AbstractFieldObject getFieldObjectsByKey(FieldObjectType type, Integer key) {
        getFieldObjectReadLock(type).lock();
        try {
            return getFieldObjects().get(type).getOrDefault(key, null);
        } finally {
            getFieldObjectReadLock(type).unlock();
        }
    }

    public Map<Integer, AbstractFieldObject> getFieldObjectsMapByType(FieldObjectType type) {
        getFieldObjectReadLock(type).lock();
        try {
            return getFieldObjects().get(type);
        } finally {
            getFieldObjectReadLock(type).unlock();
        }
    }

    public List<Npc> getAllNpc() {
        getFieldObjectReadLock(FieldObjectType.NPC).lock();
        try {
            return getFieldObjects().get(FieldObjectType.NPC).values().stream().map(o -> (Npc) o).collect(Collectors.toList());
        } finally {
            getFieldObjectReadLock(FieldObjectType.NPC).unlock();
        }
    }

    public List<Mob> getAllMobs() {
        getFieldObjectReadLock(FieldObjectType.MOB).lock();
        try {
            return getFieldObjects().get(FieldObjectType.MOB).values().stream().map(o -> (Mob) o).collect(Collectors.toList());
        } finally {
            getFieldObjectReadLock(FieldObjectType.MOB).unlock();
        }
    }


    public List<Character> getAllCharacters() {
        getFieldObjectReadLock(FieldObjectType.CHARACTER).lock();
        try {
            return getFieldObjects().get(FieldObjectType.CHARACTER).values().stream().map(o -> (Character) o).collect(Collectors.toList());
        } finally {
            getFieldObjectReadLock(FieldObjectType.CHARACTER).unlock();
        }
    }

    public List<Drop> getAllDrops() {
        getFieldObjectReadLock(FieldObjectType.DROP).lock();
        try {
            return getFieldObjects().get(FieldObjectType.DROP).values().stream().map(o -> (Drop) o).collect(Collectors.toList());
        } finally {
            getFieldObjectReadLock(FieldObjectType.DROP).unlock();
        }
    }

    public List<AffectedArea> getAllAffectedAreas() {
        getFieldObjectReadLock(FieldObjectType.AFFECTED_AREA).lock();
        try {
            return getFieldObjects().get(FieldObjectType.AFFECTED_AREA).values().stream().map(o -> (AffectedArea) o).collect(Collectors.toList());
        } finally {
            getFieldObjectReadLock(FieldObjectType.AFFECTED_AREA).unlock();
        }
    }


    public Character getCharacterById(int id) {
        return getAllCharacters().stream()
                .filter(chr -> chr.getId() == id)
                .findFirst().orElse(null);
    }

    public Npc getNpcByObjectID(int objectId) {
        return (Npc) getFieldObjectsByKey(FieldObjectType.NPC, objectId);
    }

    public Npc getNpcByTemplateId(int templateId) {
        return getAllNpc().stream()
                .filter(npc -> npc.getTemplateId() == templateId)
                .findFirst().orElse(null);
    }

    public Mob getMobByObjectId(int objectId) {
        return (Mob) getFieldObjectsByKey(FieldObjectType.MOB, objectId);
    }

    public Drop getDropByObjectId(int objectId) {
        return (Drop) getFieldObjectsByKey(FieldObjectType.DROP, objectId);
    }


    public List<Mob> getMobInRect(Rect rect) {
        return getAllMobs().stream().filter(mob -> rect.contains(mob.getPosition())).collect(Collectors.toList());
    }

    private void removeCharacter(Character chr) {
        removeFieldObject(chr);
        broadcastPacket(new LP_UserLeaveField(chr), chr);
        chr.getControlledMobs().forEach(mob -> {
            if (mob.getController().equals(chr)) {
                mob.setController(null);
                mob.setControllerLevel(1);
                updateMobController(mob);
            }
        });
        //remove follow
        //remove Extractor
        //remove summon
    }

    public void addFieldObject(AbstractFieldObject object) {
        object.setObjectId(getNewObjectID());
        getFieldObjectWriteLock(object.getFieldObjectType()).lock();
        try {
            getFieldObjects().get(object.getFieldObjectType()).put(object.getObjectId(), object);
        } finally {
            getFieldObjectWriteLock(object.getFieldObjectType()).unlock();
        }
    }

    public void removeFieldObject(AbstractFieldObject object) {
        getFieldObjectWriteLock(object.getFieldObjectType()).lock();
        try {
            getFieldObjects().get(object.getFieldObjectType()).remove(object.getObjectId());
        } finally {
            getFieldObjectWriteLock(object.getFieldObjectType()).unlock();
        }
    }


    public void updateFieldObjectVisibility(Character character, AbstractFieldObject object) {
        if (!character.isVisibleFieldObject(object)) {
            if (character.getPosition().distanceSq(object.getPosition()) <= GameConstants.maxViewRangeSq()) {
                character.addVisibleFieldObject(object);
                object.enterScreen(character.getClient());
            }
        } else {
            if (character.getPosition().distanceSq(object.getPosition()) > GameConstants.maxViewRangeSq()) {
                character.removeVisibleFieldObject(object);
                object.outScreen(character.getClient());
            }
        }
    }

    public List<AbstractFieldObject> getFieldObjectsInRange(Position position, double rangeSq) {
        List<AbstractFieldObject> ret = new ArrayList<>();
        Arrays.stream(FieldObjectType.values()).forEach(type -> {
            getFieldObjectReadLock(type).lock();
            try {
                getFieldObjects().get(type).values().forEach(eachObject -> {
                    if (position.distanceSq(eachObject.getPosition()) <= rangeSq) {
                        ret.add(eachObject);
                    }
                });
            } finally {
                getFieldObjectReadLock(type).unlock();
            }
        });
        return ret;
    }

    public void updateCharacterPosition(Character chr) {
        getFieldObjectsInRange(chr.getPosition(), GameConstants.maxViewRangeSq()).forEach(object -> {
            updateFieldObjectVisibility(chr, object);
        });
    }

    public void addSpawnPoint(AbstractSpawnPoint spawnPoint) {
        getSpawnPoints().add(spawnPoint);
    }

    public void update(LocalDateTime now) {
        getAllDrops().forEach(drop -> {
            if (drop.isExpired()) {

            }
        });
        int charSize = getAllCharacters().size();

        if (charSize > 0) {
            if (getLastSpawnTime().plus(10 * 1000, ChronoUnit.MILLIS).isBefore(now)) {
                respawn(now, false);
                setLastSpawnTime(now.plus(10 * 1000, ChronoUnit.MILLIS));
            }
        }

        getAllMobs().forEach(mob -> {
            if (!mob.isAlive()) {
                mob.die();
            }
        });
    }

    public void removeMob(Mob mob) {
        removeFieldObject(mob);
        getAllCharacters().forEach(chr -> {
            chr.removeVisibleFieldObject(mob);
        });
    }


    public void removeSummon(Summon evilEye) {
        removeFieldObject(evilEye);
        evilEye.setField(null);
    }


    public void removeMistByObjectId(int objectId) {
        AbstractFieldObject mist = getFieldObjectsByKey(FieldObjectType.MIST, objectId);
        removeFieldObject(mist);
    }

    public Portal findClosestPortal(Position position) {
        Portal closest = getPortalByID(0);
        double distance, shortestDistance = Double.POSITIVE_INFINITY;
        for (Portal portal : getPortals()) {
            distance = portal.getPosition().distanceSq(position);
            if (distance < shortestDistance) {
                closest = portal;
                shortestDistance = distance;
            }
        }
        return closest;
    }

    public int getFieldLimit() {
        return getFieldData().getFieldLimit();
    }

    public boolean checkFieldLimit(FieldLimit limits) {
        return limits.isLimit(getFieldLimit());
    }

    public boolean checkFieldLimits(FieldLimit... limits) {
        return Arrays.stream(limits).allMatch(
                limit -> limit.isLimit(getFieldLimit())
        );
    }

    public int getConsumeItemCoolTime() {
        return getFieldData().getConsumeItemCoolTime();
    }

    public void addForceAtom(ForceAtomInfo atomInfo) {
        getForceAtomHandler().addForceAtom(atomInfo);
    }

    public void removeForceAtomByCount(int count) {
        getForceAtomHandler().removeAtom(count);
    }

    public ForceAtomInfo getForceAtomByCount(int count) {
        return getForceAtomHandler().getForceAtom(count);
    }

    public boolean hasForceAtom(int count) {
        return getForceAtomHandler().getForceAtom(count) != null;
    }
}

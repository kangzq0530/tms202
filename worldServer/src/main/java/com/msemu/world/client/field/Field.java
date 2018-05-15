package com.msemu.world.client.field;

import com.msemu.commons.data.templates.field.FieldTemplate;
import com.msemu.commons.data.templates.field.Foothold;
import com.msemu.commons.data.templates.field.Portal;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.thread.EventManager;
import com.msemu.commons.utils.Rand;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.out.drops.LP_DropEnterField;
import com.msemu.core.network.packets.out.drops.LP_DropLeaveField;
import com.msemu.core.network.packets.out.field.LP_AffectedAreaCreated;
import com.msemu.core.network.packets.out.field.LP_FootHoldMove;
import com.msemu.core.network.packets.out.field.LP_SetQuickMoveInfo;
import com.msemu.core.network.packets.out.user.LP_UserEnterField;
import com.msemu.core.network.packets.out.user.LP_UserLeaveField;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.skill.TemporaryStatManager;
import com.msemu.world.client.field.lifes.*;
import com.msemu.world.client.field.spawns.AbstractSpawnPoint;
import com.msemu.world.client.field.spawns.NpcSpawnPoint;
import com.msemu.world.constants.FieldConstants;
import com.msemu.world.constants.GameConstants;
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
    @Setter
    private long uniqueId;

    @Getter
    private final Map<FieldObjectType, LinkedHashMap<Integer, AbstractFieldObject>> fieldObjects;
    @Getter
    private final Map<FieldObjectType, ReentrantReadWriteLock> fieldObjectLocks;
    @Getter
    private final List<AbstractSpawnPoint> spawnPoints = new ArrayList<>();
    @Getter
    private final List<FieldObject> objects;
    @Getter
    private final Map<AbstractFieldObject, ScheduledFuture> objectSchedules;
    @Setter
    private AtomicInteger objectIDCounter = new AtomicInteger(1000000);
    @Getter
    private final FieldTemplate fieldData;
    @Getter
    @Setter
    private FieldCustom fieldCustom;
    @Getter
    @Setter
    private boolean isUserFirstEnter;
    @Getter
    private final FootholdTree footholdTree;
    @Getter
    @Setter
    private LocalDateTime lastSpawnEliteMobTime = LocalDateTime.MIN, lastSpawnTime = LocalDateTime.MIN;
    @Getter
    private final AtomicInteger spawnedMobOnField = new AtomicInteger(0);

    public Field(long uniqueId, FieldTemplate template) {
        this.uniqueId = uniqueId;
        this.fieldData = template;
        this.objects = new ArrayList<>();
        this.objectSchedules = new HashMap<>();
        Position lBound = new Position(fieldData.getRect().getLeft(), fieldData.getRect().getTop());
        Position rBound = new Position(fieldData.getRect().getRight(), fieldData.getRect().getBottom());
        this.footholdTree = new FootholdTree(lBound, rBound);
        this.fieldData.getFootholds().forEach(this.footholdTree::insertFoothold);
        EnumMap<FieldObjectType, LinkedHashMap<Integer, AbstractFieldObject>> objectsMap = new EnumMap<>(FieldObjectType.class);
        EnumMap<FieldObjectType, ReentrantReadWriteLock> objectLockMap = new EnumMap<>(FieldObjectType.class);
        Arrays.stream(FieldObjectType.values()).forEach(type -> {
            objectsMap.put(type, new LinkedHashMap<>());
            objectLockMap.put(type, new ReentrantReadWriteLock());
        });
        this.fieldObjects = Collections.unmodifiableMap(objectsMap);
        this.fieldObjectLocks = Collections.unmodifiableMap(objectLockMap);
    }

    public int getWidth() {
        return getFieldData().getRect().getWidth();
    }

    public int getHeight() {
        return getFieldData().getRect().getHeight();
    }

    public int getId() {
        return getFieldData().getId();
    }

    public Set<Portal> getPortals() {
        return getFieldData().getPortals();
    }

    public int getMobRate() {
        return getFieldData().getMobRate();
    }

    public String getOnUserEnter() {
        return getFieldData().getOnUserEnter();
    }

    public String getOnFirstUserEnter() {
        return getFieldData().getOnFirstUserEnter();
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
     * Returns a Portal object in this field. The id argument is a specifier that is related to portal id
     *
     * @param id
     * @return the portal with the specified id
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


    public Foothold getFootholdById(int fh) {
        return getFootholdTree().getFootholdById(fh);
    }

    public void addCharacter(Character chr) {
        chr.setField(this);
        updateCharacterPosition(chr);
        if (!getCharacters().contains(chr)) {
            addFieldObject(chr);
            if (!isUserFirstEnter() && hasUserFirstEnterScript()) {
                chr.getScriptManager().startScript(getId(), getOnFirstUserEnter(), ScriptType.FIELD);
            }
            if (chr.getParty() != null) {
                chr.getParty().updateFull();
            }
            QuickMoveInfo quickMoveInfo = QuickMoveInfo.getByMap(getId());
            if (quickMoveInfo != null) {
                List<QuickMoveNpcInfo> quickMoveNpcInfos = new LinkedList<>();
                if (QuickMoveInfo.GLOBAL_NPC != 0 && !FieldConstants.isBossMap(getId()) && !FieldConstants.isTutorialMap(getId()) && (getId() / 100 != 9100000 || getId() == 910000000)) {
                    for (QuickMoveNpcInfo npc : QuickMoveNpcInfo.values()) {
                        if (npc.check(QuickMoveInfo.GLOBAL_NPC) && npc.show(getId())) {
                            quickMoveNpcInfos.add(npc);
                        }
                    }
                }
                if (!quickMoveNpcInfos.isEmpty()) {
                    chr.write(new LP_SetQuickMoveInfo(quickMoveNpcInfos));
                }
            }

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

            List<FieldObject> movingPlatforms = getObjects().stream()
                    .filter(FieldObject::isMove).collect(Collectors.toList());
            if (!movingPlatforms.isEmpty()) {
                chr.write(new LP_FootHoldMove(movingPlatforms));
            }


            //TODO  處理事件腳本

            chr.getForcedStatManager().onEnterField(getId());

            //叫出花狐 龍

            // 強化任意門剩餘次數


        }
        broadcastPacket(new LP_UserEnterField(chr), chr);
    }

    private boolean hasUserFirstEnterScript() {
        return getOnFirstUserEnter() != null && !getOnFirstUserEnter().equalsIgnoreCase("");
    }

    public void broadcastPacket(OutPacket outPacket, Character exceptChr) {
        getCharacters().stream().filter(chr -> !chr.equals(exceptChr)).forEach(
                chr -> chr.write(outPacket)
        );
    }


    @Override
    public String toString() {
        return String.format("[地圖] %s(%d)s", fieldData.getName(), fieldData.getId());
    }

    public void broadcastPacket(OutPacket<GameClient> outPacket) {
        for (Character c : getCharacters()) {
            c.getClient().write(outPacket);
        }
    }

    public void spawnAffectedArea(AffectedArea affectedArea) {
        addFieldObject(affectedArea);
        SkillInfo si = SkillData.getInstance().getSkillInfoById(affectedArea.getSkillID());
        if (si != null) {
            int duration = si.getValue(time, affectedArea.getSlv()) * 1000;
            ScheduledFuture sf = EventManager.getInstance().addEvent(() -> {
                removeSchedule(affectedArea, true);
                removeFieldObject(affectedArea);
            }, duration);
            addLifeSchedule(affectedArea, sf);
        }
        broadcastPacket(new LP_AffectedAreaCreated(affectedArea));
        getCharacters().forEach(chr -> affectedArea.getField().checkCharInAffectedAreas(chr));
        getMobs().forEach(mob -> affectedArea.getField().checkMobInAffectedAreas(mob));
    }


    public void spawnSummon(Summon summon) {
        summon.setField(this);
        addFieldObject(summon);
    }

    public void spawnMob(Mob mob) {
        if (getMobs().contains(mob))
            return;
        mob.setObjectId(getNewObjectID());
        mob.setField(this);
        addFieldObject(mob);
    }


    public int getNewObjectID() {
        return objectIDCounter.getAndIncrement();
    }


    public void removeDropFromUser(Drop drop) {
        AbstractFieldObject object = getFieldObjectsByKey(FieldObjectType.DROP, drop.getObjectId());
        broadcastPacket(new LP_DropLeaveField(DropLeaveType.PickedUpByUser, drop.getObjectId(), drop.getPickupId(), (short) 0, 0, 0));
        removeSchedule(object, true);
        removeFieldObject(object);
    }

    public void addLifeSchedule(AbstractFieldObject object, ScheduledFuture scheduledFuture) {
        getObjectSchedules().put(object, scheduledFuture);
    }

    public void removeSchedule(AbstractFieldObject life, boolean cancel) {
        if (!getObjectSchedules().containsKey(life)) {
            return;
        }
        if (!cancel) {
            getObjectSchedules().get(life).cancel(false);
        }
        getObjectSchedules().remove(life);
    }


    public void checkMobInAffectedAreas(Mob mob) {
        getAffectedAreas().stream().filter(aa -> aa.getRect().hasPositionInside(mob.getPosition())).forEach(aa -> {
            aa.handleMobInside(mob);
        });
    }

    public void checkCharInAffectedAreas(Character chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        for (AffectedArea aa : getAffectedAreas()) {
            boolean isInsideAA = aa.getRect().hasPositionInside(chr.getPosition());
            if (isInsideAA) {
                aa.handleCharInside(chr);
            } else if (tsm.hasAffectedArea(aa)) {
                tsm.removeAffectedArea(aa);
            }
        }
    }

    private void broadcastWithPredicate(OutPacket outPacket, Predicate<? super Character> predicate) {
        getCharacters().stream().filter(predicate).forEach(chr -> chr.write(outPacket));
    }

    /**
     * Drops an item to this map, given a {@link Drop}, a starting Position and an ending Position.
     * Immediately broadcasts the drop packet.
     *
     * @param drop    The Drop to drop.
     * @param posFrom The Position that the drop starts off from.
     * @param posTo   The Position where the drop lands.
     */
    public void drop(Drop drop, Position posFrom, Position posTo) {
        addFieldObject(drop);
        broadcastPacket(new LP_DropEnterField(drop, posFrom, posTo, 0));
    }

    /**
     * Drops a {@link Drop} according to a given {@link DropInfo DropInfo}'s specification.
     *
     * @param dropInfo The
     * @param posFrom  The Position that hte drop starts off from.
     * @param posTo    The Position where the drop lands.
     * @param ownerID  The owner's character ID. Will not be able to be picked up by Chars that are not the owner.
     */
    public void drop(DropInfo dropInfo, Position posFrom, Position posTo, int ownerID) {
        int itemID = dropInfo.getItemID();
        Item item;
        Drop drop = new Drop(-1);
        drop.setPosition(posTo);
        drop.setOwnerID(ownerID);
        if (itemID != 0) {
            item = ItemData.getInstance().getEquipFromTemplate(itemID);
            if (item == null) {
                item = ItemData.getInstance().getItemFromTemplate(itemID);
                item.setQuantity(Rand.get(dropInfo.getMinQuantity(), dropInfo.getMaxQuantity()));
                int slotMax = item.getTemplate().getSlotMax();
                if (slotMax > 0)
                    item.setQuantity(Math.min(slotMax, item.getQuantity()));
            }
            drop.setItem(item);
        } else {
            drop.setMoney(Rand.get(dropInfo.getMinQuantity(), dropInfo.getMaxQuantity()));
        }
        spawnDrop(drop);
        broadcastWithPredicate(new LP_DropEnterField(drop, posFrom, posTo, ownerID),
                (Character chr) -> dropInfo.getQuestReq() == 0 || chr.hasQuestInProgress(dropInfo.getQuestReq()));
    }

    /**
     * Drops a Set of {@link DropInfo}s from a base Position.
     *
     * @param dropInfos The Set of DropInfos.
     * @param position  The Position the initial Drop comes from.
     * @param ownerID   The owner's character ID.
     */
    public void drop(List<DropInfo> dropInfos, Position position, int ownerID) {
        drop(dropInfos, findFootHoldBelow(position), position, ownerID);
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
        drop(drop, position, posTo);
    }

    /**
     * Drops a Set of {@link DropInfo}s, locked to a specific {@link Foothold}.
     * Not all drops are guaranteed to be dropped, as this method calculates whether or not a Drop should drop, according
     * to the DropInfo's prop chance.
     *
     * @param dropInfos The Set of DropInfos that should be dropped.
     * @param fh        The Foothold this Set of DropInfos is bound to.
     * @param position  The Position the Drops originate from.
     * @param ownerID   The ID of the owner of all drops.
     */
    public void drop(List<DropInfo> dropInfos, Foothold fh, Position position, int ownerID) {
        int x = position.getX();
        int minX = fh.getX1();
        int maxX = fh.getX2();
        int diff = 0;
        for (DropInfo dropInfo : dropInfos) {
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
        return getCharacters().stream().filter(chr -> chr.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void execUserEnterScript(Character chr) {
        if (getOnUserEnter() == null || getOnUserEnter().equalsIgnoreCase("")) {
            return;
        }
        String script = getOnUserEnter();
        chr.getScriptManager().startScript(getId(), script, ScriptType.FIELD);
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

    public int getPartyBonusRate() {
        return getFieldData().getPartyBonusR();
    }

    public int getFixedMob() {
        return getFieldData().getFixedMobCapacity();
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

    public void updateMobController(Mob mob) {
        if (!mob.isAlive() || mob.isEscort()) {
            return;
        }
        if (mob.getController() != null) {
            mob.getController().stopControllingMob(mob);
        }
        int minControlledMobCount = -1;
        Character newController = null;
        getFieldObjectWriteLock(FieldObjectType.CHARACTER).lock();
        try {
            List<Character> characters = getCharacters();
            final Iterator<Character> ltr = characters.iterator();
            Character chr;
            while (ltr.hasNext()) {
                chr = ltr.next();
                if ((chr.getControlledMobs().size() < minControlledMobCount || minControlledMobCount == -1) && chr.getPosition().distanceSq(mob.getPosition()) <= GameConstants.maxViewRangeSq()) {
                    minControlledMobCount = chr.getControlledMobs().size();
                    newController = chr;
                }
            }
        } finally {
            getFieldObjectWriteLock(FieldObjectType.CHARACTER).unlock();
        }
        if (newController != null) {
            if (mob.isFirstAttack()) {
                newController.controlMob(mob, 2);
            } else {
                newController.controlMob(mob, 1);
            }
        } else {
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

    public List<Npc> getNpcs() {
        getFieldObjectReadLock(FieldObjectType.NPC).lock();
        try {
            return getFieldObjects().get(FieldObjectType.NPC).values().stream().map(o -> (Npc) o).collect(Collectors.toList());
        } finally {
            getFieldObjectReadLock(FieldObjectType.NPC).unlock();
        }
    }

    public List<Mob> getMobs() {
        getFieldObjectReadLock(FieldObjectType.MOB).lock();
        try {
            return getFieldObjects().get(FieldObjectType.MOB).values().stream().map(o -> (Mob) o).collect(Collectors.toList());
        } finally {
            getFieldObjectReadLock(FieldObjectType.MOB).unlock();
        }
    }


    public List<Character> getCharacters() {
        getFieldObjectReadLock(FieldObjectType.CHARACTER).lock();
        try {
            return getFieldObjects().get(FieldObjectType.CHARACTER).values().stream().map(o -> (Character) o).collect(Collectors.toList());
        } finally {
            getFieldObjectReadLock(FieldObjectType.CHARACTER).unlock();
        }
    }

    public List<Drop> getDrops() {
        getFieldObjectReadLock(FieldObjectType.DROP).lock();
        try {
            return getFieldObjects().get(FieldObjectType.DROP).values().stream().map(o -> (Drop) o).collect(Collectors.toList());
        } finally {
            getFieldObjectReadLock(FieldObjectType.DROP).unlock();
        }
    }

    public List<AffectedArea> getAffectedAreas() {
        getFieldObjectReadLock(FieldObjectType.AFFECTED_AREA).lock();
        try {
            return getFieldObjects().get(FieldObjectType.AFFECTED_AREA).values().stream().map(o -> (AffectedArea) o).collect(Collectors.toList());
        } finally {
            getFieldObjectReadLock(FieldObjectType.AFFECTED_AREA).unlock();
        }
    }


    public Character getCharByID(int id) {
        return getCharacters().stream()
                .filter(chr -> chr.getId() == id)
                .findFirst().orElse(null);
    }

    public Npc getNpcByObjectID(int objectId) {
        return (Npc) getFieldObjectsByKey(FieldObjectType.NPC, objectId);
    }

    public Npc getNpcByTemplateId(int templateId) {
        return getNpcs().stream()
                .filter(npc -> npc.getTemplateId() == templateId)
                .findFirst().orElse(null);
    }

    public Mob getMobByObjectId(int objectId) {
        return (Mob) getFieldObjectsByKey(FieldObjectType.MOB, objectId);
    }

    public Drop getDropByObjectId(int objectId) {
        return (Drop) getFieldObjectsByKey(FieldObjectType.DROP, objectId);
    }

    public void removeCharacter(Character chr) {
        getFieldObjectWriteLock(FieldObjectType.CHARACTER).lock();
        try {
            getFieldObjects().get(FieldObjectType.CHARACTER).remove(chr.getId());
        } finally {
            getFieldObjectWriteLock(FieldObjectType.CHARACTER).unlock();
        }
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
        getDrops().forEach(drop -> {
            if (drop.isExpired()) {
                // expire
            }
        });
        int charSize = getCharacters().size();

        if (charSize > 0) {
            if (getLastSpawnTime().plus(10 * 1000, ChronoUnit.MILLIS).isBefore(now)) {
                respawn(now, false);
                setLastSpawnTime(now.plus(10 * 1000, ChronoUnit.MILLIS));
            }
        }

        getMobs().forEach(mob -> {
            if (!mob.isAlive()) {
                mob.die();
            }
        });
    }

    public void removeMob(Mob mob) {
        removeFieldObject(mob);
        getCharacters().forEach(chr -> {
            chr.removeVisibleFieldObject(mob);
        });
    }

    public void spawnNpc(Npc npc) {
        if (getNpcs().contains(npc))
            return;
        npc.setObjectId(getNewObjectID());
        addFieldObject(npc);
    }

    public void spawnDrop(Drop drop) {
        if (getDrops().contains(drop)) {
            return;
        }
        drop.setObjectId(getNewObjectID());
        addFieldObject(drop);
    }
}

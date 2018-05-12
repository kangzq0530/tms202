package com.msemu.world.client.field;

import com.msemu.commons.data.templates.field.FieldObjectInfo;
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
import com.msemu.core.network.packets.out.field.LP_AffectedAreaRemoved;
import com.msemu.core.network.packets.out.field.LP_FootHoldMove;
import com.msemu.core.network.packets.out.field.LP_SetQuickMoveInfo;
import com.msemu.core.network.packets.out.mob.LP_MobChangeController;
import com.msemu.core.network.packets.out.mob.LP_MobEnterField;
import com.msemu.core.network.packets.out.npc.LP_NpcChangeController;
import com.msemu.core.network.packets.out.npc.LP_NpcEnterField;
import com.msemu.core.network.packets.out.summon.LP_SummonEnterField;
import com.msemu.core.network.packets.out.summon.LP_SummonLeaveField;
import com.msemu.core.network.packets.out.user.LP_UserEnterField;
import com.msemu.core.network.packets.out.user.LP_UserLeaveField;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.skill.TemporaryStatManager;
import com.msemu.world.client.life.*;
import com.msemu.world.client.life.skills.MobTemporaryStat;
import com.msemu.world.constants.FieldConstants;
import com.msemu.world.constants.GameConstants;
import com.msemu.world.data.ItemData;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.LeaveType;
import com.msemu.world.enums.QuickMoveInfo;
import com.msemu.world.enums.QuickMoveNpcInfo;
import com.msemu.world.enums.ScriptType;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
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
    private final List<Life> lifes;
    @Getter
    private final List<Character> chars;
    @Getter
    private final List<FieldObject> objects;
    @Getter
    private final Map<Life, Character> lifeToControllers;
    @Getter
    private final Map<Life, ScheduledFuture> lifeSchedules;
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

    public Field(long uniqueId, FieldTemplate template) {
        this.uniqueId = uniqueId;
        this.fieldData = template;
        this.lifes = new ArrayList<>();
        this.chars = new ArrayList<>();
        this.lifeToControllers = new HashMap<>();
        this.objects = new ArrayList<>();
        this.lifeSchedules = new HashMap<>();
        Position lBound = new Position(fieldData.getRect().getLeft(), fieldData.getRect().getTop());
        Position rBound = new Position(fieldData.getRect().getRight(), fieldData.getRect().getBottom());
        this.footholdTree = new FootholdTree(lBound, rBound);
        this.fieldData.getFootholds().forEach(this.footholdTree::insertFoothold);
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

    /**
     * Add a Life object to this map
     *
     * @param life the life object
     * @see Npc
     * @see Mob
     * @see Summon
     */
    public void addLife(Life life) {
        if (life.getObjectId() < 0) {
            life.setObjectId(getNewObjectID());
        }
        if (!getLifes().contains(life)) {
            getLifes().add(life);
            life.setField(this);
        }
    }

    public void removeLife(int id) {
        Life life = getLifeByObjectID(id);
        if (life == null) {
            return;
        }
        getLifes().remove(life);
    }

    public void spawnSummon(Summon summon) {
        Summon oldSummon = (Summon) getLifes().stream()
                .filter(s -> s instanceof Summon &&
                        ((Summon) s).getCharID() == summon.getCharID() &&
                        ((Summon) s).getSkillID() == summon.getSkillID())
                .findFirst().orElse(null);
        if (oldSummon != null) {
            removeLife(oldSummon.getObjectId(), false);
        }
        spawnLife(summon, null);
    }

    public void spawnAddSummon(Summon summon) { //Test
        spawnLife(summon, null);
    }

    public void spawnLife(Life life, Character onlyChar) {
        addLife(life);
        if (getCharacters().size() > 0) {
            Character controller = null;
            if (getLifeToControllers().containsKey(life)) {
                controller = getLifeToControllers().get(life);
            }
            if (controller == null) {
                controller = getCharacters().get(0);
                putLifeController(life, controller);
            }
            Mob mob = null;
            if (life instanceof Mob) {
                mob = (Mob) life;
                mob.setTemporaryStat(new MobTemporaryStat(mob));
            }
            if (mob != null) {
                Position pos = mob.getPosition();
                Foothold fh = getFootholdById(mob.getFh());
                if (fh == null) {
                    fh = findFootHoldBelow(pos);
                }
                mob.setHomeFoothold(fh.deepCopy());
                mob.setCurFoodhold(fh.deepCopy());
                if (onlyChar == null) {
                    for (Character chr : getCharacters()) {
                        chr.write(new LP_MobEnterField(mob, false));
                        chr.write(new LP_MobChangeController(mob, false, controller == chr));
                    }
                } else {
                    onlyChar.getClient().write(new LP_MobEnterField(mob, false));
                    onlyChar.getClient().write(new LP_MobChangeController(mob, false, controller == onlyChar));
                }
            }
            if (life instanceof Summon) {
                Summon summon = (Summon) life;
                if (summon.getSummonTerm() > 0) {
                    ScheduledFuture sf = EventManager.getInstance().addEvent(() -> removeLife(summon.getObjectId(), true), summon.getSummonTerm());
                    addLifeSchedule(summon, sf);
                }
                broadcastPacket(new LP_SummonEnterField(summon.getCharID(), summon));
            }
            if (life instanceof Npc) {
                Npc npc = (Npc) life;
                for (Character chr : getCharacters()) {
                    chr.write(new LP_NpcEnterField(npc));
                    chr.write(new LP_NpcChangeController(npc, true));
                }
            }
        }
    }

    private void removeLife(Life life) {
        if (getLifes().contains(life)) {
            getLifes().remove(life);
        }
    }

    public Foothold getFootholdById(int fh) {
        return getFootholdTree().getFootholdById(fh);
    }

    public List<Character> getCharacters() {
        return chars;
    }

    public Character getCharByID(int id) {
        return getCharacters().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public void addCharacter(Character chr) {
        if (!getCharacters().contains(chr)) {
            getCharacters().add(chr);

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
            if(ft.getTimeLimit() > 0 && ft.getReturnMap() > 0) {
                //TODO 計時回地圖
            } else if (ft.getTimeLimit() > 0 && ft.getForcedReturn() > 0) {
                //TODO 計時回地圖
            }

            // LP_BlowWeather

            List<FieldObject> movingPlatforms = getObjects().stream()
                    .filter(FieldObject::isMove).collect(Collectors.toList());
            if(!movingPlatforms.isEmpty()) {
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

    public void removeCharacter(Character chr) {
        if (getCharacters().contains(chr)) {
            getCharacters().remove(chr);
            broadcastPacket(new LP_UserLeaveField(chr), chr);
        }
        getLifeToControllers().entrySet().stream().filter(entry -> entry.getValue() != null && entry.getValue().equals(chr)).forEach(entry -> {
            putLifeController(entry.getKey(), null);
        });
    }

    public void putLifeController(Life life, Character chr) {
        getLifeToControllers().put(life, chr);
    }

    public Character getLifeController(Life life) {
        return getLifeToControllers().getOrDefault(life, null);
    }

    public Life getLifeByObjectID(int objectID) {
        return getLifes().stream()
                .filter(life -> life.getObjectId() == objectID)
                .findFirst().orElse(null);
    }

    public Npc getNpcByObjectID(int objectID) {
        return getLifes().stream()
                .filter(life -> life instanceof Npc && life.getObjectId() == objectID)
                .map(life -> (Npc) life)
                .findFirst().orElse(null);
    }

    public void spawnAllLifeForChar(Character chr) {
        for (Life life : getLifes()) {
            spawnLife(life, chr);
        }
    }

    @Override
    public String toString() {
        return "[field] ID = " + getId();
    }

    public void respawn(Mob mob) {
        mob.setHp(mob.getMaxHp());
        mob.setMp(mob.getMaxMp());
        mob.setPosition(mob.getHomePosition().deepCopy());
        spawnLife(mob, null);
    }

    public void broadcastPacket(OutPacket<GameClient> outPacket) {
        for (Character c : getCharacters()) {
            c.getClient().write(outPacket);
        }
    }

    public void spawnAffectedArea(AffectedArea aa) {
        addLife(aa);
        SkillInfo si = SkillData.getInstance().getSkillInfoById(aa.getSkillID());
        if (si != null) {
            int duration = si.getValue(time, aa.getSlv()) * 1000;
            ScheduledFuture sf = EventManager.getInstance().addEvent(() -> removeLife(aa.getObjectId(), true), duration);
            addLifeSchedule(aa, sf);
        }
        broadcastPacket(new LP_AffectedAreaCreated(aa));
        getCharacters().forEach(chr -> aa.getField().checkCharInAffectedAreas(chr));
        getMobs().forEach(mob -> aa.getField().checkMobInAffectedAreas(mob));
    }

    public List<Mob> getMobs() {
        return getLifes().stream().filter(life -> life instanceof Mob).map(l -> (Mob) l).collect(Collectors.toList());
    }

    public int getNewObjectID() {
        return objectIDCounter.getAndIncrement();
    }

    public List<Life> getLifesInRect(Rect rect) {
        List<Life> lifes = new ArrayList<>();
        for (Life life : getLifes()) {
            Position position = life.getPosition();
            int x = position.getX();
            int y = position.getY();
            if (x >= rect.getLeft() && y >= rect.getTop()
                    && x <= rect.getRight() && y <= rect.getBottom()) {
                lifes.add(life);
            }
        }
        return lifes;
    }

    public synchronized void removeLife(Integer id, Boolean fromSchedule) {
        Life life = getLifeByObjectID(id);
        if (life == null) {
            return;
        }
        removeLife(id);
        removeSchedule(life, fromSchedule);
        if (life instanceof Summon) {
            Summon summon = (Summon) life;
            broadcastPacket(new LP_SummonLeaveField(summon, LeaveType.ANIMATION));
        } else if (life instanceof AffectedArea) {
            AffectedArea aa = (AffectedArea) life;
            broadcastPacket(new LP_AffectedAreaRemoved(aa, false));
            for (Character chr : getCharacters()) {
                TemporaryStatManager tsm = chr.getTemporaryStatManager();
                if (tsm.hasAffectedArea(aa)) {
                    tsm.removeStatsBySkill(aa.getSkillID());
                }
            }
        }
    }

    public synchronized void removeDrop(Integer dropID, Integer pickupUserID, Boolean fromSchedule) {
        Life life = getLifeByObjectID(dropID);
        if (life instanceof Drop) {
            broadcastPacket(new LP_DropLeaveField(dropID, pickupUserID));
            removeLife(dropID, fromSchedule);
        }
    }

    public void addLifeSchedule(Life life, ScheduledFuture scheduledFuture) {
        getLifeSchedules().put(life, scheduledFuture);
    }

    public void removeSchedule(Life life, boolean fromSchedule) {
        if (!getLifeSchedules().containsKey(life)) {
            return;
        }
        if (!fromSchedule) {
            getLifeSchedules().get(life).cancel(false);
        }
        getLifeSchedules().remove(life);
    }

    public List<AffectedArea> getAffectedAreas() {
        return getLifes().stream().filter(life -> life instanceof AffectedArea).map(l -> (AffectedArea) l).collect(Collectors.toList());
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
        addLife(drop);
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
        addLife(drop);
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
            int x = portals2.getX();
            int y = portals2.getY();
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

    public Npc getNpcByTemplateID(int npcID) {
        return getLifes().stream().filter(life -> life instanceof Npc && life.getTemplateId() == npcID)
                .map(life -> (Npc) life).findFirst().orElse(null);
    }

    public void startTimeLimitTask(int time, Field toField) {


    }
}

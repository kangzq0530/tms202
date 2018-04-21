package com.msemu.world.client.character;

import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.commons.utils.types.Position;
import com.msemu.world.client.GameClient;
import com.msemu.world.client.guild.Guild;
import com.msemu.world.client.character.items.Equip;
import com.msemu.world.client.character.items.Item;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.character.quest.Quest;
import com.msemu.world.client.character.quest.QuestManager;
import com.msemu.world.client.character.skills.Skill;
import com.msemu.world.client.character.skills.TemporaryStatManager;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.Portal;
import com.msemu.world.client.guild.GuildMember;
import com.msemu.world.client.guild.operations.GuildUpdate;
import com.msemu.world.client.guild.operations.GuildUpdateMemberLogin;
import com.msemu.world.client.jobs.Job;
import com.msemu.world.client.life.AffectedArea;
import com.msemu.world.client.life.Pet;
import com.msemu.world.constants.GameConstants;
import com.msemu.world.constants.ItemConstants;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.constants.SkillConstants;
import com.msemu.world.data.ItemData;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.*;
import com.msemu.core.network.packets.out.Stage.SetField;
import com.msemu.core.network.packets.out.UserPool.UserEnterField;
import com.msemu.core.network.packets.out.UserPool.UserLocal.ChatMsg;
import com.msemu.core.network.packets.out.WvsContext.GuildResult;
import com.msemu.core.network.packets.out.WvsContext.InventoryOperation;
import com.msemu.core.network.packets.out.WvsContext.StatChanged;
import com.msemu.core.network.packets.out.WvsContext.messages.IncExpMessage;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;
import java.util.function.Predicate;

import static com.msemu.world.enums.ChatMsgColor.YELLOW;
import static com.msemu.world.enums.InventoryOperationType.*;

/**
 * Created by Weber on 2018/3/29.
 */
@Schema
@MappedSuperclass
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private int id;

    @Column(name = "accId")
    @Getter
    @Setter
    private int accId;

    @JoinColumn(name = "questManager")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private QuestManager questManager;

    @JoinColumn(name = "equippedInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Inventory equippedInventory = new Inventory(InvType.EQUIPPED, 50);

    @JoinColumn(name = "equipInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Inventory equipInventory = new Inventory(InvType.EQUIP, 50);

    @JoinColumn(name = "consumeInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Inventory consumeInventory = new Inventory(InvType.CONSUME, 50);

    @JoinColumn(name = "etcInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Inventory etcInventory = new Inventory(InvType.ETC, 50);

    @JoinColumn(name = "installInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Inventory installInventory = new Inventory(InvType.INSTALL, 50);

    @JoinColumn(name = "cashInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Inventory cashInventory = new Inventory(InvType.CASH, 50);

    @JoinColumn(name = "avatarData")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private AvatarData avatarData;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private FuncKeyMap funcKeyMap;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "charId")
    @Getter
    @Setter
    private List<Skill> skills;

    @JoinColumn(name = "guild")
    @OneToOne(cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Guild guild;

    @Transient
    @Getter
    @Setter
    private GameClient client;

    @Transient
    @Getter
    @Setter
    private Ranking ranking;

    @Transient
    @Getter
    @Setter
    private int combatOrders;

    @Transient
    @Getter
    @Setter
    private List<Skill> stolenSkills;

    @Transient
    @Getter
    @Setter
    private List<Skill> chosenSkills;

    @Transient
    @Getter
    @Setter
    private List<ItemPot> itemPots;

    @Transient
    @Getter
    @Setter
    private List<Pet> pets;

    @Transient
    @Getter
    @Setter
    private List<FriendRecord> friends;

    @Transient
    @Getter
    @Setter
    private List<ExpConsumeItem> expConsumeItems;

    @Transient
    @Getter
    @Setter
    private List<MonsterBattleMobInfo> monsterBattleMobInfos;

    @Transient
    @Getter
    @Setter
    private MonsterBattleLadder monsterBattleLadder;

    @Transient
    @Getter
    @Setter
    private MonsterBattleRankInfo monsterBattleRankInfo;

    @Transient
    @Getter
    @Setter
    private Position position;

    @Transient
    @Getter
    @Setter
    private Position oldPosition;

    @Transient
    private Field field;

    @Transient
    @Getter
    @Setter
    private byte moveAction;

    @Transient
    @Getter
    @Setter
    private TemporaryStatManager temporaryStatManager;

    @Transient
    @Getter
    @Setter
    private Job jobHandler;

    @Transient
    @Getter
    @Setter
    private boolean left;

    @Transient
    @Getter
    @Setter
    private MarriageRecord marriageRecord;

    @Transient
    @Getter
    @Setter
    private WildHunterInfo wildHunterInfo;

    @Transient
    @Getter
    @Setter
    private ZeroInfo zeroInfo;

    @Transient
    @Getter
    @Setter
    private int nickItem;

    @Transient
    @Getter
    @Setter
    private int damageSkin;

    @Transient
    @Getter
    @Setter
    private int premiumDamageSkin;

    @Transient
    @Getter
    @Setter
    private boolean partyInvitable;

    @Transient
    @Getter
    @Setter
    private ScriptManager scriptManager = new ScriptManager(this);

    @Transient
    @Getter
    @Setter
    private int driverID;

    @Transient
    @Getter
    @Setter
    private int passengerID;

    @Transient
    @Getter
    @Setter
    private int chocoCount;

    @Transient
    private int activeEffectItemID;
    @Transient
    private int monkeyEffectItemID;
    @Transient
    private int completedSetItemID;
    @Transient
    private short fieldSeatID;
    @Transient
    private int portableChairID;
    @Transient
    private String portableChairMsg;
    @Transient
    private short foothold;
    @Transient
    private int tamingMobLevel;
    @Transient
    private int tamingMobExp;
    @Transient
    private int tamingMobFatigue;
    @Transient
    private MiniRoom miniRoom;
    @Transient
    private String ADBoardRemoteMsg;
    @Transient
    private boolean inCouple;
    @Transient
    private CoupleRecord couple;
    @Transient
    private FriendshipRingRecord friendshipRingRecord;
    @Transient
    private int evanDragonGlide;
    @Transient
    private int kaiserMorphRotateHueExtern;
    @Transient
    private int kaiserMorphPrimiumBlack;
    @Transient
    private int kaiserMorphRotateHueInnner;
    @Transient
    private int makingMeisterSkillEff;
    @Transient
    private FarmUserInfo farmUserInfo;
    @Transient
    private int customizeEffect;
    @Transient
    private String customizeEffectMsg;
    @Transient
    private byte soulEffect;
    @Transient
    private FreezeHotEventInfo freezeHotEventInfo;
    @Transient
    private int eventBestFriendAID;
    @Transient
    private int mesoChairCount;
    @Transient
    private boolean beastFormWingOn;
    @Transient
    private int activeNickItemID;
    @Transient
    private int mechanicHue;
    @Transient
    private boolean online;
    @Transient
    @Getter
    @Setter
    private Party party;
    @Transient
    @Getter
    @Setter
    private FieldInstanceType fieldInstanceType;
    @Transient
    private Map<Integer, Field> fields = new HashMap<>();
    @Transient
    @Getter
    @Setter
    private int bulletIDForAttack;

    public Character() {
        avatarData = new AvatarData();
        avatarData.setAvatarLook(new AvatarLook());
    }


    public String getName() {
        return getAvatarData().getCharacterStat().getName();
    }

    public long getMoney() {
        return getAvatarData().getCharacterStat().getMoney();
    }

    public short getJob() {
        return (short) getAvatarData().getCharacterStat().getJob();
    }

    public int getLevel() {
        return getAvatarData().getCharacterStat().getLevel();
    }

    public short getSubJob() {
        return (short) getAvatarData().getCharacterStat().getSubJob();
    }

    public boolean hasQuestInProgress(int questReq) {
        return getQuestManager().hasQuestInProgress(questReq);
    }

    public void write(OutPacket outPacket) {
        if (getClient() != null) {
            getClient().write(outPacket);
        }
    }

    public ExpIncreaseInfo getExpIncreaseInfo() {
        return new ExpIncreaseInfo();
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
        setFieldID(field.getId());
    }

    public int getFieldID() {
        return (int) getAvatarData().getCharacterStat().getPosMap();
    }

    private void setFieldID(long fieldId) {
        setFieldID((int) fieldId);
    }

    private void setFieldID(int fieldID) {
        getAvatarData().getCharacterStat().setPosMap(fieldID);
    }


    public void addExp(long amount) {
        ExpIncreaseInfo eii = new ExpIncreaseInfo();
        eii.setLastHit(true);
        eii.setIncEXP((int) Math.min(Integer.MAX_VALUE, amount));
        addExp(amount, eii);
    }

    public void addExp(long amount, ExpIncreaseInfo eii) {
        CharacterStat cs = getAvatarData().getCharacterStat();
        long curExp = cs.getExp();
        int level = getStat(Stat.LEVEL);
        if (level >= GameConstants.charExp.length - 1) {
            return;
        }
        long newExp = curExp + amount;
        Map<Stat, Object> stats = new HashMap<>();
        while (newExp > GameConstants.charExp[level]) {
            newExp -= GameConstants.charExp[level];
            addStat(Stat.LEVEL, 1);
            stats.put(Stat.LEVEL, (byte) getStat(Stat.LEVEL));
            getJobHandler().handleLevelUp();
            level++;
        }
        cs.setExp(newExp);
        stats.put(Stat.EXP, newExp);
        write(new IncExpMessage(eii));
        getClient().write(new StatChanged(stats));
    }

    public void addStat(Stat charStat, int amount) {
        setStat(charStat, getStat(charStat) + amount);
    }

    public int getStat(Stat charStat) {
        CharacterStat cs = getAvatarData().getCharacterStat();
        switch (charStat) {
            case STR:
                return cs.getStr();
            case DEX:
                return cs.getDex();
            case INT:
                return cs.getInt();
            case LUK:
                return cs.getLuk();
            case HP:
                return cs.getHp();
            case MAXHP:
                return cs.getMaxHp();
            case MP:
                return cs.getMp();
            case MAXMP:
                return cs.getMaxMp();
            case AP:
                return cs.getAp();
            case LEVEL:
                return cs.getLevel();
        }
        return -1;
    }

    public void setStat(Stat charStat, int amount) {
        switch (charStat) {
            case STR:
                getAvatarData().getCharacterStat().setStr(amount);
                break;
            case DEX:
                getAvatarData().getCharacterStat().setDex(amount);
                break;
            case INT:
                getAvatarData().getCharacterStat().setInt(amount);
                break;
            case LUK:
                getAvatarData().getCharacterStat().setLuk(amount);
                break;
            case HP:
                getAvatarData().getCharacterStat().setHp(amount);
                break;
            case MAXHP:
                getAvatarData().getCharacterStat().setMaxHp(amount);
                break;
            case MP:
                getAvatarData().getCharacterStat().setMp(amount);
                break;
            case MAXMP:
                getAvatarData().getCharacterStat().setMaxMp(amount);
                break;
            case AP:
                getAvatarData().getCharacterStat().setAp(amount);
                break;
            case LEVEL:
                getAvatarData().getCharacterStat().setLevel(amount);
                notifyChanges();
                break;
            case JOB:
                getAvatarData().getCharacterStat().setJob(amount);
        }
    }

    private void notifyChanges() {
        if (getParty() != null) {
            getParty().updateFull();
        }
        if (getGuild() != null) {
            // TODO
        }
    }


    public boolean isMarried() {
        // TODO
        return false;
    }

    public boolean hasBuffProtector() {
        // TODO
        return false;
    }

    public List<Inventory> getInventories() {
        return new ArrayList<>(Arrays.asList(getEquippedInventory(), getEquipInventory(),
                getConsumeInventory(), getEtcInventory(), getInstallInventory(), getCashInventory()));
    }

    public Inventory getInventoryByType(InvType invType) {
        switch (invType) {
            case EQUIPPED:
                return getEquippedInventory();
            case EQUIP:
                return getEquipInventory();
            case CONSUME:
                return getConsumeInventory();
            case ETC:
                return getEtcInventory();
            case INSTALL:
                return getInstallInventory();
            case CASH:
                return getCashInventory();
            default:
                return null;
        }
    }

    /**
     * Consumes a single {@link Item} from this Char's {@link Inventory}. Will remove the Item if it has a quantity of 1.
     *
     * @param item The Item to consume, which is currently in the Char's inventory.
     */
    public void consumeItem(Item item) {
        Inventory inventory = getInventoryByType(item.getInvType());
        // data race possible
        if (item.getQuantity() <= 1 && !ItemConstants.isThrowingItem(item.getItemId())) {
            item.setQuantity(0);
            inventory.removeItem(item);
            write(new InventoryOperation(true, false,
                    REMOVE, (short) item.getBagIndex(), (byte) -1, 0, item));
        } else {
            item.setQuantity(item.getQuantity() - 1);
            write(new InventoryOperation(true, false,
                    UPDATE_QUANTITY, (short) item.getBagIndex(), (byte) -1, 0, item));
        }
        setBulletIDForAttack(calculateBulletIDForAttack());
    }

    /**
     * Consumes an item of this Char with the given id. Will do nothing if the Char doesn't have the Item.
     * Only works for non-Equip (i.e., type is not EQUIPPED or EQUIP, CASH is fine) itemTemplates.
     * Calls {@link #consumeItem(Item)} if an Item is found.
     *
     * @param id       The Item's id.
     * @param quantity The amount to consume.
     */
    public void consumeItem(int id, int quantity) {
        quantity -= 1;
        Item checkItem = ItemData.getItemDeepCopy(id);
        Item item = getInventoryByType(checkItem.getInvType()).getItemByItemID(id);
        item.setQuantity(quantity);
        consumeItem(item);
    }

    public boolean hasItem(int itemID) {
        return getInventories().stream().anyMatch(inv -> inv.containsItem(itemID));
    }

    public boolean hasItemCount(int itemID, int quantity) {
        return getInventories().stream().anyMatch(inv -> {
            Item item = inv.getItemByItemID(itemID);
            return item != null && item.getQuantity() >= quantity;
        });
    }

    public void addMoney(long amount) {
        CharacterStat cs = getAvatarData().getCharacterStat();
        long money = cs.getMoney();
        long newMoney = money + amount;
        if (newMoney >= 0) {
            newMoney = Math.min(GameConstants.MAX_MONEY, newMoney);
            Map<Stat, Object> stats = new HashMap<>();
            cs.setMoney(newMoney);
            stats.put(Stat.MONEY, newMoney);
            write(new StatChanged(stats));
        }
    }

    public int calculateBulletIDForAttack() {
        Item weapon = getEquippedInventory().getFirstItemByBodyPart(BodyPart.WEAPON);
        if (weapon == null) {
            return 0;
        }
        Predicate<Item> p;
        int id = weapon.getItemId();

        if (ItemConstants.isClaw(id)) {
            p = i -> ItemConstants.isThrowingStar(i.getItemId());
        } else if (ItemConstants.isBow(id)) {
            p = i -> ItemConstants.isBowArrow(i.getItemId());
        } else if (ItemConstants.isXBow(id)) {
            p = i -> ItemConstants.isXBowArrow(i.getItemId());
        } else if (ItemConstants.isGun(id)) {
            p = i -> ItemConstants.isBullet(i.getItemId());
        } else {
            return 0;
        }
        Item i = getConsumeInventory().getItems().stream().filter(p).findFirst().orElse(null);
        return i != null ? i.getItemId() : 0;
    }

    public void addItemToInventory(InvType type, Item item, boolean hasCorrectBagIndex) {
        getQuestManager().handleItemGain(item);
        Inventory inventory = getInventoryByType(type);
        if (inventory != null) {
            Item existingItem = inventory.getItemByItemID(item.getItemId());
            if (existingItem != null && existingItem.getInvType().isStackable()) {
                existingItem.addQuantity(item.getQuantity());
                write(new InventoryOperation(true, false,
                        UPDATE_BAG_QUANTITY, (short) existingItem.getBagIndex(), (byte) -1, 0, existingItem));
            } else {
                item.setInventoryId(inventory.getId());
                if (!hasCorrectBagIndex) {
                    item.setBagIndex(inventory.getFirstOpenSlot());
                }
                inventory.addItem(item);
                if (item.getId() == 0) {
                    DatabaseFactory.getInstance().saveToDB(item);
                }
                write(new InventoryOperation(true, false,
                        ADD, (short) item.getBagIndex(), (byte) -1, 0, item));
            }
            setBulletIDForAttack(calculateBulletIDForAttack());
        }
    }

    public void addItemToInventory(Item item) {
        addItemToInventory(item.getInvType(), item, false);
    }

    /**
     * Sends a message to this Char with a default colour {@link ChatMsgColor#YELLOW}.
     *
     * @param msg The message to display.
     */
    public void chatMessage(String msg) {
        chatMessage(YELLOW, msg);
    }

    /**
     * Sends a message to this Char with a given {@link ChatMsgColor colour}.
     *
     * @param clr The Colour this message should be in.
     * @param msg The message to display.
     */
    public void chatMessage(ChatMsgColor clr, String msg) {
        getClient().write(new ChatMsg(clr, msg));
    }

    /**
     * Adds a {@link Skill} to this Char. Overrides the old Skill if the Char already had a Skill with the same id.
     *
     * @param skill The Skill this Char should get.
     */
    public void addSkill(Skill skill) {
        skill.setCharId(getId());
        if (getSkills().stream().noneMatch(s -> s.getSkillId() == skill.getSkillId())) {
            getSkills().add(skill);
        } else {
            Skill oldSkill = getSkill(skill.getSkillId());
            oldSkill.setCurrentLevel(skill.getCurrentLevel());
            oldSkill.setMasterLevel(skill.getMasterLevel());
        }
    }

    /**
     * Returns whether or not this Char has a {@link Skill} with a given id.
     *
     * @param id The id of the Skill.
     * @return Whether or not this Char has a Skill with the given id.
     */
    public boolean hasSkill(int id) {
        return getSkills().stream().anyMatch(s -> s.getSkillId() == id) && getSkill(id, false).getCurrentLevel() > 0;
    }

    /**
     * Gets a {@link Skill} of this Char with a given id.
     *
     * @param id The id of the requested Skill.
     * @return The Skill corresponding to the given id of this Char, or null if there is none.
     */
    public Skill getSkill(int id) {
        return getSkill(id, false);
    }

    /**
     * Gets a {@link Skill} with a given ID. If <code>createIfNull</code> is true, creates the Skill if it doesn't exist yet.
     * If it is false, will return null if this Char does not have the given Skill.
     *
     * @param id           The id of the requested Skill.
     * @param createIfNull Whether or not this method should create the Skill if it doesn't exist.
     * @return The Skill that the Char has, or <code>null</code> if there is no such skill and <code>createIfNull</code> is false.
     */
    public Skill getSkill(int id, boolean createIfNull) {
        for (Skill s : getSkills()) {
            if (s.getSkillId() == id) {
                return s;
            }
        }
        return createIfNull ? createAndReturnSkill(id) : null;
    }

    /**
     * Creates a new {@link Skill} for this Char.
     *
     * @param id The skillID of the Skill to be created.
     * @return The new Skill.
     */
    private Skill createAndReturnSkill(int id) {
        Skill skill = SkillData.getSkillDeepCopyById(id);
        addSkill(skill);
        return skill;
    }

    /**
     * Warps this character to a given field, at the starting position.
     * See {@link #warp(Field, Portal) warp}.
     *
     * @param toField The field to warp to.
     */
    public void warp(Field toField) {
        warp(toField, toField.getPortalByName("sp"), false);
    }

    /**
     * Warps this Char to a given {@link Field}, with the Field's "sp" portal as spawn position.
     *
     * @param toField       The Field to warp to.
     * @param characterData Whether or not the character data should be encoded.
     */
    public void warp(Field toField, boolean characterData) {
        warp(toField, toField.getPortalByName("sp"), characterData);
    }

    /**
     * Warps this Char to a given {@link Field} and {@link Portal}. Will not include character data.
     *
     * @param toField  The Field to warp to.
     * @param toPortal The Portal to spawn at.
     */
    public void warp(Field toField, Portal toPortal) {
        warp(toField, toPortal, false);
    }

    /**
     * Warps this character to a given field, at a given portal.
     * Ensures that the previous map does not contain this Char anymore, and that the new field does.
     * Ensures that all Lifes are immediately spawned for the new player.
     *
     * @param toField The {@link Field} to warp to.
     * @param portal  The {@link Portal} where to spawn at.
     */
    public void warp(Field toField, Portal portal, boolean characterData) {
        TemporaryStatManager tsm = getTemporaryStatManager();
        for (AffectedArea aa : tsm.getAffectedAreas()) {
            tsm.removeStatsBySkill(aa.getSkillID());
        }
        if (getField() != null) {
            getField().removeCharacter(this);
        }
        setField(toField);
        toField.addChar(this);
        getClient().write(new SetField(this, toField, getClient().getChannel(), false, 0, characterData, hasBuffProtector(),
                (byte) portal.getId(), false, 100, null, true, -1));
        if (characterData) {
            if (getGuild() != null) {

                write(new GuildResult(new GuildUpdate(getGuild())));
            }
        }
        toField.spawnLifesForChar(this);
        for (Character c : toField.getCharacters()) {
            if (!c.equals(this)) {
                write(new UserEnterField(c));
            }
        }
        notifyChanges();
        toField.execUserEnterScript(this);
    }

    public void initZeroInfo() {
        ZeroInfo zeroInfo = new ZeroInfo();
        CharacterStat cs = getAvatarData().getCharacterStat();
        zeroInfo.setSubHP(cs.getHp());
        zeroInfo.setSubMHP(cs.getMaxHp());
        zeroInfo.setSubMP(cs.getMp());
        zeroInfo.setSubMMP(cs.getMaxMp());
        setZeroInfo(zeroInfo);
    }

    public void encode(OutPacket outPacket, DBChar mask) {
        // CharacterData::Decode
        outPacket.encodeLong(mask.get());
        outPacket.encodeByte(getCombatOrders());
        for (Pet pet : getPets()) {
            outPacket.encodeInt(pet.getActiveSkillCoolTime());
        }
        // 拼圖?
        byte sizeByte = 0;
        outPacket.encodeByte(sizeByte);
        for (int i = 0; i < sizeByte; i++) {
            outPacket.encodeInt(0);
        }

        int sizee = 0;
        outPacket.encodeInt(sizee);
        for (int i = 0; i < sizee; i++) {
            outPacket.encodeInt(0); // nKey
            outPacket.encodeLong(0); // pInfo
        }

        boolean v215 = false;
        outPacket.encodeByte(v215);// unk
        if (v215) {
            outPacket.encodeByte(0);
            int v11 = 0;
            outPacket.encodeInt(v11);
            for (int i = 0; i < v11; i++) {
                outPacket.encodeLong(0);
            }
            int v14 = 0;
            outPacket.encodeInt(v14);
            for (int i = 0; i < v14; i++) {
                outPacket.encodeLong(0);
            }
        }

        if (mask.isInMask(DBChar.Character)) {
            getAvatarData().getCharacterStat().encode(outPacket);
            outPacket.encodeByte(getFriends().size());
            // 精靈的祝福
            boolean hasBlessingOfFairy = getBlessingOfFairy() != null;
            outPacket.encodeByte(hasBlessingOfFairy);
            if (hasBlessingOfFairy) {
                outPacket.encodeString(getBlessingOfFairy());
            }
            // 女皇的祝福
            boolean hasBlessingOfEmpress = getBlessingOfEmpress() != null;
            outPacket.encodeByte(hasBlessingOfEmpress);
            if (hasBlessingOfEmpress) {
                outPacket.encodeString(getBlessingOfEmpress());
            }
            // 終極冒險家 (捨棄了)
            outPacket.encodeByte(false); // ultimate explorer, deprecated

            outPacket.encodeFT(FileTime.getTime());
            outPacket.encodeFT(new FileTime(-2));

            //sub_510BF0
            int v7 = 2;
            do {
                outPacket.encodeInt(0);
                while (true) {
                    int res = 255;
                    outPacket.encodeByte(res);
                    if (res == 255) {
                        break;
                    }
                    outPacket.encodeInt(0);
                }
                v7 += 36;
            } while (v7 < 74);
        }
        if (mask.isInMask(DBChar.Money)) {
            outPacket.encodeLong(getMoney());
            outPacket.encodeInt(getId());
            // TODO 楓葉點數、小鋼珠
            int maplepoints = 0;
            int pachingoPoints = 0;
            outPacket.encodeInt(maplepoints);
            outPacket.encodeInt(pachingoPoints);
        }
        if (mask.isInMask(DBChar.ItemSlotConsume) || mask.isInMask(DBChar.ExpConsumeItem)) {
            outPacket.encodeInt(getExpConsumeItems().size());
            for (ExpConsumeItem eci : getExpConsumeItems()) {
                eci.encode(outPacket);
            }
        }

        if (mask.isInMask(DBChar.InventorySize)) {
            outPacket.encodeByte(getEquipInventory().getSlots());
            outPacket.encodeByte(getConsumeInventory().getSlots());
            outPacket.encodeByte(getEtcInventory().getSlots());
            outPacket.encodeByte(getInstallInventory().getSlots());
            outPacket.encodeByte(getCashInventory().getSlots());
        }

        if (mask.isInMask(DBChar.AdminShopCount)) {
            // ???
            outPacket.encodeFT(new FileTime(-2));
        }
        if (mask.isInMask(DBChar.ItemSlotEquip)) {
            outPacket.encodeByte(0); // ?
            List<Item> equippedItems = getEquippedInventory().getItems();
            equippedItems.sort(Comparator.comparingInt(Item::getBagIndex));
            for (Item item : equippedItems) {
                Equip equip = (Equip) item;
                if (item.getBagIndex() > 0 && item.getBagIndex() < 100) {
                    outPacket.encodeShort(equip.getBagIndex());
                    equip.encode(outPacket);
                }
            }
            outPacket.encodeShort(0);
            for (Item item : getEquippedInventory().getItems()) {
                Equip equip = (Equip) item;
                if (item.getBagIndex() > 100 && item.getBagIndex() < 1000) {
                    outPacket.encodeShort(equip.getBagIndex());
                    equip.encode(outPacket);
                }
            }
            outPacket.encodeShort(0);
            for (Item item : getEquipInventory().getItems()) {
                Equip equip = (Equip) item;
                outPacket.encodeShort(equip.getBagIndex());
                equip.encode(outPacket);
            }
            outPacket.encodeShort(0);
            // NonBPEquip::Decode
            int[] NonBPEquipBasePos = {1000, 1100, 1200, 1300, 1400, 1500, 5100, 1600, 5200, 5000, 6000};
            int[] NonBPEquipEndPos = {1004, 1105, 1207, 1305, 1425, 1512, 5106, 1606, 5201, 5003, 6025};
            for (int i = 0; i < NonBPEquipBasePos.length; i++) {
                int beginPos = NonBPEquipBasePos[i];
                int endPos = NonBPEquipEndPos[i];
                for (Item item : getEquippedInventory().getItems()) {
                    Equip equip = (Equip) item;
                    if (item.getBagIndex() >= beginPos && item.getBagIndex() < endPos) {
                        outPacket.encodeShort(equip.getBagIndex());
                        equip.encode(outPacket);
                    }
                }
                outPacket.encodeShort(0);
            }

            // VirtualEquipInventory::Decode
            for (Item item : getEquippedInventory().getItems()) {
                Equip equip = (Equip) item;
                if (item.getBagIndex() >= 20000 && item.getBagIndex() < 20024) {
                    outPacket.encodeShort(equip.getBagIndex());
                    equip.encode(outPacket);
                }
            }
            outPacket.encodeShort(0);
            for (Item item : getEquippedInventory().getItems()) {
                Equip equip = (Equip) item;
                if (item.getBagIndex() >= 21000 && item.getBagIndex() < 21024) {
                    outPacket.encodeShort(equip.getBagIndex());
                    equip.encode(outPacket);
                }
            }
            outPacket.encodeShort(0);

            /////
            // 201 +
            int v151 = 0;
            outPacket.encodeInt(v151);
            for (int i = 0; i < v151; i++) {
                outPacket.encodeLong(0);
                // sub_4EBE40
                outPacket.encodeShort(0);
                outPacket.encodeShort(0);
                outPacket.encodeShort(0);
                outPacket.encodeString("");
                outPacket.encodeInt(0);
                outPacket.encodeLong(0);
            }
        }
        if (mask.isInMask(DBChar.ItemSlotInstall)) {
            // v202 sub_502EA0
            // 不知道是啥
            int[] unkItemBasePos = {20001, 20049};
            int[] unkItemEndPos = {20048, 20051};
            for (int i = 0; i < unkItemBasePos.length; i++) {
                int beginPos = unkItemBasePos[i];
                int endPos = unkItemEndPos[i];
                for (Item item : getEquippedInventory().getItems()) {
                    Equip equip = (Equip) item;
                    if (item.getBagIndex() >= beginPos && item.getBagIndex() < endPos) {
                        outPacket.encodeShort(equip.getBagIndex());
                        equip.encode(outPacket);
                    }
                }
                outPacket.encodeShort(0);
            }
        }

        if (mask.isInMask(DBChar.ItemSlotConsume)) {
            for (Item item : getConsumeInventory().getItems()) {
                outPacket.encodeByte(item.getBagIndex());
                item.encode(outPacket);
            }
            outPacket.encodeByte(0);
        }
        if (mask.isInMask(DBChar.ItemSlotInstall)) {
            for (Item item : getInstallInventory().getItems()) {
                outPacket.encodeByte(item.getBagIndex());
                item.encode(outPacket);
            }
            outPacket.encodeByte(0);
        }
        if (mask.isInMask(DBChar.ItemSlotEtc)) {
            for (Item item : getEtcInventory().getItems()) {
                outPacket.encodeByte(item.getBagIndex());
                item.encode(outPacket);
            }
            outPacket.encodeByte(0);
        }
        if (mask.isInMask(DBChar.ItemSlotCash)) {
            for (Item item : getCashInventory().getItems()) {
                outPacket.encodeByte(item.getBagIndex());
                item.encode(outPacket);
            }
            outPacket.encodeByte(0);
        }

        // BagDatas // extenedSlots
        if (mask.isInMask(DBChar.ItemSlotConsume)) {
            // TODO
            outPacket.encodeInt(0);
        }
        if (mask.isInMask(DBChar.ItemSlotInstall)) {
            // TODO
            outPacket.encodeInt(0);
        }
        if (mask.isInMask(DBChar.ItemSlotEtc)) {
            // TODO
            outPacket.encodeInt(0);
        }
        if (mask.isInMask(DBChar.ItemSlotCash)) {
            // TODO
            outPacket.encodeInt(0);
        }
        // End bagdatas

        if (mask.isInMask(DBChar.CoreAura)) {
            int val = 0;
            outPacket.encodeInt(val);
            for (int i = 0; i < val; i++) {
                outPacket.encodeInt(0);
                outPacket.encodeLong(0);
            }
        }
        if (mask.isInMask(DBChar.Unsure)) {
            int val = 0;
            outPacket.encodeInt(val);
            for (int i = 0; i < val; i++) {
                new FileTime(0).encode(outPacket);
                outPacket.encodeLong(0);
            }
        }
        if (mask.isInMask(DBChar.ItemPot)) {
            boolean hasItemPot = getItemPots() != null;
            outPacket.encodeByte(hasItemPot);
            if (hasItemPot) {
                for (int i = 0; i < getItemPots().size(); i++) {
                    getItemPots().get(i).encode(outPacket);
                    outPacket.encodeByte(i != getItemPots().size() - 1);
                }
            }
        }

        if (mask.isInMask(DBChar.SkillRecord)) {
            boolean encodeSkills = getSkills().size() > 0;
            outPacket.encodeByte(encodeSkills);
            if (encodeSkills) {
                short size = (short) getSkills().size();
                outPacket.encodeShort(size);
                for (Skill skill : getSkills()) {
                    outPacket.encodeInt(skill.getSkillId());
                    outPacket.encodeInt(skill.getCurrentLevel());
                    outPacket.encodeFT(skill.getDateExpire());
                    if (SkillConstants.isSkillNeedMasterLevel(skill.getSkillId())) {
                        outPacket.encodeInt(skill.getMasterLevel());
                    }
                    if (SkillConstants.get紫扇傳授UnknownValue(skill.getId()) > 0) {
                        outPacket.encodeInt(skill.getMasterLevel());
                    }
                }

            } else {
                final Map<Integer, Integer> skillsWithoutMax = new LinkedHashMap<>();
                final Map<Integer, FileTime> skillsWithExpiration = new LinkedHashMap<>();
                final Map<Integer, Integer> skillsWithMax = new LinkedHashMap<>();
                for (Skill skill : getSkills()) {
                    skillsWithoutMax.put(skill.getSkillId(), skill.getCurrentLevel());
                    if (skill.getDateExpire() != null && skill.getDateExpire().getLongValue() !=
                            FileTime.Type.PERMANENT.getVal()) {
                        skillsWithExpiration.put(skill.getSkillId(), skill.getDateExpire());
                    }
                    if (SkillConstants.isSkillNeedMasterLevel(skill.getSkillId())) {
                        skillsWithMax.put(skill.getSkillId(), skill.getMasterLevel());
                    }
                    if (SkillConstants.get紫扇傳授UnknownValue(skill.getSkillId()) > 0) {
                        skillsWithMax.put(skill.getSkillId(), skill.getMasterLevel());
                    }
                    outPacket.encodeShort(skillsWithoutMax.size());
                    skillsWithoutMax.forEach((k, v) -> {
                        outPacket.encodeInt(k);
                        outPacket.encodeInt(v);
                    });
                    int amount = 0;
                    outPacket.encodeShort(amount);
                    for (int i = 0; i < amount; ++i)
                        outPacket.encodeInt(0);
                    outPacket.encodeShort(skillsWithExpiration.size());
                    skillsWithExpiration.forEach((k, v) -> {
                        outPacket.encodeInt(k);
                        outPacket.encodeFT(v);
                    });
                    amount = 0;
                    outPacket.encodeShort(amount);
                    for (int i = 0; i < amount; ++i)
                        outPacket.encodeInt(0);

                    outPacket.encodeInt(skillsWithMax.size());
                    skillsWithMax.forEach((k, v) -> {
                        outPacket.encodeInt(k);
                        outPacket.encodeInt(v);
                    });
                    amount = 0;
                    outPacket.encodeInt(amount);
                    for (int i = 0; i < amount; ++i)
                        outPacket.encodeInt(0);
                }
            }
            short size2 = 0;
            outPacket.encodeShort(size2);
            for (int i = 0; i < size2; i++) {
                outPacket.encodeInt(0); // another nCount
                outPacket.encodeShort(0); // idk
            }
            // 連結技能
            int size3 = 0;
            outPacket.encodeInt(size3);
            for (int i = 0; i < size3; i++) {
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0); // SkillID
                outPacket.encodeShort(0); // sLv
                outPacket.encodeFT(FileTime.getTime()); // dateExpire
            }
        }

        if (mask.isInMask(DBChar.SkillCoolTime)) {
            short size = 0;
            outPacket.encodeShort(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeInt(0); // nSkillId
                outPacket.encodeInt(0); // nSkillCooltime
            }
        }
        if (mask.isInMask(DBChar.QuestRecord)) {
            // modified/deleted, not completed anyway
            boolean removeAllOldEntries = true;
            outPacket.encodeByte(removeAllOldEntries);
            short size = (short) getQuestManager().getQuestsInProgress().size();
            outPacket.encodeShort(size);
            for (Quest quest : getQuestManager().getQuestsInProgress()) {
                outPacket.encodeInt(quest.getQRKey());
                outPacket.encodeString(quest.getQRValue());
            }
            if (!removeAllOldEntries) {
                // blacklisted quests
                short size2 = 0;
                outPacket.encodeShort(size2);
                for (int i = 0; i < size2; i++) {
                    outPacket.encodeInt(0); // nQRKey
                }
            }
            size = 0;
            outPacket.encodeShort(size);
            // Not sure what this is for
            for (int i = 0; i < size; i++) {
                outPacket.encodeString("");
                outPacket.encodeString("");
            }
        }
        if (mask.isInMask(DBChar.QuestComplete)) {
            boolean removeAllOldEntries = true;
            outPacket.encodeByte(removeAllOldEntries);
            Set<Quest> completedQuests = getQuestManager().getCompletedQuests();
            outPacket.encodeShort(completedQuests.size());
            for (Quest quest : completedQuests) {
                outPacket.encodeInt(quest.getQRKey());
                outPacket.encodeInt(0); // Timestamp of completion
            }
            if (!removeAllOldEntries) {
                short size = 0;
                outPacket.encodeShort(size);
                for (int i = 0; i < size; i++) {
                    outPacket.encodeInt(0); // nQRKey?
                }
            }
        }
        if (mask.isInMask(DBChar.MiniGameRecord)) {
            int size = 0;
            outPacket.encodeShort(size);
            for (int i = 0; i < size; i++) {
                new MiniGameRecord().encode(outPacket);
            }
        }
        if (mask.isInMask(DBChar.CoupleRecord)) {
            int coupleSize = 0;
            outPacket.encodeShort(coupleSize);
            for (int i = 0; i < coupleSize; i++) {
                new CoupleRecord().encode(outPacket);
            }
            int friendSize = 0;
            outPacket.encodeShort(friendSize);
            for (int i = 0; i < friendSize; i++) {
                new FriendRecord().encode(outPacket);
            }
            int marriageSize = 0;
            outPacket.encodeShort(marriageSize);
            for (int i = 0; i < marriageSize; i++) {
                new MarriageRecord().encode(outPacket);
            }
        }

        if (mask.isInMask(DBChar.MapTransfer)) {
            for (int i = 0; i < 5; i++) {
                outPacket.encodeInt(0);
            }
            for (int i = 0; i < 10; i++) {
                outPacket.encodeInt(0);
            }
            for (int i = 0; i < 13; i++) {
                outPacket.encodeInt(0);
            }
            for (int i = 0; i < 13; i++) {
                outPacket.encodeInt(0);
            }
        }

        if (mask.isInMask(DBChar.QuestRecordEx)) {
            outPacket.encodeShort(getQuestManager().getEx().size());
            for (Quest quest : getQuestManager().getEx()) {
                outPacket.encodeInt(quest.getQRKey());
                outPacket.encodeString(quest.getQRValue());
            }
        }
        if (mask.isInMask(DBChar.Avatar)) {

            short size = 0;
            outPacket.encodeShort(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeInt(0); // sValue
                new AvatarLook().encode(outPacket);
            }
        }
        if (mask.isInMask(DBChar.Flag80000)) {
            int size = 0;
            outPacket.encodeShort(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeInt(0);
                outPacket.encodeShort(0);
            }
        }
        boolean unk = true;
        outPacket.encodeByte(unk);
        if (unk) {
            if (mask.isInMask(DBChar.Flag8000000000L)) {
                int i6 = 0;
                outPacket.encodeInt(i6);
                for (int i = 0; i < i6; i++) {
                    outPacket.encodeInt(0);
                    outPacket.encodeString("");
                }
            }
        }

        if (mask.isInMask(DBChar.Flag100000000000)) {
            int i2 = 0;
            outPacket.encodeInt(i2);
            for (int i = 0; i < i2; i++) {
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
            }
        }

        if (mask.isInMask(DBChar.WildHunterInfo)) {
            if (MapleJob.is狂豹獵人(getAvatarData().getCharacterStat().getJob())) {
                getWildHunterInfo().encode(outPacket); // GW_WildHunterInfo::Decode
            }
        }
        if (mask.isInMask(DBChar.ZeroInfo)) {
            if (MapleJob.is神之子(getAvatarData().getCharacterStat().getJob())) {
                if (getZeroInfo() == null) {
                    initZeroInfo();
                }
                getZeroInfo().encode(outPacket); //ZeroInfo::Decode
            }
        }
        if (mask.isInMask(DBChar.ShopBuyLimit)) {
            int shopSize = 0;
            outPacket.encodeShort(shopSize);
            for (int i = 0; i < shopSize; i++) {
                short amount = 0;
                outPacket.encodeShort(amount);
                int npcID = 0;
                outPacket.encodeInt(npcID);
                for (int j = 0; j < amount; j++) {
                    // dummyBLD.dwNPCID
                    outPacket.encodeInt(npcID); // NPCID 應該是跟
                    // dummyBLD.nItemIndex
                    outPacket.encodeInt(0); // 商品位置嗎
                    // dummyBLD.nItemID
                    outPacket.encodeInt(0); // 道具ID
                    // dummyBLD.nCount
                    outPacket.encodeInt(0); // 已經購買次數
                    // dummyBLD.ftDate
                    outPacket.encodeLong(0); // 時間吧
                }
            }
        }
        if (mask.isInMask(DBChar.StolenSkills)) {
            if (MapleJob.is幻影俠盜(getAvatarData().getCharacterStat().getJob())) {
                for (Skill skill : getStolenSkills()) {
                    outPacket.encodeInt(skill.getSkillId());
                }
            } else {
                outPacket.encodeZeroBytes(60);
            }
        }
        if (mask.isInMask(DBChar.ChosenSkills)) {
            if (MapleJob.is幻影俠盜(getAvatarData().getCharacterStat().getJob())) {
                for (Skill skill : getChosenSkills()) {
                    outPacket.encodeInt(skill.getSkillId());
                }
            } else {
                outPacket.encodeZeroBytes(20);
            }
        }
        if (mask.isInMask(DBChar.CharacterPotentialSkill)) {
            short size = 0;
            outPacket.encodeShort(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeByte(0); // nKey
                outPacket.encodeInt(0); // nSkillID
                outPacket.encodeByte(0); // nSLV
                outPacket.encodeByte(0); // nGrade
            }
        }
        if (mask.isInMask(DBChar.SoulCollection)) {
            short size = 0;
            outPacket.encodeShort(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeInt(0); //
                outPacket.encodeInt(0); //
            }
        }
        //內在能力聲望訊息
        if (mask.isInMask(DBChar.HonorInfo)) {
            outPacket.encodeInt(0); // honor level
            outPacket.encodeInt(0); // honor exp
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }

        if (mask.isInMask(DBChar.Flag200000000000)) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);

            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);

            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);

            outPacket.encodeLong(0);
            outPacket.encodeByte(MapleJob.is蒼龍俠客(getJob()) && MapleJob.is幻獸師(getJob()));
        }

        if(mask.isInMask(DBChar.OXSystem)) {
            int size = 0;
            outPacket.encodeShort(size);
            for(int i = 0 ; i < size;i++) {
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeString("");
                outPacket.encodeByte(0);
                outPacket.encodeLong(0);
                outPacket.encodeInt(0);
                outPacket.encodeString("");
                outPacket.encodeByte(0);
                outPacket.encodeByte(0);
                outPacket.encodeLong(0);
                outPacket.encodeString("");
            }
        }

        if(mask.isInMask(DBChar.ExpChairInfo)) {
            int size = 0;
            outPacket.encodeInt(size);
            for(int i = 0 ; i < size;i++) {
                outPacket.encodeInt(0);
                outPacket.encodeByte(0);
                outPacket.encodeShort(0);
                outPacket.encodeByte(0);
                outPacket.encodeInt(0); // itemid
                outPacket.encodeLong(0);
            }

        }

        if(mask.isInMask(DBChar.RedLeafInfo)) {
            int idarr[] = new int[]{9410165, 9410166, 9410167, 9410168, 9410198};
            outPacket.encodeInt(getAccId());
            outPacket.encodeInt(getId());
            int size = 5;
            outPacket.encodeInt(size);
            outPacket.encodeInt(0);
            for(int i = 0 ; i < size;i++) {
                outPacket.encodeInt(idarr[i]);
                outPacket.encodeInt(0); // fiendShipPoints
            }
        }

        if (mask.isInMask(DBChar.Flag2000000000000)) {
            int size = 0 ;
            outPacket.encodeShort(size);
            for(int i = 0 ; i < size;i++) {
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeLong(0);
                outPacket.encodeLong(0);
                outPacket.encodeInt(0);
                int res = 0;
                for(int j = 0 ; j < res; j++) {
                    outPacket.encodeInt(0);
                }
            }
        }

        if(mask.isInMask(DBChar.EntryRecord)) {
            boolean bool = true;
            outPacket.encodeByte(bool);
            if(bool) {
                int v1 = 0 ;
                outPacket.encodeShort(v1);
                for(int i = 0 ; i < v1; i++) {
                    outPacket.encodeShort(v1);
                    int v2 = 0 ;
                    outPacket.encodeShort(v2);
                    for(int j = 0 ; j < v2; j++) {
                        outPacket.encodeInt(0);
                        outPacket.encodeInt(0);
                        //CharacterData::SetEntryRecord
                    }
                }
            } else {
                int v3 = 0;
                outPacket.encodeShort(v3);
                for(int i = 0 ; i < v3; i++) {
                    outPacket.encodeShort(0);
                    outPacket.encodeInt(0);
                    outPacket.encodeInt(0);
                    // CharacterData::SetEntryRecord
                    // CharacterData::SetERModified
                }
            }
        }
        if (mask.isInMask(DBChar.ReturnEffectInfo)) {
//            getReturnEffectInfo().encode(outPacket); // ReturnEffectInfo::Decode
            outPacket.encodeByte(0);
        }

        //  天使破壞者
        if (mask.isInMask(DBChar.DressUpInfo)) {
            new DressUpInfo().encode(outPacket); // GW_DressUpInfo::Decode
        }


        if(mask.isInMask(DBChar.Flag20000000000000L)) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeFT(new FileTime(-2));
        }

        // 進化系統
        if (mask.isInMask(DBChar.CoreInfo)) {
            // GW_Core
            short size = 0;
            outPacket.encodeShort(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeShort(0); // nPos
                outPacket.encodeInt(0); // nCoreID
                outPacket.encodeInt(0); // nLeftCount
            }

            size = 0;
            outPacket.encodeShort(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeShort(0); // nPos
                outPacket.encodeInt(0); // nCoreID
                outPacket.encodeInt(0); // nLeftCount
            }
        }

        if (mask.isInMask(DBChar.LikePoint)) {
            new LikePoint().encode(outPacket);
        }
        if (mask.isInMask(DBChar.RunnerGameRecord)) {
            new RunnerGameRecord().encode(outPacket);
        }

        if (mask.isInMask(0x800000000000000L)) {
            // [4:size] ([4][1][1][1]) * size) [4] [8]
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeLong(0);
        }


        outPacket.encodeShort(0); // 先略過

        if(mask.isInMask(0x4000000000000L)) {
            outPacket.encodeShort(0); // TODO:
        }

        outPacket.encodeInt(0); // loop for [4][str]


        if(mask.isInMask(0x1000000000000000L)) {
            outPacket.encodeShort(0); // loop for [4][4]
        }

        if(mask.isInMask(DBChar.VMatrixInfo)) {
            int vmatrix_size = 0;
            outPacket.encodeInt(vmatrix_size);
            // getVMatrixRecords().encode(outPacket)
        }

        if(mask.isInMask(0x4000000000000000L)) {
            outPacket.encodeInt(0);
        }

        // 跟克梅勒茲航海有關
        if(mask.isInMask(0x4000000000000L)) {
            outPacket.encodeByte(false);
            outPacket.encodeShort(0);
            outPacket.encodeShort(0);
        }

        if(mask.isInMask(0x8000000000000L)) {
            outPacket.encodeByte(0);
        }

        if(mask.isInMask(0x10000000000000L)) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }

        // TODO: 這個包有點醜 // sub_538480 [1] [4] * ??????
        outPacket.encodeByte(false);


    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        if(getGuild() != null) {
            Guild g = getGuild();
            GuildMember gm = g.getMemberByID(getId());
            gm.setOnline(online);
            gm.setCharacter(online ? this : null);
            getGuild().broadcast(new GuildResult(
                    new GuildUpdateMemberLogin(g.getId(), getId(), online, !this.online && online)), this);
        }
        this.online = online;
    }

    private String getBlessingOfFairy() {
        //TODO
        return null;
    }

    private String getBlessingOfEmpress() {
        // TODO
        return null;
    }

    public void logout() {
        setOnline(false);
        getField().removeCharacter(this);
        DatabaseFactory.getInstance().saveToDB(this);
        getClient().getChannelInstance().removeCharacter(this);
    }
}




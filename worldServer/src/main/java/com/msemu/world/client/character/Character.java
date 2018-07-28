package com.msemu.world.client.character;

import com.msemu.commons.data.enums.InvType;
import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.data.templates.field.FieldTemplate;
import com.msemu.commons.data.templates.field.Portal;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.database.Schema;
import com.msemu.commons.enums.FileTimeUnit;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.mob.LP_MobChangeController;
import com.msemu.core.network.packets.outpacket.stage.LP_SetField;
import com.msemu.core.network.packets.outpacket.user.LP_UserEnterField;
import com.msemu.core.network.packets.outpacket.user.LP_UserLeaveField;
import com.msemu.core.network.packets.outpacket.user.local.LP_ChatMsg;
import com.msemu.core.network.packets.outpacket.user.local.LP_UserChat;
import com.msemu.core.network.packets.outpacket.user.remote.LP_UserAvatarModified;
import com.msemu.core.network.packets.outpacket.user.remote.effect.LP_UserEffectRemote;
import com.msemu.core.network.packets.outpacket.wvscontext.*;
import com.msemu.world.Channel;
import com.msemu.world.client.character.effect.LevelUpUserEffect;
import com.msemu.world.client.character.effect.MultiKillMessage;
import com.msemu.world.client.character.friends.FriendList;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.jobs.JobHandler;
import com.msemu.world.client.character.jobs.JobManager;
import com.msemu.world.client.character.messages.ComboKillMessage;
import com.msemu.world.client.character.messages.IncExpMessage;
import com.msemu.world.client.character.messages.IncMoneyMessage;
import com.msemu.world.client.character.messages.MoneyDropPickUpMessage;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.character.quest.Quest;
import com.msemu.world.client.character.quest.QuestManager;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.client.character.skill.SkillMacro;
import com.msemu.world.client.character.skill.vcore.VMatrixRecord;
import com.msemu.world.client.character.stats.CharacterLocalStat;
import com.msemu.world.client.character.stats.CharacterStat;
import com.msemu.world.client.character.stats.ForcedStatManager;
import com.msemu.world.client.character.stats.TemporaryStatManager;
import com.msemu.world.client.field.AbstractFieldObject;
import com.msemu.world.client.field.AffectedArea;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.Life;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.client.field.lifes.Pet;
import com.msemu.world.client.guild.Guild;
import com.msemu.world.client.guild.GuildMember;
import com.msemu.world.client.guild.operations.LoadGuildDoneResponse;
import com.msemu.world.client.guild.operations.NotifyLoginOrLogoutResponse;
import com.msemu.world.client.scripting.ScriptManager;
import com.msemu.world.constants.*;
import com.msemu.world.data.FieldData;
import com.msemu.world.data.ItemData;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.*;
import com.msemu.world.service.GuildService;
import com.msemu.world.service.PartyService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

import static com.msemu.commons.data.enums.InvType.EQUIP;
import static com.msemu.commons.data.enums.InvType.EQUIPPED;
import static com.msemu.world.enums.InventoryOperationType.*;

/**
 * Created by Weber on 2018/3/29.
 */
@Schema
@Entity
@Table(name = "characters")
@Getter
@Setter
public class Character extends Life {

    @Transient
    private transient final Set<Mob> controlledMobs = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "accId")
    private int accId;
    @JoinColumn(name = "questManager")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter(value = AccessLevel.NONE)
    private QuestManager questManager;
    @JoinColumn(name = "equippedInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Inventory equippedInventory = new Inventory(EQUIPPED, 50);
    @JoinColumn(name = "equipInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Inventory equipInventory = new Inventory(EQUIP, 50);
    @JoinColumn(name = "consumeInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Inventory consumeInventory = new Inventory(InvType.CONSUME, 50);
    @JoinColumn(name = "etcInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Inventory etcInventory = new Inventory(InvType.ETC, 50);
    @JoinColumn(name = "installInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Inventory installInventory = new Inventory(InvType.INSTALL, 50);
    @JoinColumn(name = "cashInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Inventory cashInventory = new Inventory(InvType.CASH, 50);
    @JoinColumn(name = "avatarData")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private AvatarData avatarData;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private FuncKeyMap funcKeyMap;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "charId")
    private List<Skill> skills;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "charId")
    private List<VMatrixRecord> vMatrixRecords;
    @JoinColumn(name = "guild")
    @OneToOne(cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Guild guild;
    @Transient
    private GameClient client;
    @Transient
    private Ranking ranking;
    @Transient
    private int combatOrders;
    @Transient
    private List<Skill> stolenSkills;
    @Transient
    private List<Skill> chosenSkills;
    @Transient
    private List<ItemPot> itemPots;
    @Transient
    private List<Pet> pets;
    @Transient
    private List<FriendRecord> friendRecords;
    @Transient
    private List<ExpConsumeItem> expConsumeItems;
    @Transient
    private List<MonsterBattleMobInfo> monsterBattleMobInfos;
    @Transient
    private MonsterBattleLadder monsterBattleLadder;
    @Transient
    private MonsterBattleRankInfo monsterBattleRankInfo;
    @Transient
    private Field field;
    @Transient
    @Setter(AccessLevel.NONE)
    private TemporaryStatManager temporaryStatManager;
    @Transient
    @Setter(AccessLevel.NONE)
    private ForcedStatManager forcedStatManager;
    @Transient
    private JobHandler jobHandler;
    @Transient
    private MarriageRing marriageRecord;
    @Transient
    private WildHunterInfo wildHunterInfo;
    @Transient
    private ZeroInfo zeroInfo;
    @Transient
    private int damageSkin;
    @Transient
    private int premiumDamageSkin;
    @Transient
    private boolean partyInvitable;
    @Transient
    private ScriptManager scriptManager = new ScriptManager(this);
    @Transient
    private int driverID;
    @Transient
    private int passengerID;
    @Transient
    private int chocoCount;
    @Transient
    private int activeEffectItemID;
    @Transient
    private int monkeyEffectItemID;
    @Transient
    private int completedSetItemID;
    @Transient
    private short fieldSeatID = -1;
    @Transient
    private int portableChairID;
    @Transient
    private String portableChairMsg;
    @Transient
    private short foothold;
    @Transient
    private int tamingMobLevel = 1;
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
    private List<FriendshipRingRecord> friendshipRingRecord = new ArrayList<>();
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
    private FreezeHotEventInfo freezeHotEventInfo = new FreezeHotEventInfo();
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
    @Setter(AccessLevel.NONE)
    private boolean online;
    @Transient
    private Party party;
    @Transient
    private FieldInstanceType fieldInstanceType;
    @Transient
    private final Map<Integer, Field> fields = new HashMap<>();
    @Transient
    @Setter(AccessLevel.NONE)
    private int bulletIDForAttack;
    @Transient
    private final CharacterLocalStat characterLocalStat;
    @Transient
    private final FriendList friendList = new FriendList();
    @Transient
    private final ReentrantReadWriteLock controlledLock = new ReentrantReadWriteLock();
    @Getter
    @Transient
    private final Set<AbstractFieldObject> visibleFieldObjects = new HashSet<>();
    @Transient
    private final ReentrantReadWriteLock visibleMapObjectsLock = new ReentrantReadWriteLock();
    @Transient
    LocalDateTime lastUseStatChangeItemTime = LocalDateTime.MIN;
    @Transient
    private Android activeAndroid;
    @Transient
    private int team;
    @Transient
    @Setter(AccessLevel.NONE)
    private List<SkillMacro> skillMacros = new ArrayList<>();
    @Transient
    private int comboKill;

    // timestamps ??

    public Character() {
        avatarData = new AvatarData();
        avatarData.setAvatarLook(new AvatarLook());
        stolenSkills = new ArrayList<>();
        chosenSkills = new ArrayList<>();
        questManager = new QuestManager(this);
        itemPots = new ArrayList<>();
        friendRecords = new ArrayList<>();
        expConsumeItems = new ArrayList<>();
        skills = new ArrayList<>();
        temporaryStatManager = new TemporaryStatManager(this);
        forcedStatManager = new ForcedStatManager(this);
        characterLocalStat = new CharacterLocalStat(this);
        pets = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            pets.add(new Pet(-1));
        }
        setFieldInstanceType(FieldInstanceType.CHANNEL);
    }

    public static Character findById(int id) {
        Session session = DatabaseFactory.getInstance().getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Character> query = builder.createQuery(Character.class);
        Root<Character> root = query.from(Character.class);
        query.select(root).where(builder.equal(root.get("id"), id));
        Character result = session.createQuery(query).getSingleResult();
        session.close();
        return result;
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

    public void setJob(int jobId) {
        MapleJob job = MapleJob.getById(jobId);
        if (job == null)
            return;
        if (MapleJob.is影武者(jobId)) {
            getAvatarData().getCharacterStat().setSubJob(1);
        } else if (MapleJob.is重砲指揮官(jobId)) {
            getAvatarData().getCharacterStat().setSubJob(2);
        } else if (MapleJob.盜賊.getId() != jobId) {
            getAvatarData().getCharacterStat().setSubJob(0);
        }
        final int oldJob = getAvatarData().getCharacterStat().getJob();

        if (oldJob != jobId) {
            setStat(Stat.JOB, jobId);
            getAvatarData().getCharacterStat().setJob(jobId);
        }

        setJobHandler(JobManager.getJobHandler((short) job.getId(), this));

        // 學習該職業的技能
        List<Skill> skills = SkillData.getInstance().getSkillsByJob((short) job.getId());
        skills.stream().filter(jobSkill -> !hasSkill(jobSkill.getSkillId()))
                .forEach(this::addSkill);

        // 升級技能
        Skill beginnerSkill = SkillData.getInstance().getSkillById(80001770);
        beginnerSkill.setCurrentLevel(1);
        addSkill(beginnerSkill);


        getClient().write(new LP_ChangeSkillRecordResult(skills, true,
                false, false, false));
        notifyChanges();
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

    public void write(OutPacket<GameClient> outPacket) {
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
        if (field != null)
            setFieldID(field.getFieldId());
    }

    public int getFieldID() {
        return (int) getAvatarData().getCharacterStat().getPosMap();
    }

    private void setFieldID(int fieldID) {
        getAvatarData().getCharacterStat().setPosMap(fieldID);
    }

    private void setFieldID(long fieldId) {
        setFieldID((int) fieldId);
    }

    public QuestManager getQuestManager() {
        if (questManager.getCharacter() == null)
            questManager.setCharacter(this);
        return questManager;
    }

    public long getExp() {
        CharacterStat cs = getAvatarData().getCharacterStat();
        return cs.getExp();
    }

    public void setExp(long amount) {
        CharacterStat cs = getAvatarData().getCharacterStat();
        cs.setExp(amount);
        Map<Stat, Object> stats = new HashMap<>();
        stats.put(Stat.EXP, amount);
        getClient().write(new LP_StatChanged(stats));
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
        if (level >= GameConstants.CHAR_EXP_TABLE.length - 1) {
            return;
        }
        long newExp = curExp + amount;
        Map<Stat, Object> stats = new HashMap<>();
        if (level == 250)
            newExp = 0;
        while (newExp >= GameConstants.CHAR_EXP_TABLE[level]) {
            newExp -= GameConstants.CHAR_EXP_TABLE[level];
            addStat(Stat.LEVEL, 1);
            getJobHandler().handleLevelUp();
            level++;
            getField().broadcastPacket(new LP_UserEffectRemote(this, new LevelUpUserEffect()), this);
        }
        cs.setExp(newExp);
        stats.put(Stat.EXP, newExp);
        if (eii != null)
            write(new LP_Message(new IncExpMessage(eii)));
        getClient().write(new LP_StatChanged(stats));
        this.addSp(1, 30);
    }

    public void addStat(Stat charStat, int amount) {
        addStat(Collections.singletonMap(charStat, amount));
    }

    public void addStat(Map<Stat, Integer> stats) {
        Map<Stat, Integer> after = new EnumMap<>(Stat.class);
        for (Map.Entry<Stat, Integer> entry : stats.entrySet()) {
            after.put(entry.getKey(), getStat(entry.getKey()) + entry.getValue());
        }
        setStat(after);
    }

    public int getStat(Stat charStat) {
        CharacterStat cs = getAvatarData().getCharacterStat();
        switch (charStat) {
            case STR:
                return cs.getStr();
            case DEX:
                return cs.getDex();
            case INT:
                return cs.getInte();
            case LUK:
                return cs.getLuk();
            case HP:
                return cs.getHp();
            case MAX_HP:
                return cs.getMaxHp();
            case MP:
                return cs.getMp();
            case MAX_MP:
                return cs.getMaxMp();
            case AP:
                return cs.getAp();
            case LEVEL:
                return cs.getLevel();
        }
        return -1;
    }

    public void setStat(Stat stat, int amount) {
        setStat(Collections.singletonMap(stat, amount));
    }

    public void setStat(Map<Stat, Integer> stats) {
        Map<Stat, Object> changedStats = new EnumMap<>(Stat.class);
        stats.forEach((charStat, amount) -> {
            amount = Math.max(amount, 0);
            switch (charStat) {
                case STR:
                    amount = Math.min(amount, GameConstants.MAX_BASIC_STAT);
                    getAvatarData().getCharacterStat().setStr(amount);
                    break;
                case DEX:
                    amount = Math.min(amount, GameConstants.MAX_BASIC_STAT);
                    getAvatarData().getCharacterStat().setDex(amount);
                    break;
                case INT:
                    amount = Math.min(amount, GameConstants.MAX_BASIC_STAT);
                    getAvatarData().getCharacterStat().setInte(amount);
                    break;
                case LUK:
                    amount = Math.min(amount, GameConstants.MAX_BASIC_STAT);
                    getAvatarData().getCharacterStat().setLuk(amount);
                    break;
                case HP:
                    amount = Math.min(amount, getCurrentMaxHp());
                    getAvatarData().getCharacterStat().setHp(amount);
                    break;
                case MAX_HP:
                    amount = Math.min(amount, GameConstants.MAX_HP);
                    getAvatarData().getCharacterStat().setMaxHp(amount);
                    break;
                case MP:
                    amount = Math.min(amount, getCurrentMaxMp());
                    getAvatarData().getCharacterStat().setMp(amount);
                    break;
                case MAX_MP:
                    amount = Math.min(amount, GameConstants.MAX_HP);
                    getAvatarData().getCharacterStat().setMaxMp(amount);
                    break;
                case AP:
                    getAvatarData().getCharacterStat().setAp(amount);
                    break;
                case LEVEL:
                    amount = Math.min(amount, GameConstants.MAX_LEVEL);
                    getAvatarData().getCharacterStat().setLevel(amount);
                    break;
            }
            changedStats.put(charStat, amount);
        });
        notifyChanges();
        write(new LP_StatChanged(changedStats));
    }

    public void setJob(MapleJob job) {
        setJob(job.getId());
    }

    private void notifyChanges() {
        if (getParty() != null) {
            getParty().updateFull();
        }
        if (getGuild() != null) {
            // TODO
        }
        renewCharacterStats();
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

    public void renewAvatarLook() {
        byte mask = AvatarModifiedMask.AvatarLook.getVal();
        getField().broadcastPacket(new LP_UserAvatarModified(this, mask, false));
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
     * Consumes a single {@link Item} from this Char's {@link Inventory}. Will unregisterParty the Item if it has a quantity of 1.
     *
     * @param item The Item to consume, which is currently in the Char's inventory.
     */
    public void consumeItem(Item item) {
        Inventory inventory = getInventoryByType(item.getInvType());
        // data race possible
        if (item.getQuantity() <= 1 && !ItemConstants.isThrowingItem(item.getItemId())) {
            item.setQuantity(0);
            inventory.removeItem(item);
            write(new LP_InventoryOperation(true, false,
                    REMOVE, item, item.getBagIndex()));
        } else {
            item.setQuantity(item.getQuantity() - 1);
            write(new LP_InventoryOperation(true, false,
                    UPDATE, item, item.getBagIndex()));
        }
        this.renewBulletIDForAttack();
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
        Item checkItem = ItemData.getInstance().createItem(id);
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

    public int getCurrentMaxHp() {
        return getCharacterLocalStat().getMaxHp();
    }

    public int getCurrentMaxMp() {
        return getCharacterLocalStat().getMaxMp();
    }

    public void addMoney(long amount) {
        addMoney(amount, true);
    }

    public void addMoney(long amount, boolean showInChat) {
        CharacterStat cs = getAvatarData().getCharacterStat();
        long money = cs.getMoney();
        long newMoney = money + amount;
        if (newMoney >= 0) {
            newMoney = Math.min(GameConstants.MAX_MONEY, newMoney);
            Map<Stat, Object> stats = new HashMap<>();
            cs.setMoney(newMoney);
            stats.put(Stat.MONEY, newMoney);
            write(new LP_StatChanged(stats));
        }
        if (showInChat) {
            write(new LP_Message(new IncMoneyMessage(amount)));
        } else {
            write(new LP_Message(new MoneyDropPickUpMessage(amount)));
        }
    }

    public void renewBulletIDForAttack() {
        this.bulletIDForAttack = calculateBulletIDForAttack();
    }

    public int calculateBulletIDForAttack() {
        Item weapon = getEquippedInventory().getFirstItemByBodyPart(BodyPart.WEAPON);
        if (weapon == null) {
            return 0;
        }
        Predicate<Item> p;
        int weaponItemId = weapon.getItemId();

        if (ItemConstants.類型.拳套(weaponItemId)) {
            p = i -> ItemConstants.isThrowingStar(i.getItemId());
        } else if (ItemConstants.類型.弓(weaponItemId)) {
            p = i -> ItemConstants.類型.弓箭(i.getItemId());
        } else if (ItemConstants.類型.弩(weaponItemId)) {
            p = i -> ItemConstants.類型.弩箭(i.getItemId());
        } else if (ItemConstants.類型.槍(weaponItemId)) {
            p = i -> ItemConstants.類型.子彈(i.getItemId());
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
            Item existingItem = inventory.getItems().stream()
                    .filter(_item -> _item.getDateExpire().equal(item.getDateExpire()) &&
                            _item.getItemId() == item.getItemId()).findFirst().orElse(null);
            if (existingItem != null && existingItem.getInvType().isStackable()) {
                existingItem.addQuantity(item.getQuantity());
                write(new LP_InventoryOperation(true, false,
                        UPDATE, existingItem, (short) existingItem.getBagIndex()));
            } else {
                item.setInventoryId(inventory.getId());
                if (!hasCorrectBagIndex) {
                    item.setBagIndex(inventory.getFirstOpenSlot());
                }
                inventory.addItem(item);
                if (item.getId() == 0) {
                    DatabaseFactory.getInstance().saveToDB(item);
                }
                write(new LP_InventoryOperation(true, false,
                        ADD, item, item.getBagIndex()));
            }
            this.renewBulletIDForAttack();
        }
    }

    public void addItemToInventory(Item item) {
        addItemToInventory(item.getInvType(), item, false);
    }

    /**
     * Sends a message to this Char with a default colour {@link ChatMsgType#MOB}.
     *
     * @param msg The message to display.
     */
    public void chatMessage(String msg) {
        chatMessage(ChatMsgType.NOTICE, msg);
    }

    /**
     * Sends a message to this Char with a given {@link ChatMsgType colour}.
     *
     * @param clr The Colour this message should be in.
     * @param msg The message to display.
     */
    public void chatMessage(ChatMsgType clr, String msg) {
        getClient().write(new LP_ChatMsg(clr, msg));
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
        write(new LP_ChangeSkillRecordResult(getSkills(), true,
                false, false, false));
    }

    /**
     * Adds a skill to this Char. If the Char already has this skill, just changes the levels.
     *
     * @param skillID      the skill's id to add
     * @param currentLevel the current level of the skill
     * @param masterLevel  the master level of the skill
     */
    public void addSkill(int skillID, int currentLevel, int masterLevel) {
        Skill skill = SkillData.getInstance().getSkillById(skillID);
        if (skill == null) {
            LoggerFactory.getLogger(Character.class).error("No such skill found.");
            return;
        }
        skill.setCurrentLevel(currentLevel);
        skill.setMasterLevel(masterLevel);
        addSkill(skill);
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
        Skill skill = SkillData.getInstance().getSkillById(id);
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
     * Warps this Char to a given {@link Field}, with the field's "sp" portal as spawn position.
     *
     * @param toField       The field to warp to.
     * @param characterData Whether or not the character data should be encoded.
     */
    public void warp(Field toField, boolean characterData) {
        warp(toField, toField.getPortalByName("sp"), characterData);
    }

    /**
     * Warps this Char to a given {@link Field} and {@link Portal}. Will not include character data.
     *
     * @param toField  The field to warp to.
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
            getField().leave(this);
        }
        removeAllVisibleObjects();
        removeControlledMobs();
        write(new LP_SetField(this, toField, client.getChannel(), false, 0, characterData, hasBuffProtector(),
                portal.getId(), false, 100, null, true, -1));
        toField.enter(this, portal, true);
        if (characterData) {
            if (getGuild() != null) {
                write(new LP_GuildResult(new LoadGuildDoneResponse(getGuild())));
            }
        }
        renewCharacterStats();
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

    public void encode(OutPacket<GameClient> outPacket, DBChar mask) {
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

        if (mask.isInMask(DBChar.CHARACTER)) {
            getAvatarData().getCharacterStat().encode(outPacket);
            outPacket.encodeByte(getFriendRecords().size());
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
            outPacket.encodeFT(FileTime.now());
            outPacket.encodeFT(FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME));
            LP_UserEnterField.unkFunction(outPacket);
        }
        if (mask.isInMask(DBChar.MONEY)) {
            outPacket.encodeLong(getMoney());
            outPacket.encodeInt(getId());
            // TODO 楓葉點數、小鋼珠
            int maplepoints = 0;
            int pachingoPoints = 0;
            outPacket.encodeInt(maplepoints);
            outPacket.encodeInt(pachingoPoints);
        }
        if (mask.isInMask(DBChar.ITEM_SLOT_CONSUME) || mask.isInMask(DBChar.ExpConsumeItem)) {
            outPacket.encodeInt(getExpConsumeItems().size());
            for (ExpConsumeItem eci : getExpConsumeItems()) {
                eci.encode(outPacket);
            }
        }

        if (mask.isInMask(DBChar.INVENTORY_SIZE)) {
            outPacket.encodeByte(getEquipInventory().getSlots());
            outPacket.encodeByte(getConsumeInventory().getSlots());
            outPacket.encodeByte(getEtcInventory().getSlots());
            outPacket.encodeByte(getInstallInventory().getSlots());
            outPacket.encodeByte(getCashInventory().getSlots());
        }

        if (mask.isInMask(DBChar.AdminShopCount)) {
            // ???
            outPacket.encodeFT(FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME));
        }
        if (mask.isInMask(DBChar.ITEM_SLOT_EQUIP)) {
            boolean v248 = false;
            outPacket.encodeByte(v248); // ?
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
                if (item.getBagIndex() > 100 && item.getBagIndex() < 900) {
                    outPacket.encodeShort(equip.getBagIndex());
                    equip.encode(outPacket);
                }
            }
            outPacket.encodeShort(0);
            if (!v248) {
                for (Item item : getEquipInventory().getItems()) {
                    Equip equip = (Equip) item;
                    outPacket.encodeShort(equip.getBagIndex());
                    equip.encode(outPacket);
                }
                outPacket.encodeShort(0);
            }
            // NonBPEquip::Decode
            int[] NonBPEquipBasePos = {1000, 1100, 1200, 1300, 1400, 1500, 5100, 1600, 5200, 5000, 6000};
            int[] NonBPEquipEndPos = {1004, 1105, 1207, 1305, 1425, 1512, 5106, 1606, 5201, 5003, 6025};
            for (int i = 0; i < 11; i++) {
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

            int[] virtualEqpRangeBase = new int[]{20000, 21000};
            int[] virtualEqpRangeEnd = new int[]{20024, 21024};
            for (int i = 0; i < 2; i++) {
                int beginPos = virtualEqpRangeBase[i];
                int endPos = virtualEqpRangeEnd[i];
                for (Item item : getEquippedInventory().getItems()) {
                    Equip equip = (Equip) item;
                    if (item.getBagIndex() >= beginPos && item.getBagIndex() < endPos) {
                        outPacket.encodeShort(equip.getBagIndex());
                        equip.encode(outPacket);
                    }
                }
                outPacket.encodeShort(0);
            }

            /////
            // 201 +
            int v151 = 0;
            outPacket.encodeInt(v151);
        }
        if (mask.isInMask(DBChar.ITEM_SLOT_INSTALL)) {
            // v202 sub_502EA0
            // 不知道是啥
            int[] unkItemBasePos = {20001, 20049};
            int[] unkItemEndPos = {20048, 20051};
            for (int i = 0; i < 2; i++) {
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

        if (mask.isInMask(DBChar.ITEM_SLOT_CONSUME)) {
            for (Item item : getConsumeInventory().getItems()) {
                outPacket.encodeByte(item.getBagIndex());
                item.encode(outPacket);
            }
            outPacket.encodeByte(0);
        }
        if (mask.isInMask(DBChar.ITEM_SLOT_INSTALL)) {
            for (Item item : getInstallInventory().getItems()) {
                outPacket.encodeByte(item.getBagIndex());
                item.encode(outPacket);
            }
            outPacket.encodeByte(0);
        }
        if (mask.isInMask(DBChar.ITEM_SLOT_ETC)) {
            for (Item item : getEtcInventory().getItems()) {
                outPacket.encodeByte(item.getBagIndex());
                item.encode(outPacket);
            }
            outPacket.encodeByte(0);
        }
        if (mask.isInMask(DBChar.ITEM_SLOT_CASH)) {
            for (Item item : getCashInventory().getItems()) {
                outPacket.encodeByte(item.getBagIndex());
                item.encode(outPacket);
            }
            outPacket.encodeByte(0);
        }

        // BagDatas // extenedSlots
        if (mask.isInMask(DBChar.ITEM_SLOT_CONSUME)) {
            // TODO
            outPacket.encodeInt(0);
        }
        if (mask.isInMask(DBChar.ITEM_SLOT_INSTALL)) {
            // TODO
            outPacket.encodeInt(0);
        }
        if (mask.isInMask(DBChar.ITEM_SLOT_ETC)) {
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
            boolean hasItemPot = getItemPots() != null && getItemPots().size() > 0;
            outPacket.encodeByte(hasItemPot);
            if (hasItemPot) {
                for (int i = 0; i < getItemPots().size(); i++) {
                    getItemPots().get(i).encode(outPacket);
                    outPacket.encodeByte(i != getItemPots().size() - 1);
                }
            }
        }

        if (mask.isInMask(DBChar.SKILL_RECORD)) {
            boolean encodeSkills = true;
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
                outPacket.encodeFT(FileTime.now()); // dateExpire
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
        if (mask.isInMask(DBChar.QUEST_RECORD)) {
            // modified/deleted, not completed anyway
            boolean removeAllOldEntries = true;
            outPacket.encodeByte(removeAllOldEntries);
            short size = (short) getQuestManager().getQuestsInProgress().size();
            outPacket.encodeShort(size);
            for (Quest quest : getQuestManager().getQuestsInProgress()) {
                outPacket.encodeInt(quest.getQRKey());
                outPacket.encodeString(quest.getQrValue());
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
        if (mask.isInMask(DBChar.QUEST_COMPLETE)) {
            boolean removeAllOldEntries = true;
            outPacket.encodeByte(removeAllOldEntries);
            Set<Quest> completedQuests = getQuestManager().getCompletedQuests();
            outPacket.encodeShort(completedQuests.size());
            for (Quest quest : completedQuests) {
                outPacket.encodeInt(quest.getQRKey());
                quest.getCompletedTime().encode(outPacket);
            }
            if (!removeAllOldEntries) {
                short size = 0;
                outPacket.encodeShort(size);
                for (int i = 0; i < size; i++) {
                    outPacket.encodeInt(0); // nQRKey?
                }
            }
        }
        if (mask.isInMask(DBChar.MINI_GAME_RECORD)) {
            int size = 0;
            outPacket.encodeShort(size);
            for (int i = 0; i < size; i++) {
                new MiniGameRecord().encode(outPacket);
            }
        }
        if (mask.isInMask(DBChar.COUPLE_RECORD)) {
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
                new MarriageRing().encode(outPacket);
            }
        }

        if (mask.isInMask(DBChar.MAP_TRANSFER)) {
            for (int i = 0; i < 5; i++) {
                outPacket.encodeInt(999999999);
            }
            for (int i = 0; i < 10; i++) {
                outPacket.encodeInt(999999999);
            }
            for (int i = 0; i < 13; i++) {
                outPacket.encodeInt(999999999);
            }
        }

        if (mask.isInMask(DBChar.QuestRecordEx)) {
            outPacket.encodeShort(getQuestManager().getQuests().size());
            for (Quest quest : getQuestManager().getQuests()) {
                outPacket.encodeInt(quest.getQRKey());
                outPacket.encodeString(quest.getQrValue());
            }
        }
        if (mask.isInMask(DBChar.Avatar)) {

            short size = 0;
            outPacket.encodeShort(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeInt(0); // sValue
                getAvatarData().getAvatarLook().encode(outPacket);
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
            outPacket.encodeInt(0); // honor maxLevel
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

            outPacket.encodeLong(FileTime.now().getLongValue() + 86400000L);
            outPacket.encodeByte(MapleJob.is蒼龍俠客(getJob()) && MapleJob.is幻獸師(getJob()));

            outPacket.encodeByte(1);
        }

        if (mask.isInMask(DBChar.OXSystem)) {
            int size = 0;
            outPacket.encodeShort(size);
            for (int i = 0; i < size; i++) {
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

        if (mask.isInMask(DBChar.ExpChairInfo)) {
            int size = 0;
            outPacket.encodeShort(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeInt(0);
                outPacket.encodeByte(0);
                outPacket.encodeShort(0);
                outPacket.encodeByte(0);
                outPacket.encodeInt(0); // itemid
                outPacket.encodeLong(0);
            }

        }

        if (mask.isInMask(DBChar.RedLeafInfo)) {
            int idarr[] = new int[]{9410165, 9410166, 9410167, 9410168, 9410198};
            outPacket.encodeInt(getAccId());
            outPacket.encodeInt(getId());
            int size = 5;
            outPacket.encodeInt(size);
            outPacket.encodeInt(0);
            for (int i = 0; i < size; i++) {
                outPacket.encodeInt(idarr[i]);
                outPacket.encodeInt(0); // fiendShipPoints
            }
        }

        if (mask.isInMask(DBChar.Flag2000000000000)) {
            int size = 0;
            outPacket.encodeShort(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeLong(0);
                outPacket.encodeLong(0);
                outPacket.encodeInt(0);
                int res = 0;
                for (int j = 0; j < res; j++) {
                    outPacket.encodeInt(0);
                }
            }
        }

        if (mask.isInMask(DBChar.EntryRecord)) {
            boolean bool = true;
            outPacket.encodeByte(bool);
            if (bool) {
                int v1 = 0;
                outPacket.encodeShort(v1);
                for (int i = 0; i < v1; i++) {
                    outPacket.encodeShort(v1);
                    int v2 = 0;
                    outPacket.encodeShort(v2);
                    for (int j = 0; j < v2; j++) {
                        outPacket.encodeInt(0);
                        outPacket.encodeInt(0);
                        //CharacterData::SetEntryRecord
                    }
                }
            } else {
                int v3 = 0;
                outPacket.encodeShort(v3);
                for (int i = 0; i < v3; i++) {
                    outPacket.encodeShort(0);
                    outPacket.encodeInt(0);
                    outPacket.encodeInt(0);
                    // CharacterData::SetEntryRecord
                    // CharacterData::SetERModified
                }
            }
        }
        if (mask.isInMask(DBChar.ReturnEffectInfo)) {
//            getReturnEffectInfo().encodeForTown(outPacket); // ReturnEffectInfo::Decode
            outPacket.encodeByte(0);
        }

        //  天使破壞者
        if (mask.isInMask(DBChar.DressUpInfo)) {
            new DressUpInfo().encode(outPacket); // GW_DressUpInfo::Decode
        }


        if (mask.isInMask(DBChar.Flag20000000000000L)) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeFT(FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME));
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

        if (mask.isInMask(DBChar.Flag20000000000)) {
            outPacket.encodeByte(false);
        }

        if (mask.isInMask(DBChar.LikePoint)) {
            new LikePoint().encode(outPacket);
        }
        if (mask.isInMask(DBChar.RunnerGameRecord)) {
            // TODO
            RunnerGameRecord runnerGameRecord = new RunnerGameRecord();
            runnerGameRecord.setCharacterID(getId());
            runnerGameRecord.encode(outPacket);
        }

        if (mask.isInMask(0x800000000000000L)) {
            // [4:size] ([4][1][1][1]) * size) [4] [8]
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeLong(0);
        }


        outPacket.encodeShort(0); // 先略過

        if (mask.isInMask(0x4000000000000L)) {
            outPacket.encodeShort(0); // TODO:
        }

        outPacket.encodeInt(0); // loop for [4][str]


        if (mask.isInMask(0x1000000000000000L)) {
            outPacket.encodeShort(0); // loop for [4][4]
        }

        if (mask.isInMask(DBChar.VMatrixInfo)) {
            int vmatrix_size = 0;
            outPacket.encodeInt(vmatrix_size);
            // getVMatrixRecords().encodeForTown(outPacket)
        }

        if (mask.isInMask(0x4000000000000000L)) {
            outPacket.encodeInt(0);
        }

        // 跟克梅勒茲航海有關
        if (mask.isInMask(0x4000000000000L)) {
            boolean v190 = true;
            outPacket.encodeByte(v190);
            if (v190) {
                outPacket.encodeByte(0);
                outPacket.encodeInt(1);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                FileTime.now().encode(outPacket);
            }
            short v192 = 0;
            outPacket.encodeShort(v192);
            for (int i = 0; i < v192; i++) {
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeLong(0);
            }
            short v194 = 0;
            outPacket.encodeShort(v194);
            for (int i = 0; i < v194; i++) {
                outPacket.encodeByte(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
            }
        }

        if (mask.isInMask(0x8000000000000L)) {
            outPacket.encodeByte(0);
        }

        if (mask.isInMask(0x10000000000000L)) {
            outPacket.encodeInt(0);
            // loop [short][short]
            outPacket.encodeInt(0);
            // loop [short][int]
        }

        // TODO: 這個包有點醜 // sub_538480 [1] [4] * ??????
        outPacket.encodeByte(false);


    }

    public void setGuild(Guild guild) {
        // make sure sharing the same instance
        if (guild != null)
            guild = GuildService.getInstance().getGuildById(guild.getId());
        this.guild = guild;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        if (getGuild() != null) {
            Guild g = getGuild();
            GuildMember gm = g.getMemberByID(getId());
            gm.setOnline(online);
            gm.setCharacter(online ? this : null);
            getGuild().broadcast(new LP_GuildResult(
                    new NotifyLoginOrLogoutResponse(g.getId(), getId(), online, !this.online && online)), this);
        }
        if (getParty() != null) {
            getParty().updatePartyMemberInfo(this);
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
        if (getField() != null) {
            FieldTemplate ft = getField().getFieldData();
            getAvatarData().getCharacterStat().setPosMap((ft.getForcedReturn() > 0 && ft.getForcedReturn() != 999999999 ? ft.getForcedReturn() : getFieldID()));
            getField().leave(this);
        }
        if (getParty() != null) {
            getParty().updateFull();
            setParty(null);
        }
        PartyService.getInstance().removeInvitation(this);
        DatabaseFactory.getInstance().saveToDB(this);
        getClient().getChannelInstance().removeCharacter(this);
    }

    public int getTotalChuc() {
        return getInventoryByType(EQUIPPED).getItems().stream().mapToInt(i -> ((Equip) i).getChuc()).sum();
    }

    public boolean hasFriendshipItem() {
        // TODO
        return false;
    }

    public void renewCharacterStats() {
        renewBulletIDForAttack();
        getCharacterLocalStat().reCalculateLocalStat();
    }

    public void enableActions() {
        write(new LP_StatChanged());
    }

    public void dropMessage() {

    }

    public Field getOrCreateFieldByCurrentInstanceType(int fieldID) {
        Field res = null;
        switch (getFieldInstanceType()) {
            case SOLO:
                if (getFields().containsKey(fieldID)) {
                    res = getFields().get(fieldID);
                } else {
                    Field field = FieldData.getInstance().getFieldFromTemplate(fieldID);
                    getFields().put(field.getFieldId(), field);
                    res = field;
                }
                break;
            case PARTY:
                res = getParty() != null ? getParty().getOrCreateFieldById(fieldID) : null;
                break;
            // TODO expedition
            default:
                res = getClient().getChannelInstance().getField(fieldID);
                break;
        }
        return res;
    }

    public void warpPortal(Portal portal) {
        Channel channel = getClient().getChannelInstance();
        if (!portal.getScript().isEmpty()) {
            getScriptManager().startScript(portal.getId(), portal.getScript(), ScriptType.PORTAL);
        } else if (portal.getTargetMapId() != 999999999) {
            Field toField = channel.getField(portal.getTargetMapId());
            if (toField == null)
                enableActions();
            else {
                Portal toPortal = toField.getPortalByName(portal.getTargetPortalName());
                if (toPortal == null)
                    warp(toField);
                else
                    warp(toField, toPortal);
            }
        }
    }

    public boolean canHold(final int itemID, final int quantity) {
        if (ItemConstants.isEquip(itemID)) {
            return getEquipInventory().getSlots() > getEquipInventory().getItems().size();
        } else {
            ItemTemplate template = ItemData.getInstance().getItemInfo(itemID);
            Inventory inv = getInventoryByType(template.getInvType());
            Item existsItem = inv.getItemByItemID(itemID);
            return (existsItem != null && existsItem.getQuantity() + 1 < template.getSlotMax())
                    || inv.getSlots() > inv.getItems().size();
        }
    }


    public void giveItem(int itemID, int quantity, int period) {
        double isEquip = Math.floor((itemID / 1000000));
        Item item = ItemData.getInstance().createItem(itemID);
        if (item == null)
            return;
        if (period > 0)
            item.setDateExpire(FileTime.now().plus(period, FileTimeUnit.DAY));
        if (item.getType() == Item.Type.EQUIP) {  //Equip
            addItemToInventory(item.getInvType(), item, false);
        } else {    //Item
            item.setQuantity(quantity);
            addItemToInventory(item);
        }
    }

    @Override
    public FieldObjectType getFieldObjectType() {
        return FieldObjectType.CHARACTER;
    }

    @Override
    public void enterScreen(GameClient client) {
        client.write(new LP_UserEnterField(this));
        // EffectSwitch
        // dragon
        // haku
        // android
        // familiar
        // summons
        // LP_FllowCharacter
    }

    @Override
    public void outScreen(GameClient client) {
        write(new LP_UserLeaveField(this));
    }

    public Collection<Mob> getControlledMobs() {
        getControlledLock().readLock().lock();
        try {
            return Collections.unmodifiableCollection(controlledMobs);
        } finally {
            getControlledLock().readLock().unlock();
        }
    }

    public void stopControllingMob(Mob mob) {
        getControlledLock().writeLock().lock();
        try {
            if (controlledMobs.contains(mob)) {
                controlledMobs.remove(mob);
            }
        } finally {
            getControlledLock().writeLock().unlock();
        }
    }

    public void removeControlledMobs() {
        getControlledLock().writeLock().lock();
        try {
            controlledMobs.clear();
        } finally {
            getControlledLock().writeLock().unlock();
        }
    }

    public void controlMob(Mob mob, int controllerLevel) {
        mob.setControllerLevel(controllerLevel);
        mob.setController(this);
        getControlledLock().writeLock().lock();
        try {
            controlledMobs.add(mob);
        } finally {
            getControlledLock().writeLock().unlock();
        }
        client.write(new LP_MobChangeController(mob, false, true));
    }

    public boolean isVisibleFieldObject(AbstractFieldObject object) {
        getVisibleMapObjectsLock().readLock().lock();
        try {
            return getVisibleFieldObjects().contains(object);
        } finally {
            getVisibleMapObjectsLock().readLock().unlock();
        }
    }

    public void addVisibleFieldObject(AbstractFieldObject object) {
        getVisibleMapObjectsLock().writeLock().lock();
        try {
            getVisibleFieldObjects().add(object);
        } finally {
            getVisibleMapObjectsLock().writeLock().unlock();
        }
    }

    public void removeVisibleFieldObject(AbstractFieldObject object) {
        getVisibleMapObjectsLock().writeLock().lock();
        try {
            getVisibleFieldObjects().remove(object);
        } finally {
            getVisibleMapObjectsLock().writeLock().unlock();
        }
    }

    public void removeAllVisibleObjects() {
        getVisibleMapObjectsLock().writeLock().lock();
        try {
            getVisibleFieldObjects().clear();
        } finally {
            getVisibleMapObjectsLock().writeLock().unlock();
        }
    }

    public boolean isAlive() {
        return getStat(Stat.HP) > 0;
    }


    public void addSp(int amount) {
        addSp(SkillConstants.getSkillJobLevel(getJob()), amount);
    }

    public void addSp(int jobLevel, int amount) {
        final CharacterStat stats = getAvatarData().getCharacterStat();
        if (JobConstants.isSeparatedSp(getJob())) {
            int sp = stats.getExtendSP().getSpByJobLevel((byte) jobLevel) + amount;
            sp = Math.min(GameConstants.MAX_BASIC_STAT, sp);
            stats.getExtendSP().setSpToJobLevel(jobLevel, sp);
            Map<Stat, Object> newStats = new EnumMap<>(Stat.class);
            newStats.put(Stat.SP, stats.getExtendSP());
            write(new LP_StatChanged(newStats));
        } else {
            int sp = stats.getSp() + amount;
            sp = Math.min(GameConstants.MAX_BASIC_STAT, sp);
            Map<Stat, Object> newStats = new EnumMap<>(Stat.class);
            newStats.put(Stat.SP, sp);
            write(new LP_StatChanged(newStats));
        }
    }

    public void attackMob(AttackInfo attackInfo) {
        final List<Mob> killedMob = new ArrayList<>();
        final Skill skill = this.getSkill(attackInfo.getSkillId());
        final SkillInfo si = skill != null ? SkillData.getInstance().getSkillInfoById(attackInfo.getSkillId()) : null;
        attackInfo.getMobAttackInfo().forEach(mai -> {
            Mob mob = field.getMobByObjectId(mai.getObjectID());
            if (mob != null) {
                long totalDamage = Arrays.stream(mai.getDamages()).sum();
                this.chatMessage(ChatMsgType.GAME_DESC, String.format("近距離攻擊: 技能: %s(%d) 怪物: %s(%d)  HP: (%d/%d) MP: (%d/%d) 總攻擊: %d 座標: %s",
                        (si != null ? si.getName() : "普通攻擊"), attackInfo.getSkillId(), mob.getTemplate().getName(),
                        mob.getTemplateId(), mob.getHp(), mob.getMaxHp(), mob.getMp(), mob.getMaxHp(), totalDamage, attackInfo.getPos().toString()));
                mob.addDamage(this, totalDamage);
                mob.damage(totalDamage);

                // 處理 multikill & combo kill
                if (!mob.isAlive()) {
                    killedMob.add(mob);
                    setComboKill(getComboKill() + 1);
                    this.write(new LP_Message(new ComboKillMessage(getComboKill(), mob.getObjectId())));
                }
            }
        });

        final int multiKillCount = killedMob.size();
        if (multiKillCount >= 3) {
            final boolean bossKilled = killedMob.stream()
                    .anyMatch(Mob::isBoss);
            final long totalExp = killedMob
                    .stream()
                    .mapToLong(Mob::getExp)
                    .sum();
            final double multiKillExpRate = GameConstants.getMultiKillExpRate(multiKillCount, bossKilled);
            final int bonusExp = (int) (multiKillExpRate * totalExp);
            this.addExp(bonusExp, null);
            this.write(new LP_Message(new MultiKillMessage(bonusExp, multiKillCount)));
        }
    }

    public void showDebugMessage(String caption, ChatMsgType type, String text) {
        //TODO 判斷debug狀態
        this.write(new LP_ChatMsg(type, "[" + caption + "] " + text));
    }

}




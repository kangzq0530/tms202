package com.msemu.world.data;

import com.msemu.commons.utils.DirUtils;
import com.msemu.commons.utils.StringUtils;
import com.msemu.commons.utils.XMLApi;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.configs.CoreConfig;
import com.msemu.world.client.character.items.Equip;
import com.msemu.world.client.character.items.Item;
import com.msemu.world.client.character.items.ItemOption;
import com.msemu.world.data.annotations.DataLoader;
import com.msemu.world.data.annotations.DataSaver;
import com.msemu.world.data.templates.EquipTemplate;
import com.msemu.world.data.templates.ItemTemplate;
import com.msemu.world.enums.InvType;
import com.msemu.world.enums.ItemState;
import com.msemu.world.enums.ScrollStat;
import com.msemu.world.enums.SpecStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.msemu.world.client.character.items.Item.Type.ITEM;
import static com.msemu.world.enums.ScrollStat.*;

/**
 * Created by Weber on 2018/4/12.
 */
public class ItemData {
    private static final Logger log = LoggerFactory.getLogger(ItemData.class);

    public static Map<Integer, EquipTemplate> equipTemplates = new HashMap<>();
    public static Map<Integer, ItemTemplate> items = new HashMap<>();
    public static List<ItemOption> itemOptions = new ArrayList<>();

    /**
     * Creates a new Equip given an itemId.
     *
     * @param itemId The itemId of the wanted equip.
     * @return A deep copy of the default values of the corresponding Equip, or null if there is no equip with itemId
     * <code>itemId</code>.
     */
    public static Equip getEquipDeepCopyFromId(int itemId) {
        EquipTemplate t = getEquipTemplateById(itemId);
        Equip ret = t == null ? null : t.toEquip();
        if (ret != null) {
            ret.setQuantity(1);
            ret.setCuttable((short) -1);
            ret.setItemState((short) ItemState.ENHANCABLE.getValue());
            ret.setDateExpire(new FileTime(-1));
        }
        return ret;
    }

    private static EquipTemplate getEquipTemplateById(int itemId) {
        return getEquipTemplates().getOrDefault(itemId, getEquipTemplateFromFile(itemId));
    }

    private static EquipTemplate getEquipTemplateFromFile(int itemId) {
        String fieldDir = String.format("%s/equipTemplates/%d.dat", CoreConfig.DAT_PATH, itemId);
        File file = new File(fieldDir);
        if (!file.exists()) {
            return null;
        } else {
            return readEquipTemplateFromFile(file);
        }
    }

    private static EquipTemplate readEquipTemplateFromFile(File file) {
        EquipTemplate t = null;
        try {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
            int itemId = dataInputStream.readInt();
            String islot = dataInputStream.readUTF();
            String vslot = dataInputStream.readUTF();
            short rJob = dataInputStream.readShort();
            short rLevel = dataInputStream.readShort();
            short rStr = dataInputStream.readShort();
            short rDex = dataInputStream.readShort();
            short rInt = dataInputStream.readShort();
            short rLuk = dataInputStream.readShort();
            short rPop = dataInputStream.readShort();
            short iStr = dataInputStream.readShort();
            short iDex = dataInputStream.readShort();
            short iInt = dataInputStream.readShort();
            short iLuk = dataInputStream.readShort();
            short iPDD = dataInputStream.readShort();
            short iMDD = dataInputStream.readShort();
            short iMaxHp = dataInputStream.readShort();
            short iMaxMp = dataInputStream.readShort();
            short iPad = dataInputStream.readShort();
            short iMad = dataInputStream.readShort();
            short iEva = dataInputStream.readShort();
            short iAcc = dataInputStream.readShort();
            short iCraft = dataInputStream.readShort();
            short iSpeed = dataInputStream.readShort();
            short iJump = dataInputStream.readShort();
            short damR = dataInputStream.readShort();
            short statR = dataInputStream.readShort();
            short ruc = dataInputStream.readShort();
            int charmEXP = dataInputStream.readInt();
            int setItemID = dataInputStream.readInt();
            int price = dataInputStream.readInt();
            int attackSpeed = dataInputStream.readInt();
            boolean cash = dataInputStream.readBoolean();
            boolean expireOnLogout = dataInputStream.readBoolean();
            boolean exItem = dataInputStream.readBoolean();
            boolean notSale = dataInputStream.readBoolean();
            boolean only = dataInputStream.readBoolean();
            boolean tradeBlock = dataInputStream.readBoolean();
            boolean equipTradeBlock = dataInputStream.readBoolean();
            boolean fixedPotential = dataInputStream.readBoolean();
            short optionLength = dataInputStream.readShort();
            List<Integer> options = new ArrayList<>(optionLength);
            for (int i = 0; i < optionLength; i++) {
                options.add(dataInputStream.readInt());
            }
            for (int i = 0; i < 7 - optionLength; i++) {
                options.add(0);
            }
            int fixedGrade = dataInputStream.readInt();
            int specialGrade = dataInputStream.readInt();

            t = new EquipTemplate();
            t.setItemId(itemId);
            t.setRuc(ruc);
            t.setIStr(iStr);
            t.setIDex(iDex);
            t.setIInt(iInt);
            t.setILuk(iLuk);
            t.setIMaxHp(iMaxHp);
            t.setIMaxMp(iMaxMp);
            t.setIPad(iPad);
            t.setIMad(iMad);
            t.setIPDD(iPad);
            t.setIMDD(iMDD);
            t.setIAcc(iAcc);
            t.setIEva(iEva);
            t.setIPad(iPad);
            t.setIPad(iPad);
            t.setICraft(iCraft);
            t.setISpeed(iSpeed);
            t.setIJump(iJump);
            t.setRStr(rStr);
            t.setRInt(rInt);
            t.setRDex(rDex);
            t.setRLuk(rLuk);
            t.setRLevel(rLevel);
            t.setRJob(rJob);
            t.setDamR(damR);
            t.setRPop(rPop);
            t.setCash(cash);
            t.setISlot(islot);
            t.setVSlot(vslot);
            t.setFixedGrade(fixedGrade);
            t.setSpecialGrade(specialGrade);
            t.setFixedPotential(fixedPotential);
            t.setTradeBlock(tradeBlock);
            t.setOnly(only);
            t.setNotSale(notSale);
            t.setAttackSpeed(attackSpeed);
            t.setPrice(price);
            t.setCharmEXP(charmEXP);
            t.setExpireOnLogout(expireOnLogout);
            t.setItemId(itemId);
            t.setExItem(exItem);
            t.setFixedGrade(fixedGrade);
            t.setEquipTradeBlock(equipTradeBlock);
            t.setOptions(options);
            t.setSpecialGrade(specialGrade);
            equipTemplates.put(t.getItemId(), t);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    @DataSaver(varName = "equipTemplates")
    public static void saveEquips(String dir) {
        DirUtils.makeDirIfAbsent(dir);
        DataOutputStream dataOutputStream;
        try {
            for (EquipTemplate equip : getEquipTemplates().values()) {
                dataOutputStream = new DataOutputStream(new FileOutputStream(dir + "/" + equip.getItemId() + ".dat"));
                dataOutputStream.writeInt(equip.getItemId());
                dataOutputStream.writeUTF(equip.getISlot());
                dataOutputStream.writeUTF(equip.getVSlot());
                dataOutputStream.writeShort(equip.getRJob());
                dataOutputStream.writeShort(equip.getRLevel());
                dataOutputStream.writeShort(equip.getRStr());
                dataOutputStream.writeShort(equip.getRDex());
                dataOutputStream.writeShort(equip.getRInt());
                dataOutputStream.writeShort(equip.getRLuk());
                dataOutputStream.writeShort(equip.getRPop());
                dataOutputStream.writeShort(equip.getIStr());
                dataOutputStream.writeShort(equip.getIDex());
                dataOutputStream.writeShort(equip.getIInt());
                dataOutputStream.writeShort(equip.getILuk());
                dataOutputStream.writeShort(equip.getIPDD());
                dataOutputStream.writeShort(equip.getIMDD());
                dataOutputStream.writeShort(equip.getIMaxHp());
                dataOutputStream.writeShort(equip.getIMaxMp());
                dataOutputStream.writeShort(equip.getIPad());
                dataOutputStream.writeShort(equip.getIMad());
                dataOutputStream.writeShort(equip.getIEva());
                dataOutputStream.writeShort(equip.getIAcc());
                dataOutputStream.writeShort(equip.getICraft());
                dataOutputStream.writeShort(equip.getISpeed());
                dataOutputStream.writeShort(equip.getIJump());
                dataOutputStream.writeShort(equip.getDamR());
                dataOutputStream.writeShort(equip.getStatR());
                dataOutputStream.writeShort(equip.getRuc());
                dataOutputStream.writeInt(equip.getCharmEXP());
                dataOutputStream.writeInt(equip.getSetItemID());
                dataOutputStream.writeInt(equip.getPrice());
                dataOutputStream.writeInt(equip.getAttackSpeed());
                dataOutputStream.writeBoolean(equip.isCash());
                dataOutputStream.writeBoolean(equip.isExpireOnLogout());
                dataOutputStream.writeBoolean(equip.isExItem());
                dataOutputStream.writeBoolean(equip.isNotSale());
                dataOutputStream.writeBoolean(equip.isOnly());
                dataOutputStream.writeBoolean(equip.isTradeBlock());
                dataOutputStream.writeBoolean(equip.isEquipTradeBlock());
                dataOutputStream.writeBoolean(equip.isFixedPotential());
                dataOutputStream.writeShort(equip.getOptions().size());
                for (int i : equip.getOptions()) {
                    dataOutputStream.writeInt(i);
                }
                dataOutputStream.writeInt(equip.getFixedGrade());
                dataOutputStream.writeInt(equip.getSpecialGrade());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void loadEquipsFromWz() {
        String wzDir = CoreConfig.WZ_PATH + "/Character.wz";
        String[] subMaps = new String[]{"Accessory", "Android", "Cap", "Cape", "Coat", "Dragon", "Face", "Glove",
                "Longcoat", "Mechanic", "Pants", "PetEquip", "Ring", "Shield", "Shoes", "Totem", "Weapon"};
        for (String subMap : subMaps) {
            File subDir = new File(String.format("%s/%s", wzDir, subMap));
            File[] files = subDir.listFiles();
            for (File file : files) {
                Document doc = XMLApi.getRoot(file);
                Node node = doc;
                List<Node> nodes = XMLApi.getAllChildren(node);
                for (Node mainNode : nodes) {
                    Map<String, String> attributes = XMLApi.getAttributes(mainNode);
                    String name = attributes.get("name");
                    int itemId = -1;
                    if (name != null) {
                        itemId = Integer.parseInt(attributes.get("name").replace(".img", ""));
                        String islot = "";
                        String vslot = "";
                        int reqJob = 0;
                        int reqLevel = 0;
                        int reqStr = 0;
                        int reqDex = 0;
                        int reqInt = 0;
                        int reqLuk = 0;
                        int incStr = 0;
                        int incDex = 0;
                        int incInt = 0;
                        int incLuk = 0;
                        int incPDD = 0;
                        int incMDD = 0;
                        int incPAD = 0;
                        int incMAD = 0;
                        int charmEXP = 0;
                        int incMHP = 0;
                        int incMMP = 0;
                        int incACC = 0;
                        int incEVA = 0;
                        int incCraft = 0;
                        int incSpeed = 0;
                        int incJump = 0;
                        int ruc = 0;
                        int price = 0;
                        int attackSpeed = 0;
                        int damR = 0;
                        int statR = 0;
                        int reqPop = 0;
                        int setItemID = 0;
                        boolean cash = false;
                        boolean expireOnLogout = false;
                        boolean notSale = false;
                        boolean only = false;
                        boolean tradeBlock = false;
                        boolean fixedPotential = false;
                        boolean exItem = false;
                        boolean equipTradeBlock = false;
                        List<Integer> options = new ArrayList<>(7);
                        int fixedGrade = 0;
                        int specialGrade = 0;
                        for (Node n : XMLApi.getAllChildren(XMLApi.getFirstChildByNameBF(mainNode, "info"))) {
                            attributes = XMLApi.getAttributes(n);
                            boolean hasISlot = attributes.get("name").equalsIgnoreCase("islot");
                            if (hasISlot) {
                                islot = attributes.get("value");
                            }
                            boolean hasVSlot = attributes.get("name").equalsIgnoreCase("vslot");
                            if (hasVSlot) {
                                vslot = attributes.get("value");
                            }
                            boolean hasReqJob = attributes.get("name").equalsIgnoreCase("reqJob");
                            if (hasReqJob) {
                                reqJob = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasReqLevel = attributes.get("name").equalsIgnoreCase("reqLevel");
                            if (hasReqLevel) {
                                reqLevel = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasReqStr = attributes.get("name").equalsIgnoreCase("reqSTR");
                            if (hasReqStr) {
                                reqStr = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasReqDex = attributes.get("name").equalsIgnoreCase("reqDex");
                            if (hasReqDex) {
                                reqDex = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasReqInt = attributes.get("name").equalsIgnoreCase("reqInt");
                            if (hasReqInt) {
                                reqInt = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasReqLuk = attributes.get("name").equalsIgnoreCase("reqLuk");
                            if (hasReqLuk) {
                                reqLuk = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasreqPOP = attributes.get("name").equalsIgnoreCase("reqPOP");
                            if (hasreqPOP) {
                                reqPop = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasIncStr = attributes.get("name").equalsIgnoreCase("incStr");
                            if (hasIncStr) {
                                incStr = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasincDex = attributes.get("name").equalsIgnoreCase("incDex");
                            if (hasincDex) {
                                incDex = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasincInt = attributes.get("name").equalsIgnoreCase("incInt");
                            if (hasincInt) {
                                incInt = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasincLuk = attributes.get("name").equalsIgnoreCase("incLuk");
                            if (hasincLuk) {
                                incLuk = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasincPDD = attributes.get("name").equalsIgnoreCase("incPDD");
                            if (hasincPDD) {
                                incPDD = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasincMDD = attributes.get("name").equalsIgnoreCase("incMDD");
                            if (hasincMDD) {
                                incMDD = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasincMHP = attributes.get("name").equalsIgnoreCase("incMHP");
                            if (hasincMHP) {
                                incMHP = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasincMMP = attributes.get("name").equalsIgnoreCase("incMMP");
                            if (hasincMMP) {
                                incMMP = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasincPAD = attributes.get("name").equalsIgnoreCase("incPAD");
                            if (hasincPAD) {
                                incPAD = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasincMAD = attributes.get("name").equalsIgnoreCase("incMAD");
                            if (hasincMAD) {
                                incMAD = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasincEVA = attributes.get("name").equalsIgnoreCase("incEVA");
                            if (hasincEVA) {
                                incEVA = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasincACC = attributes.get("name").equalsIgnoreCase("incACC");
                            if (hasincACC) {
                                incACC = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasincSpeed = attributes.get("name").equalsIgnoreCase("incSpeed");
                            if (hasincSpeed) {
                                incSpeed = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasincJump = attributes.get("name").equalsIgnoreCase("incJump");
                            if (hasincJump) {
                                incJump = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasdamR = attributes.get("name").equalsIgnoreCase("damR");
                            if (hasdamR) {
                                damR = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasstatR = attributes.get("name").equalsIgnoreCase("statR");
                            if (hasstatR) {
                                statR = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasruc = attributes.get("name").equalsIgnoreCase("tuc");
                            if (hasruc) {
                                ruc = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hassetItemID = attributes.get("name").equalsIgnoreCase("setItemID");
                            if (hassetItemID) {
                                setItemID = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasprice = attributes.get("name").equalsIgnoreCase("price");
                            if (hasprice) {
                                price = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasattackSpeed = attributes.get("name").equalsIgnoreCase("attackSpeed");
                            if (hasattackSpeed) {
                                attackSpeed = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hascash = attributes.get("name").equalsIgnoreCase("cash");
                            if (hascash) {
                                cash = Integer.parseInt(attributes.get("value")) == 1;
                            }
                            boolean hasexpireOnLogout = attributes.get("name").equalsIgnoreCase("expireOnLogout");
                            if (hasexpireOnLogout) {
                                expireOnLogout = Integer.parseInt(attributes.get("value")) == 1;
                            }
                            boolean hasexItem = attributes.get("name").equalsIgnoreCase("exItem");
                            if (hasexItem) {
                                exItem = Integer.parseInt(attributes.get("value")) == 1;
                            }
                            boolean hasnotSale = attributes.get("name").equalsIgnoreCase("notSale");
                            if (hasnotSale) {
                                notSale = Integer.parseInt(attributes.get("value")) == 1;
                            }
                            boolean hasonly = attributes.get("name").equalsIgnoreCase("only");
                            if (hasonly) {
                                only = Integer.parseInt(attributes.get("value")) == 1;
                            }
                            boolean hastradeBlock = attributes.get("name").equalsIgnoreCase("tradeBlock");
                            if (hastradeBlock) {
                                tradeBlock = Integer.parseInt(attributes.get("value")) == 1;
                            }
                            boolean hasequipTradeBlock = attributes.get("name").equalsIgnoreCase("equipTradeBlock");
                            if (hasequipTradeBlock) {
                                equipTradeBlock = Integer.parseInt(attributes.get("value")) == 1;
                            }
                            boolean hasfixedPotential = attributes.get("name").equalsIgnoreCase("fixedPotential");
                            if (hasfixedPotential) {
                                fixedPotential = Integer.parseInt(attributes.get("value")) == 1;
                            }
                            boolean hasOptions = attributes.get("name").equalsIgnoreCase("option");
                            if (hasOptions) {
                                for (Node whichOptionNode : XMLApi.getAllChildren(n)) {
                                    attributes = XMLApi.getAttributes(whichOptionNode);
                                    int index = Integer.parseInt(attributes.get("name"));
                                    Node optionNode = XMLApi.getFirstChildByNameBF(whichOptionNode, "option");
                                    Map<String, String> optionAttr = XMLApi.getAttributes(optionNode);
                                    options.set(index, Integer.parseInt(optionAttr.get("value")));
                                }
                            }
                            for (int i = 0; i < 7 - options.size(); i++) {
                                options.add(0);
                            }

                            boolean hasfixedGrade = attributes.get("name").equalsIgnoreCase("fixedGrade");
                            if (hasfixedGrade) {
                                fixedGrade = Integer.parseInt(attributes.get("value"));
                            }
                            boolean hasspecialGrade = attributes.get("name").equalsIgnoreCase("specialGrade");
                            if (hasspecialGrade) {
                                specialGrade = Integer.parseInt(attributes.get("value"));
                            }
                        }

                        EquipTemplate t = new EquipTemplate();


                        t.setItemId(itemId);
                        t.setRuc((short) ruc);
                        t.setIStr((short) incStr);
                        t.setIDex((short) incDex);
                        t.setIInt((short) incInt);
                        t.setILuk((short) incLuk);
                        t.setIMaxHp((short) incMHP);
                        t.setIMaxMp((short) incMMP);
                        t.setIPad((short) incPAD);
                        t.setIMad((short) incMAD);
                        t.setIPDD((short) incPDD);
                        t.setIMDD((short) incMDD);
                        t.setIAcc((short) incACC);
                        t.setIEva((short) incEVA);
                        t.setIPad((short) incPAD);
                        t.setIPad((short) incPAD);
                        t.setICraft((short) incCraft);
                        t.setISpeed((short) incSpeed);
                        t.setIJump((short) incJump);
                        t.setRStr((short) reqStr);
                        t.setRInt((short) reqInt);
                        t.setRDex((short) reqDex);
                        t.setRLuk((short) reqLuk);
                        t.setRLevel((short) reqLevel);
                        t.setRJob((short) reqJob);
                        t.setDamR((short) damR);
                        t.setRPop((short) reqPop);
                        t.setCash(cash);
                        t.setISlot(islot);
                        t.setVSlot(vslot);
                        t.setFixedGrade(fixedGrade);
                        t.setSpecialGrade(specialGrade);
                        t.setFixedPotential(fixedPotential);
                        t.setTradeBlock(tradeBlock);
                        t.setOnly(only);
                        t.setNotSale(notSale);
                        t.setAttackSpeed(attackSpeed);
                        t.setPrice(price);
                        t.setCharmEXP(charmEXP);
                        t.setExpireOnLogout(expireOnLogout);
                        t.setItemId(itemId);
                        t.setExItem(exItem);
                        t.setFixedGrade(fixedGrade);
                        t.setEquipTradeBlock(equipTradeBlock);
                        t.setOptions(options);
                        t.setSpecialGrade(specialGrade);
                        getEquipTemplates().put(t.getItemId(), t);
                    }
                }
            }
        }
    }

    public static ItemTemplate loadItemByFile(File file) {
        ItemTemplate itemInfo = null;
        try {
            itemInfo = new ItemTemplate();
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
            itemInfo.setItemId(dataInputStream.readInt());
            itemInfo.setInvType(InvType.getInvTypeByString(dataInputStream.readUTF()));
            itemInfo.setCash(dataInputStream.readBoolean());
            itemInfo.setPrice(dataInputStream.readInt());
            itemInfo.setSlotMax(dataInputStream.readInt());
            itemInfo.setTradeBlock(dataInputStream.readBoolean());
            itemInfo.setNotSale(dataInputStream.readBoolean());
            itemInfo.setPath(dataInputStream.readUTF());
            itemInfo.setNoCursed(dataInputStream.readBoolean());
            itemInfo.setBagType(dataInputStream.readInt());
            itemInfo.setCharmEXP(dataInputStream.readInt());
            itemInfo.setSenseEXP(dataInputStream.readInt());
            itemInfo.setQuest(dataInputStream.readBoolean());
            itemInfo.setReqQuestOnProgress(dataInputStream.readInt());
            itemInfo.setNotConsume(dataInputStream.readBoolean());
            itemInfo.setMonsterBook(dataInputStream.readBoolean());
            itemInfo.setMobID(dataInputStream.readInt());
            itemInfo.setNpcID(dataInputStream.readInt());
            itemInfo.setLinkedID(dataInputStream.readInt());
            short size = dataInputStream.readShort();
            for (int i = 0; i < size; i++) {
                ScrollStat ss = ScrollStat.getScrollStatByString(dataInputStream.readUTF());
                int val = dataInputStream.readInt();
                itemInfo.putScrollStat(ss, val);
            }
            size = dataInputStream.readShort();
            for (int i = 0; i < size; i++) {
                SpecStat ss = SpecStat.getSpecStatByName(dataInputStream.readUTF());
                int val = dataInputStream.readInt();
                itemInfo.putSpecStat(ss, val);
            }
            size = dataInputStream.readShort();
            for (int i = 0; i < size; i++) {
                itemInfo.addQuest(dataInputStream.readInt());
            }
            getItems().put(itemInfo.getItemId(), itemInfo);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemInfo;

    }

    public static void saveItems(String dir) {
        DirUtils.makeDirIfAbsent(dir);
        DataOutputStream dataOutputStream;
        try {
            for (ItemTemplate ii : getItems().values()) {
                dataOutputStream = new DataOutputStream(new FileOutputStream(new File(dir + "/" + ii.getItemId() + ".dat")));
                dataOutputStream.writeInt(ii.getItemId());
                dataOutputStream.writeUTF(ii.getInvType().toString());
                dataOutputStream.writeBoolean(ii.isCash());
                dataOutputStream.writeInt(ii.getPrice());
                dataOutputStream.writeInt(ii.getSlotMax());
                dataOutputStream.writeBoolean(ii.isTradeBlock());
                dataOutputStream.writeBoolean(ii.isNotSale());
                dataOutputStream.writeUTF(ii.getPath());
                dataOutputStream.writeBoolean(ii.isNoCursed());
                dataOutputStream.writeInt(ii.getBagType());
                dataOutputStream.writeInt(ii.getCharmEXP());
                dataOutputStream.writeInt(ii.getSenseEXP());
                dataOutputStream.writeBoolean(ii.isQuest());
                dataOutputStream.writeInt(ii.getReqQuestOnProgress());
                dataOutputStream.writeBoolean(ii.isNotConsume());
                dataOutputStream.writeBoolean(ii.isMonsterBook());
                dataOutputStream.writeInt(ii.getMobID());
                dataOutputStream.writeInt(ii.getNpcID());
                dataOutputStream.writeInt(ii.getLinkedID());
                dataOutputStream.writeShort(ii.getScrollStats().size());
                for (Map.Entry<ScrollStat, Integer> entry : ii.getScrollStats().entrySet()) {
                    dataOutputStream.writeUTF(entry.getKey().toString());
                    dataOutputStream.writeInt(entry.getValue());
                }
                dataOutputStream.writeShort(ii.getSpecStats().size());
                for (Map.Entry<SpecStat, Integer> entry : ii.getSpecStats().entrySet()) {
                    dataOutputStream.writeUTF(entry.getKey().toString());
                    dataOutputStream.writeInt(entry.getValue());
                }
                dataOutputStream.writeShort(ii.getQuestIDs().size());
                for (int i : ii.getQuestIDs()) {
                    dataOutputStream.writeInt(i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadItemsFromWZ() {
        String wzDir = CoreConfig.WZ_PATH + "/Item.wz";
        String[] subMaps = new String[]{"Cash", "Consume", "Etc", "Install", "Special"}; // not pet
        for (String subMap : subMaps) {
            File subDir = new File(String.format("%s/%s", wzDir, subMap));
            File[] files = subDir.listFiles();
            for (File file : files) {
                Document doc = XMLApi.getRoot(file);
                Node node = doc;
                List<Node> nodes = XMLApi.getAllChildren(node);
                for (Node mainNode : XMLApi.getAllChildren(nodes.get(0))) {
                    String nodeName = XMLApi.getNamedAttribute(mainNode, "name");
                    if (!StringUtils.isNumber(nodeName)) {
                        log.error(String.format("%s is not a number.", nodeName));
                        continue;
                    }
                    int id = Integer.parseInt(nodeName);
                    ItemTemplate item = new ItemTemplate();
                    item.setItemId(id);
                    item.setInvType(InvType.getInvTypeByString(subMap));
                    Node infoNode = XMLApi.getFirstChildByNameBF(mainNode, "info");
                    if (infoNode != null) {
                        for (Node info : XMLApi.getAllChildren(infoNode)) {
                            String name = XMLApi.getNamedAttribute(info, "name");
                            String value = XMLApi.getNamedAttribute(info, "value");
                            switch (name) {
                                case "cash":
                                    item.setCash(Integer.parseInt(value) != 0);
                                    break;
                                case "price":
                                    item.setPrice(Integer.parseInt(value));
                                    break;
                                case "slotMax":
                                    item.setSlotMax(Integer.parseInt(value));
                                    break;
                                // info not currently interesting. May be interesting in the future.
                                case "icon":
                                case "iconRaw":
                                case "iconD":
                                case "iconReward":
                                case "iconShop":
                                case "recoveryHP":
                                case "recoveryMP":
                                case "sitAction":
                                case "bodyRelMove":
                                case "only":
                                case "noDrop":
                                case "timeLimited":
                                case "accountSharable":
                                case "nickTag":
                                case "nickSkill":
                                case "endLotteryDate":
                                case "noFlip":
                                case "noMoveToLocker":
                                case "soldInform":
                                case "purchaseShop":
                                case "flatRate":
                                case "limitMin":
                                case "protectTime":
                                case "maxDays":
                                case "reset":
                                case "replace":
                                case "expireOnLogout":
                                case "max":
                                case "lvOptimum":
                                case "lvRange":
                                case "limitedLv":
                                case "tradeReward":
                                case "type":
                                case "floatType":
                                case "message":
                                case "pquest":
                                case "bonusEXPRate":
                                case "notExtend":
                                case "skill":
                                case "reqSkillLevel":
                                case "masterLevel":
                                case "stateChangeItem":
                                case "direction":
                                case "reqEquipLevelMax":
                                case "exGrade":
                                case "exGradeWeight":
                                case "effect":
                                case "bigSize":
                                case "nickSkillTimeLimited":
                                case "StarPlanet":
                                case "useTradeBlock":
                                case "commerce":
                                case "invisibleWeapon":
                                case "sitEmotion":
                                case "sitLeft":
                                case "tamingMob":
                                case "textInfo":
                                case "lv":
                                case "tradeAvailable":
                                case "pickUpBlock":
                                case "rewardItemID":
                                case "autoPrice":
                                case "selectedSlot":
                                case "minusLevel":
                                case "addTime":
                                case "reqLevel":
                                case "waittime":
                                case "buffchair":
                                case "cooltime":
                                case "consumeitem":
                                case "distanceX":
                                case "distanceY":
                                case "maxDiff":
                                case "maxDX":
                                case "levelDX":
                                case "maxLevel":
                                case "exp":
                                case "dropBlock":
                                case "dropExpireTime":
                                case "animation_create":
                                case "animation_dropped":
                                case "noCancelMouse":
                                case "soulItemType":
                                case "Rate":
                                case "unitPrice":
                                case "delayMsg":
                                case "bridlePropZeroMsg":
                                case "create":
                                case "nomobMsg":
                                case "bridleProp":
                                case "bridlePropChg":
                                case "bridleMsgType":
                                case "mobHP":
                                case "left":
                                case "right":
                                case "top":
                                case "bottom":
                                case "useDelay":
                                case "name":
                                case "uiData":
                                case "grade":
                                case "UI":
                                case "recoveryRate":
                                case "itemMsg":
                                case "noRotateIcon":
                                case "endUseDate":
                                case "noSound":
                                case "slotMat":
                                case "isBgmOrEffect":
                                case "bgmPath":
                                case "repeat":
                                case "NoCancel":
                                case "rotateSpeed":
                                case "gender":
                                case "life":
                                case "pickupItem":
                                case "add":
                                case "consumeHP":
                                case "longRange":
                                case "dropSweep":
                                case "pickupAll":
                                case "ignorePickup":
                                case "consumeMP":
                                case "autoBuff":
                                case "smartPet":
                                case "giantPet":
                                case "shop":
                                case "recall":
                                case "autoSpeaking":
                                case "consumeCure":
                                case "meso":
                                case "maplepoint":
                                case "rate":
                                case "overlap":
                                case "lt":
                                case "rb":
                                case "path4Top":
                                case "jumplevel":
                                case "slotIndex":
                                case "addDay":
                                case "incLEV":
                                case "cashTradeBlock":
                                case "dressUpgrade":
                                case "skillEffectID":
                                case "emotion":
                                case "tradBlock":
                                case "tragetBlock":
                                case "scanTradeBlock":
                                case "mobPotion":
                                case "ignoreTendencyStatLimit":
                                case "effectByItemID":
                                case "pachinko":
                                case "iconEnter":
                                case "iconLeave":
                                case "noMoveIcon":
                                case "noShadow":
                                case "preventslip":
                                case "recover":
                                case "warmsupport":
                                case "randstat":
                                case "reqCUC":
                                case "incCraft":
                                case "reqEquipLevelMin":
                                case "incRandVol":
                                case "incPVPDamage":
                                case "successRates":
                                case "enchantCategory":
                                case "additionalSuccess":
                                case "level":
                                case "specialItem":
                                case "createType":
                                case "exNew":
                                case "cuttable":
                                case "setItemCategory":
                                case "perfectReset":
                                case "resetRUC":
                                case "incMax":
                                case "noSuperior":
                                case "noRecovery":
                                case "reqMap":
                                case "random":
                                case "limit":
                                case "cantAccountSharable":
                                case "LvUpWarning":
                                case "canAccountSharable":
                                case "canUseJob":
                                case "createPeriod":
                                case "iconLarge":
                                case "morphItem":
                                case "consumableFrom":
                                case "noExpend":
                                case "sample":
                                case "notPickUpByPet":
                                case "sharableOnce":
                                case "bonusStageItem":
                                case "sampleOffsetY":
                                case "runOnPickup":
                                case "noSale":
                                case "skillCast":
                                case "activateCardSetID":
                                case "summonSoulMobID":
                                case "cursor":
                                case "karma":
                                case "pointCost":
                                case "itemPoint":
                                case "sharedStatCostGrade":
                                case "levelVariation":
                                case "accountShareable":
                                case "extendLimit":
                                case "showMessage":
                                case "mcType":
                                case "consumeItem":
                                case "hybrid":
                                case "mobId":
                                case "lvMin":
                                case "lvMax":
                                case "picture":
                                case "ratef":
                                case "time":
                                case "reqGuildLevel":
                                case "guild":
                                case "randEffect":
                                case "accountShareTag":
                                case "removeEffect":
                                case "forcingItem":
                                case "fixFrameIdx":
                                case "buffItemID":
                                case "removeCharacterInfo":
                                case "nameInfo":
                                case "bgmInfo":
                                case "flip":
                                case "pos":
                                case "randomChair":
                                case "maxLength":
                                case "continuity":
                                case "specificDX":
                                case "groupTWInfo":
                                case "face":
                                case "removeBody":
                                case "mesoChair":
                                case "towerBottom":
                                case "towerTop":
                                case "topOffset":
                                case "craftEXP":
                                case "willEXP":
                                    break;
                                case "tradeBlock":
                                    item.setTradeBlock(Integer.parseInt(value) != 0);
                                    break;
                                case "notSale":
                                    item.setNotSale(Integer.parseInt(value) != 0);
                                    break;
                                case "path":
                                    item.setPath(value);
                                    break;
                                case "noCursed":
                                    item.setNoCursed(Integer.parseInt(value) != 0);
                                    break;
                                case "noNegative":
                                    item.putScrollStat(noNegative, Integer.parseInt(value));
                                    break;
                                case "success":
                                    item.putScrollStat(success, Integer.parseInt(value));
                                    break;
                                case "incSTR":
                                    item.putScrollStat(incSTR, Integer.parseInt(value));
                                    break;
                                case "incDEX":
                                    item.putScrollStat(incDEX, Integer.parseInt(value));
                                    break;
                                case "incINT":
                                    item.putScrollStat(incINT, Integer.parseInt(value));
                                    break;
                                case "incLUK":
                                    item.putScrollStat(incLUK, Integer.parseInt(value));
                                    break;
                                case "incPAD":
                                    item.putScrollStat(incPAD, Integer.parseInt(value));
                                    break;
                                case "incMAD":
                                    item.putScrollStat(incMAD, Integer.parseInt(value));
                                    break;
                                case "incPDD":
                                    item.putScrollStat(incPDD, Integer.parseInt(value));
                                    break;
                                case "incMDD":
                                    item.putScrollStat(incMDD, Integer.parseInt(value));
                                    break;
                                case "incEVA":
                                    item.putScrollStat(incEVA, Integer.parseInt(value));
                                    break;
                                case "incACC":
                                    item.putScrollStat(incACC, Integer.parseInt(value));
                                    break;
                                case "incPERIOD":
                                    item.putScrollStat(incPERIOD, Integer.parseInt(value));
                                    break;
                                case "incMHP":
                                case "incMaxHP":
                                    item.putScrollStat(incMHP, Integer.parseInt(value));
                                    break;
                                case "incMMP":
                                case "incMaxMP":
                                    item.putScrollStat(incMMP, Integer.parseInt(value));
                                    break;
                                case "incSpeed":
                                    item.putScrollStat(incSpeed, Integer.parseInt(value));
                                    break;
                                case "incJump":
                                    item.putScrollStat(incJump, Integer.parseInt(value));
                                    break;
                                case "incReqLevel":
                                    item.putScrollStat(incReqLevel, Integer.parseInt(value));
                                    break;
                                case "randOption":
                                    item.putScrollStat(randOption, Integer.parseInt(value));
                                    break;
                                case "randStat":
                                    item.putScrollStat(randStat, Integer.parseInt(value));
                                    break;
                                case "tuc":
                                    item.putScrollStat(tuc, Integer.parseInt(value));
                                    break;
                                case "incIUC":
                                    item.putScrollStat(incIUC, Integer.parseInt(value));
                                    break;
                                case "speed":
                                    item.putScrollStat(speed, Integer.parseInt(value));
                                    break;
                                case "forceUpgrade":
                                    item.putScrollStat(forceUpgrade, Integer.parseInt(value));
                                    break;
                                case "cursed":
                                    item.putScrollStat(cursed, Integer.parseInt(value));
                                    break;
                                case "maxSuperiorEqp":
                                    item.putScrollStat(maxSuperiorEqp, Integer.parseInt(value));
                                    break;
                                case "reqRUC":
                                    item.putScrollStat(reqRUC, Integer.parseInt(value));
                                    break;
                                case "bagType":
                                    item.setBagType(Integer.parseInt(value));
                                    break;
                                case "charmEXP":
                                case "charismaEXP":
                                    item.setCharmEXP(Integer.parseInt(value));
                                    break;
                                case "senseEXP":
                                    item.setSenseEXP(Integer.parseInt(value));
                                    break;
                                case "quest":
                                    item.setQuest(Integer.parseInt(value) != 0);
                                    break;
                                case "reqQuestOnProgress":
                                    item.setReqQuestOnProgress(Integer.parseInt(value));
                                    break;
                                case "qid":
                                case "questId":
                                    if (value.contains(".") && value.split("[.]").length > 0) {
                                        item.addQuest(Integer.parseInt(value.split("[.]")[0]));
                                    } else {
                                        item.addQuest(Integer.parseInt(value));
                                    }
                                    break;
                                case "notConsume":
                                    item.setNotConsume(Integer.parseInt(value) != 0);
                                    break;
                                case "monsterBook":
                                    item.setMonsterBook(Integer.parseInt(value) != 0);
                                    break;
                                case "mob":
                                    item.setMobID(Integer.parseInt(value));
                                    break;
                                case "npc":
                                    item.setNpcID(Integer.parseInt(value));
                                    break;
                                case "linkedID":
                                    item.setLinkedID(Integer.parseInt(value));
                                    break;
                                case "spec":
                                    break;
                                default:
                                    log.warn(String.format("Unkown node: %s, value = %s, itemID = %s", name, value, item.getItemId()));
                            }
                        }
                    }
                    Node spec = XMLApi.getFirstChildByNameBF(mainNode, "spec");
                    if (spec != null) {
                        for (Node specNode : XMLApi.getAllChildren(spec)) {
                            String name = XMLApi.getNamedAttribute(specNode, "name");
                            String value = XMLApi.getNamedAttribute(specNode, "value");
                            switch (name) {
                                case "script":
                                    item.setScript(value);
                                default:
                                    SpecStat ss = SpecStat.getSpecStatByName(name);
                                    if (ss != null && value != null) {
                                        item.putSpecStat(ss, Integer.parseInt(value));
                                    }
                            }
                        }
                    }
                    getItems().put(item.getItemId(), item);
                }
            }
        }
    }

    public static Item getDeepCopyByItemInfo(ItemTemplate itemInfo) {
        if (itemInfo == null) {
            return null;
        }
        Item res = new Item();
        res.setItemId(itemInfo.getItemId());
        res.setQuantity(1);
        res.setType(ITEM);
        res.setInvType(itemInfo.getInvType());
        return res;
    }

    public static Item getItemDeepCopy(int id) {
        return getDeepCopyByItemInfo(getItemInfoByID(id));
    }

    public static ItemTemplate getItemInfoByID(int itemID) {
        ItemTemplate ii = getItems().getOrDefault(itemID, null);
        if (ii == null) {
            File file = new File(String.format("%s/items/%d.dat", CoreConfig.DAT_PATH, itemID));
            if (!file.exists()) {
                return null;
            } else {
                ii = loadItemByFile(file);
            }
        }
        return ii;
    }

    public static Map<Integer, EquipTemplate> getEquipTemplates() {
        return equipTemplates;
    }

    public static void loadItemOptionsFromWZ() {
        String wzDir = CoreConfig.WZ_PATH + "/Item.wz";
        String itemOptionDir = String.format("%s/ItemOption.img.xml", wzDir);
        File file = new File(itemOptionDir);
        Document doc = XMLApi.getRoot(file);
        Node node = doc;
        List<Node> nodes = XMLApi.getAllChildren(node);
        for (Node mainNode : XMLApi.getAllChildren(nodes.get(0))) {
            ItemOption io = new ItemOption();
            String nodeName = XMLApi.getNamedAttribute(mainNode, "name");
            io.setId(Integer.parseInt(nodeName));
            int optionType = 0;
            Node typeNode = XMLApi.getFirstChildByNameBF(mainNode, "optionType");
            if (typeNode != null) {
                optionType = Integer.parseInt(XMLApi.getNamedAttribute(typeNode, "value"));
            }
            int weight = 0;
            Node weightNode = XMLApi.getFirstChildByNameBF(mainNode, "weight");
            if (weightNode != null) {
                weight = Integer.parseInt(XMLApi.getNamedAttribute(weightNode, "value"));
            }
            int reqLevel = 0;
            Node reqLevelNode = XMLApi.getFirstChildByNameBF(mainNode, "reqLevel");
            if (reqLevelNode != null) {
                reqLevel = Integer.parseInt(XMLApi.getNamedAttribute(reqLevelNode, "value"));
            }
            io.setOptionType(optionType);
            io.setWeight(weight);
            io.setReqLevel(reqLevel);
            if (weight == 0) {
                io.setWeight(1);
            }
            getItemOptions().add(io);
        }
    }

    public static List<ItemOption> getItemOptions() {
        return itemOptions;
    }

    public static void saveItemOptions(String dir) {
        File file = new File(String.format("%s/itemOptions.dat", dir));
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
            dos.writeInt(getItemOptions().size());
            for (ItemOption io : getItemOptions()) {
                dos.writeInt(io.getId());
                dos.writeInt(io.getOptionType());
                dos.writeInt(io.getWeight());
                dos.writeInt(io.getReqLevel());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DataLoader(varName = "itemOptions")
    public static void loadItemOptions(File file, boolean exists) {
        if (!exists) {
            loadItemOptionsFromWZ();
            saveItemOptions(CoreConfig.DAT_PATH);
        } else {
            try {
                DataInputStream dis = new DataInputStream(new FileInputStream(file));
                int size = dis.readInt();
                for (int i = 0; i < size; i++) {
                    ItemOption io = new ItemOption();
                    io.setId(dis.readInt());
                    io.setOptionType(dis.readInt());
                    io.setWeight(dis.readInt());
                    io.setReqLevel(dis.readInt());
                    getItemOptions().add(io);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unused") // Reflection
    public static void generateDatFiles() {
        loadEquipsFromWz();
        loadItemsFromWZ();
        QuestData.linkItemData();
        saveEquips(CoreConfig.DAT_PATH + "/equipTemplates");
        saveItems(CoreConfig.DAT_PATH + "/items");
        loadItemOptionsFromWZ();
        saveItemOptions(CoreConfig.DAT_PATH);
    }

    public static void main(String[] args) {
        generateDatFiles();
    }

    public static Map<Integer, ItemTemplate> getItems() {
        return items;
    }

    public static void addItemInfo(ItemTemplate ii) {
        getItems().put(ii.getItemId(), ii);
    }
}

package com.msemu.world.constants;

import com.msemu.world.client.character.items.Equip;
import com.msemu.world.client.character.items.ItemOption;
import com.msemu.world.data.ItemData;
import com.msemu.world.enums.ItemGrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/3/31.
 */
public class ItemConstants {
    private static final Logger log = LoggerFactory.getLogger(ItemConstants.class);

    public static int getGenderFromId(int nItemID) {
        int result; // eax

        if (nItemID / 1000000 != 1 && nItemID / 10000 != 254 || nItemID / 10000 == 119 || nItemID / 10000 == 168)
            return 2;
        switch (nItemID / 1000 % 10) {
            case 0:
                result = 0;
                break;
            case 1:
                result = 1;
                break;
            default:
                return 2;
        }
        return result;
    }

    public static int getBodyPartFromItem(int nItemID, int gender) {
        List<Integer> arr = getBodyPartArrayFromItem(nItemID, gender);
        int result = arr.size() > 0 ? arr.get(0) : 0;
        return result;
    }

    public static List<Integer> getBodyPartArrayFromItem(int itemID, int genderArg) {
        int gender = getGenderFromId(itemID);
        int prefix = itemID / 10000;
        List<Integer> bodyPartList = new ArrayList<>();
        if (prefix != 119 && prefix != 168) {
            if (gender != 2 && genderArg != 2 && gender != genderArg) {
                return bodyPartList;
            }
        }
        switch (prefix) {
            case 100:
                bodyPartList.add(1);
                bodyPartList.add(1200);
                bodyPartList.add(1300);
                bodyPartList.add(1501);
                break;
            case 101:
                bodyPartList.add(2);
                bodyPartList.add(1202);
                bodyPartList.add(1302);
                bodyPartList.add(1502);
                break;
            case 102:
                bodyPartList.add(3);
                bodyPartList.add(1500);
                break;
            case 103:
                bodyPartList.add(4);
                bodyPartList.add(1503);
                break;
            case 104:
            case 105:
                bodyPartList.add(5);
                bodyPartList.add(1203);
                bodyPartList.add(1505);
                break;
            case 106:
                bodyPartList.add(6);
                bodyPartList.add(1204);
                bodyPartList.add(1505);
                break;
            case 107:
                bodyPartList.add(7);
                bodyPartList.add(1205);
                bodyPartList.add(1509);
                break;
            case 108:
                bodyPartList.add(8);
                bodyPartList.add(1206);
                bodyPartList.add(1304);
                bodyPartList.add(1506);
                break;
            case 109:
            case 134:
            case 135:
            case 156:
                bodyPartList.add(10);
                break;
            case 110:
                bodyPartList.add(9);
                bodyPartList.add(1201);
                bodyPartList.add(1301);
                bodyPartList.add(1504);
                break;
            case 111:
                bodyPartList.add(12);
                bodyPartList.add(13);
                bodyPartList.add(15);
                bodyPartList.add(16);
                bodyPartList.add(1510);
                bodyPartList.add(1511);
                break;
            case 112:
                bodyPartList.add(17);
                bodyPartList.add(65);
                bodyPartList.add(1512);
                bodyPartList.add(1513);
                break;
            case 113:
                bodyPartList.add(50);
                break;
            case 114:
                bodyPartList.add(49);
                break;
            case 115:
                bodyPartList.add(51);
                break;
            case 116:
                bodyPartList.add(52);
                break;
            case 117:
                bodyPartList.add(55);
                break;
            case 118:
                bodyPartList.add(56);
                break;
            case 119:
                bodyPartList.add(61);
                break;
            case 120:
                bodyPartList.add(5000);
                bodyPartList.add(5001);
                bodyPartList.add(5002);
                break;
            case 161:
                bodyPartList.add(1100);
                break;
            case 162:
                bodyPartList.add(1101);
                break;
            case 163:
                bodyPartList.add(1102);
                break;
            case 164:
                bodyPartList.add(1103);
                break;
            case 165:
                bodyPartList.add(1104);
                break;
            case 166:
                bodyPartList.add(53);
                break;
            case 167:
                bodyPartList.add(54);
                bodyPartList.add(61);
                break;
            case 168:
                for (int id = 1400; id < 1425; id++) {
                    bodyPartList.add(id);
                }
                break;
            case 180:
                bodyPartList.add(14);
                bodyPartList.add(30);
                bodyPartList.add(38);
                break;
            case 184:
                bodyPartList.add(5100);
                break;
            case 185:
                bodyPartList.add(5102);
                break;
            case 186:
                bodyPartList.add(5103);
                break;
            case 187:
                bodyPartList.add(5104);
                break;
            case 188:
                bodyPartList.add(5101);
                break;
            case 189:
                bodyPartList.add(5105);
                break;
            case 190:
                bodyPartList.add(18);
                break;
            case 191:
                bodyPartList.add(19);
                break;
            case 192:
                bodyPartList.add(20);
                break;
            case 194:
                bodyPartList.add(1000);
                break;
            case 195:
                bodyPartList.add(1001);
                break;
            case 196:
                bodyPartList.add(1002);
                break;
            case 197:
                bodyPartList.add(1003);
                break;
            default:
                if (ItemConstants.isLongOrBigSword(itemID) || ItemConstants.isWeapon(itemID)) {
                    bodyPartList.add(11);
                    if (ItemConstants.isFan(itemID)) {
                        bodyPartList.add(5200);
                    } else {
                        bodyPartList.add(1507);
                    }
                } else {
                    log.debug("Unknown type? id = " + itemID);
                }
                break;
        }
        return bodyPartList;
    }

    private static boolean isLongOrBigSword(int nItemID) {
        int prefix = nItemID / 10000;
        return prefix % 100 == 56 || prefix % 100 == 57;
    }

    private static boolean isFan(int nItemID) {
        int prefix = nItemID / 10000;
        return prefix % 100 == 55;
    }

    public static int getWeaponType(int itemID) {
        if (itemID / 1000000 != 1) {
            return 0;
        }
        return itemID / 10000 % 100;
    }

    public static boolean isThrowingItem(int itemID) {
        return isThrowingStar(itemID) || isBullet(itemID) || isBowArrow(itemID);
    }

    public static boolean isThrowingStar(int itemID) {
        return itemID / 10000 == 207;
    }

    public static boolean isBullet(int itemID) {
        return itemID / 10000 == 233;
    }

    public static boolean isBowArrow(int itemID) {
        return itemID / 1000 == 2060;
    }

    public static boolean isFamiliar(int itemID) {
        return itemID / 10000 == 287;
    }

    public static boolean isEnhancementScroll(int scrollID) {
        return scrollID / 100 == 20493;
    }

    public static boolean isHat(int itemID) {
        return itemID / 10000 == 100;
    }

    public static boolean isWeapon(int itemID) {
        return itemID >= 1210000 && itemID < 1600000 || isSecondary(itemID);
    }

    private static boolean isSecondary(int itemID) {
        return itemID / 10000 == 135;
    }

    public static boolean isAccessory(int itemID) {
        return (itemID >= 1010000 && itemID < 1040000) || (itemID >= 1122000 && itemID < 1153000) ||
                (itemID >= 1112000 && itemID < 1113000) || (itemID >= 1670000 && itemID < 1680000);
    }

    public static boolean isTop(int itemID) {
        return itemID / 10000 == 104;
    }

    public static boolean isOverall(int itemID) {
        return itemID / 10000 == 105;
    }

    public static boolean isBottom(int itemID) {
        return itemID / 10000 == 106;
    }

    public static boolean isShoe(int itemID) {
        return itemID / 10000 == 107;
    }

    public static boolean isGlove(int itemID) {
        return itemID / 10000 == 108;
    }

    public static boolean isArmor(int itemID) {
        return !isAccessory(itemID) && !isWeapon(itemID);
    }

    public static int getTierUpChance(int id) {
        int res = 0;
        switch(id) {
            case 5062009: // Red cube
            case 5062500: // Bonus potential cube
                res = 30;
                break;
        }
        return res;
    }

    public static boolean isEquip(int id) {
        return id / 1000000 == 1;
    }

    public static boolean isClaw(int id) {
        return id / 10000 == 147;
    }

    public static boolean isBow(int id) {
        return id / 10000 == 145;
    }

    public static boolean isXBow(int id) {
        return id / 10000 == 146;
    }

    public static boolean isGun(int id) {
        return id / 10000 == 149;
    }

    public static boolean isXBowArrow(int id) {
        return id / 1000 == 2061;
    }

    public static List<Integer> getWeightedOptionsByEquip(Equip equip, boolean bonus) {
        List<Integer> res = new ArrayList<>();
        List<ItemOption> data = getOptionsByEquip(equip, bonus);
        for(ItemOption io : data) {
            for (int i = 0; i < io.getWeight(); i++) {
                res.add(io.getId());
            }
        }
        return res;
    }
    public static List<ItemOption> getOptionsByEquip(Equip equip, boolean bonus) {
        int id = equip.getItemId();
        List<ItemOption> data = ItemData.getItemOptions();
        for(ItemOption io : data) {
            // TODO: Debug data, remove once prime line logic is completed (chance for prime/lower tier pot)
            ItemGrade ioGrade = ItemGrade.getGradeByOption(io.getId());
            ItemGrade itemGrade = ItemGrade.getGradeByVal(equip.getBaseGrade());
            boolean jwz = io.hasMatchingGrade(bonus ? equip.getBonusGrade() : equip.getBaseGrade());
            boolean zwj = io.isBonus() == bonus;
            int i = 0;
            i += 3;
        }
        List<ItemOption> res = data.stream().filter(
                io -> io.getOptionType() == 0 &&
                        io.hasMatchingGrade(bonus ? equip.getBonusGrade() : equip.getBaseGrade()) && io.isBonus() == bonus)
                .collect(Collectors.toList());
        if (isWeapon(id)) {
            res.addAll(data.stream().filter(
                    io -> io.getOptionType() == 10
                            &&  io.hasMatchingGrade(bonus ? equip.getBonusGrade() : equip.getBaseGrade()) && io.isBonus() == bonus
            ).collect(Collectors.toList()));
        } else {
            res.addAll(data.stream().filter(
                    io -> io.getOptionType() == 11
                            && io.hasMatchingGrade(bonus ? equip.getBonusGrade() : equip.getBaseGrade()) && io.isBonus() == bonus)
                    .collect(Collectors.toList()));
            if (isAccessory(id)) {
                res.addAll(data.stream().filter(
                        io -> io.getOptionType() == 40
                                && io.hasMatchingGrade(bonus ? equip.getBonusGrade() : equip.getBaseGrade()) && io.isBonus() == bonus)
                        .collect(Collectors.toList()));
            } else {
                res.addAll(data.stream().filter(
                        io -> io.getOptionType() == 20
                                && io.hasMatchingGrade(bonus ? equip.getBonusGrade() : equip.getBaseGrade()) && io.isBonus() == bonus)
                        .collect(Collectors.toList()));
                if (isHat(id)) {
                    res.addAll(data.stream().filter(
                            io -> io.getOptionType() == 51
                                    && io.hasMatchingGrade(bonus ? equip.getBonusGrade() : equip.getBaseGrade()) && io.isBonus() == bonus)
                            .collect(Collectors.toList()));
                }
                if (isTop(id)) {
                    res.addAll(data.stream().filter(
                            io -> io.getOptionType() == 52
                                    && io.hasMatchingGrade(bonus ? equip.getBonusGrade() : equip.getBaseGrade()) && io.isBonus() == bonus)
                            .collect(Collectors.toList()));
                }
                if (isBottom(id)) {
                    res.addAll(data.stream().filter(
                            io -> io.getOptionType() == 53
                                    && io.hasMatchingGrade(bonus ? equip.getBonusGrade() : equip.getBaseGrade()) && io.isBonus() == bonus)
                            .collect(Collectors.toList()));
                }
                if (isOverall(id)) {
                    res.addAll(data.stream().filter(
                            io -> io.getOptionType() == 52
                                    && io.hasMatchingGrade(bonus ? equip.getBonusGrade() : equip.getBaseGrade()) && io.isBonus() == bonus)
                            .collect(Collectors.toList()));
                    res.addAll(data.stream().filter(
                            io -> io.getOptionType() == 53
                                    && io.hasMatchingGrade(bonus ? equip.getBonusGrade() : equip.getBaseGrade()) && io.isBonus() == bonus)
                            .collect(Collectors.toList()));
                }
                if (isGlove(id)) {
                    res.addAll(data.stream().filter(
                            io -> io.getOptionType() == 54
                                    && io.hasMatchingGrade(bonus ? equip.getBonusGrade() : equip.getBaseGrade()) && io.isBonus() == bonus)
                            .collect(Collectors.toList()));
                }
                if (isShoe(id)) {
                    res.addAll(data.stream().filter(
                            io -> io.getOptionType() == 55
                                    && io.hasMatchingGrade(bonus ? equip.getBonusGrade() : equip.getBaseGrade()) && io.isBonus() == bonus)
                            .collect(Collectors.toList()));
                }
            }
        }
        return res.stream().filter(io -> io.getReqLevel() <= equip.getrLevel()).collect(Collectors.toList());
    }

    public static class 類型 {

        //<editor-fold defaultstate="collapsed" desc="道具一級分類">
        public static boolean 帽子(int itemid) {
            return itemid / 10000 == 100;
        }

        public static boolean 臉飾(int itemid) {
            return itemid / 10000 == 101;
        }

        public static boolean 眼飾(int itemid) {
            return itemid / 10000 == 102;
        }

        public static boolean 耳環(int itemid) {
            return itemid / 10000 == 103;
        }

        public static boolean 上衣(int itemid) {
            return itemid / 10000 == 104;
        }

        public static boolean 套服(int itemId) {
            return itemId / 10000 == 105;
        }

        public static boolean 褲裙(int itemid) {
            return itemid / 10000 == 106;
        }

        public static boolean 鞋子(int itemid) {
            return itemid / 10000 == 107;
        }

        public static boolean 手套(int itemid) {
            return itemid / 10000 == 108;
        }

        public static boolean 盾牌(int itemid) {
            return itemid / 10000 == 109;
        }

        public static boolean 披風(int itemid) {
            return itemid / 10000 == 110;
        }

        public static boolean 戒指(int itemid) {
            return itemid / 10000 == 111;
        }

        public static boolean 墜飾(int itemid) {
            return itemid / 10000 == 112;
        }

        public static boolean 腰帶(int itemid) {
            return itemid / 10000 == 113;
        }

        public static boolean 勳章(int itemid) {
            return itemid / 10000 == 114;
        }

        public static boolean 肩飾(int itemid) {
            return itemid / 10000 == 115;
        }

        public static boolean 口袋道具(int itemid) {
            return itemid / 10000 == 116;
        }

        public static boolean 胸章(int itemId) {
            return itemId / 10000 == 118;
        }

        public static boolean 能源(final int itemid) {
            return itemid / 10000 == 119;
        }

        public static boolean 圖騰(final int itemid) {
            return itemid / 10000 == 120;
        }

        public static boolean 閃亮克魯(final int itemid) {
            return itemid / 10000 == 121;
        }

        public static boolean 靈魂射手(final int itemid) {
            return itemid / 10000 == 122;
        }

        public static boolean 魔劍(final int itemid) {
            return itemid / 10000 == 123;
        }

        public static boolean 能量劍(final int itemid) {
            return itemid / 10000 == 124;
        }

        public static boolean 幻獸棍棒(final int itemid) {
            return itemid / 10000 == 125;
        }

        public static boolean ESP限制器(final int itemid) {
            return itemid / 10000 == 126;
        }

        public static boolean 單手劍(final int itemid) {
            return itemid / 10000 == 130;
        }

        public static boolean 單手斧(final int itemid) {
            return itemid / 10000 == 131;
        }

        public static boolean 單手棍(final int itemid) {
            return itemid / 10000 == 132;
        }

        public static boolean 短劍(final int itemid) {
            return itemid / 10000 == 133;
        }

        public static boolean 雙刀(final int itemid) {
            return itemid / 10000 == 134;
        }

        public static boolean 特殊副手(final int itemid) {
            return itemid / 10000 == 135;
        }

        public static boolean 手杖(final int itemid) {
            return itemid / 10000 == 136;
        }

        public static boolean 短杖(final int itemid) {
            return itemid / 10000 == 137;
        }

        public static boolean 長杖(final int itemid) {
            return itemid / 10000 == 138;
        }

        public static boolean 雙手劍(final int itemid) {
            return itemid / 10000 == 140;
        }

        public static boolean 雙手斧(final int itemid) {
            return itemid / 10000 == 141;
        }

        public static boolean 雙手棍(final int itemid) {
            return itemid / 10000 == 142;
        }

        public static boolean 槍(final int itemid) {
            return itemid / 10000 == 143;
        }

        public static boolean 矛(final int itemid) {
            return itemid / 10000 == 144;
        }

        public static boolean 弓(final int itemid) {
            return itemid / 10000 == 145;
        }

        public static boolean 弩(final int itemid) {
            return itemid / 10000 == 146;
        }

        public static boolean 拳套(final int itemid) {
            return itemid / 10000 == 147;
        }

        public static boolean 指虎(final int itemid) {
            return itemid / 10000 == 148;
        }

        public static boolean 火槍(final int itemid) {
            return itemid / 10000 == 149;
        }

        public static boolean 雙弩槍(final int itemid) {
            return itemid / 10000 == 152;
        }

        public static boolean 加農炮(final int itemid) {
            return itemid / 10000 == 153;
        }

        public static boolean 太刀(final int itemid) {
            return itemid / 10000 == 154;
        }

        public static boolean 扇子(final int itemid) {
            return itemid / 10000 == 155;
        }

        public static boolean 琉(final int itemid) {
            return itemid / 10000 == 156;
        }

        public static boolean 璃(final int itemid) {
            return itemid / 10000 == 157;
        }

        public static boolean 引擎(int itemid) {
            return itemid / 10000 == 161;
        }

        public static boolean 手臂(int itemid) {
            return itemid / 10000 == 162;
        }

        public static boolean 腿(int itemid) {
            return itemid / 10000 == 163;
        }

        public static boolean 機殼(int itemid) {
            return itemid / 10000 == 164;
        }

        public static boolean 晶體管(int itemid) {
            return itemid / 10000 == 165;
        }

        public static boolean 機器人(int itemid) {
            return itemid / 10000 == 166;
        }

        public static boolean 心臟(int itemId) {
            return itemId / 10000 == 167;
        }

        public static boolean 寵物裝備(int itemid) {
            return itemid / 10000 >= 180 && itemid / 10000 <= 183;
        }

        public static boolean 騎寵(int itemid) {
            return itemid / 10000 == 190 || itemid / 10000 == 193;
        }

        public static boolean 馬鞍(int itemid) {
            return itemid / 10000 == 191;
        }

        public static boolean 龍面具(int itemid) {
            return itemid / 10000 == 194;
        }

        public static boolean 龍墜飾(int itemid) {
            return itemid / 10000 == 195;
        }

        public static boolean 龍之翼(int itemid) {
            return itemid / 10000 == 196;
        }

        public static boolean 龍尾巴(int itemid) {
            return itemid / 10000 == 197;
        }

        public static boolean 飛鏢(int itemid) {
            return itemid / 10000 == 207;
        }

        public static boolean 子彈(int itemid) {
            return itemid / 10000 == 233;
        }

        public static boolean 寵物(int id) {
            return id / 10000 == 500;
        }

        //</editor-fold>
        public static boolean 防具(int itemid) {
            return 帽子(itemid) || 上衣(itemid) || 套服(itemid) || 褲裙(itemid) || 鞋子(itemid) || 手套(itemid) || 披風(itemid);
        }

        public static boolean 飾品(int itemid) {
            return 臉飾(itemid) || 眼飾(itemid) || 耳環(itemid) || 戒指(itemid) || 墜飾(itemid) || 腰帶(itemid) || 勳章(itemid) || 肩飾(itemid) || 口袋道具(itemid) || 胸章(itemid) || 能源(itemid) || 圖騰(itemid);
        }

        public static boolean 副手(int itemid) {
            return 盾牌(itemid) || 雙刀(itemid) || 特殊副手(itemid) || 琉(itemid);
        }

        public static boolean 武器(int itemid) {
            return 閃亮克魯(itemid)
                    || 靈魂射手(itemid)
                    || 魔劍(itemid)
                    || 能量劍(itemid)
                    || 幻獸棍棒(itemid)
                    || ESP限制器(itemid)
                    || 單手劍(itemid)
                    || 單手斧(itemid)
                    || 單手棍(itemid)
                    || 短劍(itemid)
                    || 手杖(itemid)
                    || 短杖(itemid)
                    || 長杖(itemid)
                    || 雙手劍(itemid)
                    || 雙手斧(itemid)
                    || 雙手棍(itemid)
                    || 槍(itemid)
                    || 矛(itemid)
                    || 弓(itemid)
                    || 弩(itemid)
                    || 拳套(itemid)
                    || 指虎(itemid)
                    || 火槍(itemid)
                    || 雙弩槍(itemid)
                    || 加農炮(itemid)
                    || 太刀(itemid)
                    || 扇子(itemid)
                    || 璃(itemid);
        }

        public static boolean 機械(final int itemid) {
            return 引擎(itemid) || 手臂(itemid) || 腿(itemid) || 機殼(itemid) || 晶體管(itemid);
        }

        public static boolean 龍裝備(final int itemid) {
            return 龍面具(itemid) || 龍墜飾(itemid) || 龍之翼(itemid) || 龍尾巴(itemid);
        }

        public static boolean 可充值道具(int itemid) {
            return (飛鏢(itemid)) || (子彈(itemid));
        }

        public static boolean 單手武器(int itemid) {
            return 武器(itemid) && !雙手武器(itemid);
        }

        public static boolean 雙手武器(final int itemid) {
            return 雙手劍(itemid)
                    || 雙手斧(itemid)
                    || 雙手棍(itemid)
                    || 槍(itemid)
                    || 矛(itemid)
                    || 弓(itemid)
                    || 弩(itemid)
                    || 拳套(itemid)
                    || 指虎(itemid)
                    || 火槍(itemid)
                    || 雙弩槍(itemid)
                    || 加農炮(itemid)
                    || 太刀(itemid)
                    || 扇子(itemid)
                    || 琉(itemid)
                    || 璃(itemid);
        }

        public static boolean 物理武器(int itemid) {
            return 武器(itemid) && !魔法武器(itemid);
        }

        public static boolean 魔法武器(int itemid) {
            return 短杖(itemid) || 長杖(itemid) || 扇子(itemid) || 幻獸棍棒(itemid) || ESP限制器(itemid);
        }

        public static boolean 騎寵道具(int itemid) {
            return 騎寵(itemid) || 馬鞍(itemid);
        }

        public static int getGeder(int itemid) {
            return itemid / 1000 % 10;
        }

        public static boolean 裝備(int itemid) {
            return (itemid / 10000 >= 100) && (itemid / 10000 < 200);
        }

        public static boolean 消耗(int itemid) {
            return (itemid / 10000 >= 200) && (itemid / 10000 < 300);
        }

        public static boolean 裝飾(int itemid) {
            return (itemid / 10000 >= 300) && (itemid / 10000 < 400);
        }

        public static boolean 其他(int itemid) {
            return (itemid / 10000 >= 400) && (itemid / 10000 < 500);
        }

        public static boolean 特殊(int itemid) {
            return itemid / 10000 >= 500;
        }

        public static boolean 友誼戒指(int itemid) {
            switch (itemid) {
                case 1112800:
                case 1112801:
                case 1112802:
                case 1112810:
                case 1112811:
                case 1112816:
                case 1112817:
                    return true;
                case 1112803:
                case 1112804:
                case 1112805:
                case 1112806:
                case 1112807:
                case 1112808:
                case 1112809:
                case 1112812:
                case 1112813:
                case 1112814:
                case 1112815:
            }
            return false;
        }

        public static boolean 戀人戒指(int itemid) {
            switch (itemid) {
                case 1112001:
                case 1112002:
                case 1112003:
                case 1112005:
                case 1112006:
                case 1112007:
                case 1112012:
                case 1112015:
                case 1048000:
                case 1048001:
                case 1048002:
                    return true;
                case 1112004:
                case 1112008:
                case 1112009:
                case 1112010:
                case 1112011:
                case 1112013:
                case 1112014:
            }
            return false;
        }

        public static boolean 結婚戒指(int itemid) {
            switch (itemid) {
                case 1112300:
                case 1112301:
                case 1112302:
                case 1112303:
                case 1112304:
                case 1112305:
                case 1112306:
                case 1112307:
                case 1112308:
                case 1112309:
                case 1112310:
                case 1112311:
                case 1112315:
                case 1112316:
                case 1112317:
                case 1112318:
                case 1112319:
                case 1112320:
                case 1112803:
                case 1112806:
                case 1112807:
                case 1112808:
                case 1112809:
                    return true;
            }
            return false;
        }

        public static boolean 特效戒指(int itemid) {
            return 友誼戒指(itemid) || 戀人戒指(itemid) || 結婚戒指(itemid);
        }

        public static boolean 管理員裝備(final int itemid) {
            switch (itemid) {
                case 1002140://維澤特帽
                case 1003142://維澤特帽
                case 1003274://維澤特帽
                case 1042003://維澤特西裝
                case 1042223://維澤特西裝
                case 1062007://維澤特西褲
                case 1062140://維澤特西褲
                case 1322013://維澤特手提包
                case 1322106://維澤特手提包
                case 1002959:
                    return true;
            }
            return false;
        }

        public static boolean 城鎮傳送卷軸(final int itemid) {
            return itemid >= 2030000 && itemid < 2040000;
        }

        public static boolean 普通升級卷軸(int itemid) {
            return itemid >= 2040000 && itemid <= 2048100 && !阿斯旺卷軸(itemid);
        }

        public static boolean 阿斯旺卷軸(int itemid) {
            //return MapleItemInformationProvider.getInstance().getEquipStats(scroll.getItemId()).containsKey("tuc");
            //should add this ^ too.
            return itemid >= 2046060 && itemid <= 2046069 || itemid >= 2046141 && itemid <= 2046145 || itemid >= 2046519 && itemid <= 2046530 || itemid >= 2046701 && itemid <= 2046712;
        }

        public static boolean 提升卷(int itemid) { // 龍騎士獲得的強化牌板
            return itemid >= 2047000 && itemid < 2047310;
        }

        public static boolean 附加潛能卷軸(int itemid) {
            return itemid / 100 == 20483 && !(itemid >= 2048200 && itemid <= 2048304);
        }

        public static boolean 白醫卷軸(int itemid) {
            return itemid / 100 == 20490;
        }

        public static boolean 混沌卷軸(int itemid) {
            if (itemid >= 2049105 && itemid <= 2049110) {
                return false;
            }
            return itemid / 100 == 20491 || itemid == 2040126;
        }

        public static boolean 樂觀混沌卷軸(int itemid) {
            if (!混沌卷軸(itemid)) {
                return false;
            }
            switch (itemid) {
                case 2049122://樂觀的混卷軸50%
                case 2049129://樂觀的混卷軸 50%
                case 2049130://樂觀的混卷軸 30%
                case 2049131://樂觀的混卷軸 20%
                case 2049135://驚訝樂觀的混卷軸 20%
                case 2049136://驚訝樂觀的混卷軸 20%
                case 2049137://驚訝樂觀的混卷軸 40%
                case 2049141://珠寶戒指的樂觀的混卷軸 30%
                case 2049155://珠寶工藝樂觀的混卷軸 30%
                case 2049153://驚訝樂觀的混卷軸
                    return true;
            }
            return false;
        }

        public static boolean 飾品卷軸(int itemid) {
            return itemid / 100 == 20492;
        }

        public static boolean 裝備強化卷軸(int itemid) {
            return itemid / 100 == 20493;
        }

        public static boolean 鐵鎚(int itemid) {
            return itemid / 10000 == 247;
        }

        public static boolean 潛能卷軸(int itemid) {
            return itemid / 100 == 20494 || itemid / 100 == 20497 || itemid == 5534000;
        }

        public static boolean 回真卷軸(int itemid) {
            switch (itemid) {
                case 5064200://完美回真卡
                case 5064201://星光回真卷軸
                    return true;
                default:
                    return itemid / 100 == 20496;
            }
        }

        public static boolean 幸運日卷軸(int itemid) {
            switch (itemid) {
                case 5063100://幸運保護券(防爆+幸運)
                case 5068000://寵物專用幸運日卷軸
                    return true;
                default:
                    return itemid / 1000 == 2530;
            }
        }

        public static boolean 保護卷軸(int itemid) {
            switch (itemid) {
                case 5063100://幸運保護券(防爆+幸運)
                case 5064000://裝備保護卷軸(無法用於尊貴或12星以上)
                case 5064002://星光裝備保護卷軸(105以下的裝備且無法用於尊貴或12星以上)
                case 5064003://尊貴裝備保護卷軸(無法用於非尊貴以及尊貴7星以上)
                case 5064004://[MS特價] 裝備保護卷軸(無法用於尊貴或12星以上)
                    return true;
                default:
                    return itemid / 1000 == 2531;
            }
        }

        public static boolean 安全卷軸(int itemid) {
            switch (itemid) {
                case 5064100://安全盾牌卷軸
                case 5064101://星光安全盾牌卷軸(105以下的裝備)
                case 5068100://寵物安全盾牌卷軸
                    return true;
                default:
                    return itemid / 1000 == 2532;
            }
        }

        public static boolean 卷軸保護卡(int itemid) {
            switch (itemid) {
                case 5064300://卷軸保護卡
                case 5064301://星光卷軸保護卡(105以下的裝備)
                case 5068200://寵物卷軸保護卡
                    return true;
            }
            return false;
        }

        public static boolean 靈魂卷軸_附魔器(int itemid) {
            return itemid / 1000 == 2590;
        }

        public static boolean 靈魂寶珠(int itemid) {
            return itemid / 1000 == 2591;
        }

        public static boolean TMS特殊卷軸(int itemid) {
            return itemid / 10000 == 261;
        }

        public static boolean 特殊卷軸(final int itemid) {
            return 幸運日卷軸(itemid) || 保護卷軸(itemid) || 安全卷軸(itemid) || 卷軸保護卡(itemid);
        }

        public static boolean 特殊潛能道具(final int itemid) {
            if (itemid / 100 == 10121 && itemid % 100 >= 64 && itemid % 100 <= 74 && itemid % 100 != 65 && itemid % 100 != 66) {//恰吉
                return true;
            } else if (itemid / 10 == 112212 && (itemid % 10 >= 2 && itemid % 10 <= 6)) {//真. 楓葉之心
                return true;
            } else if (itemid >= 1122224 && itemid <= 1122245) {//心之項鍊
                return true;
            } else if (itemid / 10 == 101244) {//卡爾頓的鬍子
                return true;
            }
            return false;
        }

        public static boolean 無法潛能道具(final int itemid) {
            return false;
        }

        public static boolean 臉型(final int itemid) {
            return itemid / 10000 == 2;
        }

        public static boolean 髮型(final int itemid) {
            return itemid / 10000 == 3;
        }

        public static boolean 男臉型(int id) {
            return id / 1000 == 20;
        }

        public static boolean 女臉型(int id) {
            return id / 1000 == 21;
        }

        public static boolean 男髮型(int id) {
            if (id == 33030 || id == 33160 || id == 33590) {
                return false;
            }
            if (id / 1000 == 30 || id / 1000 == 33 || (id / 1000 == 32 && id >= 32370) || id / 1000 == 36 || (id / 1000 == 37 && id >= 37160 && id <= 37170)) {
                return true;
            }
            switch (id) {
                case 32160:
                case 32330:
                case 34740:
                    return true;
            }
            return false;
        }

        public static boolean 女髮型(int id) {
            if (id == 32160 || id == 32330 || id == 34740) {
                return false;
            }
            if (id / 1000 == 31 || id / 1000 == 34 || (id / 1000 == 32 && id < 32370) || (id / 1000 == 37 && id < 37160)) {
                return true;
            }
            switch (id) {
                case 33030:
                case 33160:
                case 33590:
                    return true;
            }
            return false;
        }

        public static boolean isFamiliar(int itemID) {
            return itemID / 10000 == 287;
        }
    }
}

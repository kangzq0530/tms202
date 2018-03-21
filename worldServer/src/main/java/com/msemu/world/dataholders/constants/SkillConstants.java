package com.msemu.world.dataholders.constants;

import com.msemu.world.model.skills.summons.enums.SummonMovementType;

/**
 * Created by Weber on 2018/3/21.
 */
public class SkillConstants {

    public static int getJobBySkill(int skillId) {
        int result = skillId / 10000;
        if (skillId / 10000 == 8000) {
            result = skillId / 100;
        }
        return result;
    }


    public static boolean isAngel(int skillId) {
        int job = getJobBySkill(skillId);
        if (JobConstants.isBeginner(job)) {
            switch (skillId % 10000) {
                case 1085: // 大天使 [等級上限：1]\n召喚被大天使祝福封印的大天使。
                case 1087: // 黑天使 [等級上限：1]\n召喚被黑天使祝福封印的大天使。
                case 1090: // 大天使 [等級上限：1]\n召喚被大天使祝福封印的大天使。
                case 1179: // 白色天使 [最高等級： 1]\n召喚出被封印的聖潔天使。
                case 86: // 大天使祝福 [等級上限：1]\n得到大天使的祝福。
                    return true;
            }
        }
        switch (skillId) {
            case 80000052: // 恶魔之息 获得恶魔的力量，攻击力和魔法攻击力增加6，HP、MP增加5%，可以和其他增益叠加。
            case 80000053: // 恶魔召唤 获得恶魔的力量，攻击力和魔法攻击力增加13，HP、MP增加10%，可以和其他增益叠加。
            case 80000054: // 恶魔契约 获得恶魔的力量，攻击力和魔法攻击力增加15，HP、MP增加20%，可以和其他增益叠加。
            case 80000086: // 戰神祝福 [等級上限：1]\n得到戰神的祝福。
            case 80001154: // 白色天使 [最高等級：1]\n召喚被白天使的祝福封印的白天使。
            case 80001262: // 戰神祝福 [等級上限：1]\n召喚戰神
            case 80001518: // 元素瑪瑙 召喚瑪瑙戒指中的#c元素瑪瑙#.
            case 80001519: // 火焰瑪瑙 召喚瑪瑙戒指中的#c火焰瑪瑙#.
            case 80001520: // 閃電瑪瑙 召喚瑪瑙戒指中的#c火焰瑪瑙#.
            case 80001521: // 冰凍瑪瑙 召喚瑪瑙戒指中的#c冰凍瑪瑙#.
            case 80001522: // 大地瑪瑙 召喚瑪瑙戒指中的#c大地瑪瑙#.
            case 80001523: // 黑暗瑪瑙 召喚瑪瑙戒指中的#c黑暗瑪瑙#.
            case 80001524: // 神聖瑪瑙 召喚瑪瑙戒指中的#c神聖瑪瑙#.
            case 80001525: // 火精靈瑪瑙 召喚瑪瑙戒指中的#c火精靈瑪瑙#.
            case 80001526: // 電子瑪瑙 召喚瑪瑙戒指中的#c電子瑪瑙#.
            case 80001527: // 水精靈瑪瑙 召喚瑪瑙戒指中的#c水精靈瑪瑙#.
            case 80001528: // 地精靈瑪瑙 召喚瑪瑙戒指中的#c地精靈瑪瑙#.
            case 80001529: // 惡魔瑪瑙 召喚瑪瑙戒指中的#c惡魔瑪瑙#.
            case 80001530: // 天使瑪瑙 召喚瑪瑙戒指中的#c天使瑪瑙#.
            case 80001715: // 元素瑪瑙
            case 80001716: // 火焰瑪瑙
            case 80001717: // 閃電瑪瑙
            case 80001718: // 冰凍瑪瑙
            case 80001719: // 大地瑪瑙
            case 80001720: // 黑暗瑪瑙
            case 80001721: // 神聖瑪瑙
            case 80001722: // 火精靈瑪瑙
            case 80001723: // 電子精靈瑪瑙
            case 80001724: // 水精靈瑪瑙
            case 80001725: // 地精靈瑪瑙
            case 80001726: // 惡魔瑪瑙
            case 80001727: // 天使瑪瑙
                return true;
        }
        return false;
    }

    public static boolean isEnergyCharge(int skillId) {
        return (skillId == 5810001 || skillId == 5100015 || skillId == 5110001 || skillId == 15100004);
    }


    public static SummonMovementType getSummonMovementType(int skillid) {
        switch (skillid) {
            case 3221014:
            case 4111007:
            case 4211007:
            case 4341006:
            case 5211014:
            case 5320011:
            case 5321003:
            case 5321004:
            case 5711001:
            case 13111024:
            case 13120007:
            case 14111010:
            case 33101008:
            case 33111003:
            case 35111002:
            case 35111005:
            case 35111011:
            case 35121003:
            case 35121009:
            case 35121010:
            case 61111002:
            case 36121002: // 能量領域：貫通
            case 36121013: // 能量領域：力場
            case 36121014: // 能量領域：支援
            case 14121003:
            case 112001007:
            case 5321052: // 滾動彩虹加農炮
            case 12111022: // 漩渦
            case 80011261: // 輪迴
            case 42111003: //鬼神召喚
            case 42100010: // 式神炎舞 - 召喚獸
                return SummonMovementType.不會移動;
            case 3111005:
            case 3211005:
            case 23111008:
            case 23111009:
            case 23111010:
            case 33101011:
            case 14000027: // 暗影蝙蝠
                return SummonMovementType.跟隨飛行隨機移動攻擊;
            case 131002015: // 迷你啾
            case 5211002: // bird - pirate
                return SummonMovementType.盤旋攻擊怪死後跟隨;
            case 32111006:
            case 35121011:
            case 2111010:
                return SummonMovementType.自由移動;
            case 1301013:
            case 2121005:
            case 2211011://雷鳴風暴
            case 2221005:
            case 2321003:
            case 12001004:
            case 12111004:
            case 14001005:
            case 35111001:
            case 35111009:
            case 35111010:
            case 12000022: // 元素:火焰 12001020 12000026
            case 12100026: // 12100020  12101028
            case 12110024: // 12111028 12110020
            case 12120007: // 12120010  12120006
            case 22171052:
            case 32001014:
            case 32100010:
            case 32110017:
            case 32120019: //死神
                return SummonMovementType.飛行跟隨;
            case 14111024: // 暗影僕從
                return SummonMovementType.跟隨移動跟隨攻擊;
            case 33001007:
            case 33001008:
            case 33001009:
            case 33001010:
            case 33001011:
            case 33001012:
            case 33001013:
            case 33001014:
            case 33001015:
                return SummonMovementType.美洲豹;
            case 5201012: // 二轉盲俠
            case 5201013: // 二轉瓦蕾莉
            case 5201014: // 二轉傑克
            case 5210015: // 三轉盲俠
            case 5210016: // 三轉瓦蕾莉
            case 5210017: // 三轉傑克
            case 5210018: // 三轉斯托納
            case 12120013: // 火焰之魂
            case 12120014:
                return SummonMovementType.移動跟隨;
        }
        if (isAngel(skillid)) {
            return SummonMovementType.飛行跟隨;
        }
        return null;
    }
}

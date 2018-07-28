/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.core.network.packets.outpacket.user;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.local.effect.LP_UserEffectLocal;
import com.msemu.core.network.packets.outpacket.user.remote.effect.LP_UserEffectRemote;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.effect.*;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.enums.UserEffectType;

import java.util.Map;

/**
 * Created by Weber on 2018/4/29.
 */
public class UserEffectPacket {

    public static OutPacket<GameClient> showEffect(boolean self, Character chr, UserEffectType effect, int[] value, String[] str, Map<Integer, Integer> itemMap, Item[] items) {
        OutPacket<GameClient> outPacket;
        if (!self && chr != null) {
            outPacket = new LP_UserEffectRemote();
            outPacket.encodeInt(chr.getId());
        } else {
            outPacket = new LP_UserEffectLocal();
        }
        outPacket.encodeByte(effect.getValue());
        switch (effect) {
            case ReservedEffectRepeat:
                new ReservedEffectRepeatUserEffect().encode(outPacket);
                break;
            case ReservedEffect:
                outPacket.encodeByte(value[0]); // boolean bFlip
                outPacket.encodeInt(value[1]); // nRange
                outPacket.encodeInt(value[2]); // nNameHeight
                outPacket.encodeString(str[0]); // sMsg
                break;
            case AvatarOriented:
                new AvatarOrientedUserEffect(str[0]).encode(outPacket);
                break;
            case AvatarOrientedRepeat:
                outPacket.encodeByte(value[0]);
                if (value[0] != 0) {
                    outPacket.encodeString(str[0]); // sItemTypeName
                    outPacket.encodeInt(value[1]); // x
                    outPacket.encodeInt(value[2]); // y
                }
                break;
            case AvatarOrientedMultipleRepeat:
                outPacket.encodeString(str[0]); // sItemTypeName
                outPacket.encodeInt(value[0]); // x
                outPacket.encodeInt(value[1]); // y
                break;
            case PlaySoundWithMuteBGM:
                new PlaySoundWithMuteBgmUserEffect(str[0]).encode(outPacket);
                break;
            // SWITCH DEFAULT IF
            case FadeInOut:
                new FadeInOutUserEffect(value[0], value[1], value[2], value[3]).encode(outPacket);
                break;
            case BlindEffect:
                new BlindUserEffect(value[0] > 0).encode(outPacket);
                break;
            case PlayExclSoundWithDownBGM:
                new PlayExclSoundWithDownBGMUserEffect(str[0], 0);
                break;
            // SWITCH結束 IF開始
            case SpeechBalloon:
                new SpeechBalloonUserEffect(value[0] > 0, value[1], value[2], str[0],
                        value[3], value[4], value[5], value[6], value[7], value[8], value[9]).encode(outPacket);
            case TextEffect:
                new TextEffectUserEffect(str[0], value[0], value[1], value[2], value[3],
                        value[4], value[5], value[6], value[7]).encode(outPacket);
                break;
            case Aiming:
                outPacket.encodeInt(0); // NPC
                outPacket.encodeInt(0);
                outPacket.encodeInt(0); // nPlateNo
                outPacket.encodeInt(0); // nRange
                outPacket.encodeInt(0); // nNameHeight
                outPacket.encodeInt(0); // bFlip
                break;
            case PickUpItem:
                new PickUpItemUserEffect(items[0]).encode(outPacket);
                break;
            case BiteAttackReceiveSuccess:
                break;
            case BiteAttackReceiveFail:
                break;
            // IF結束 SWITCH 開始
            case JobEffect:
                break;
            case LevelUp:
                break;
            case SkillUse:
                new SkillUseUserEffect(value[0]).encode(outPacket);
                break;
            case SkillUseBySummoned:
                new SkillUseBySummonedUserEffect(value[0]).encode(outPacket);
                break;
            case SkillAffected:
                int skill = value[0];
                byte skillLevel = (byte) value[1];
                outPacket.encodeInt(skill); // sItemTypeName._m_pSt
                outPacket.encodeByte(skillLevel);
                if (skill == 25121006 || skill == 31111003) {
                    outPacket.encodeInt(value[2]);
                }
                break;
            case SkillAffectedEx:
                new SkillAffectedExUserEffect(value[0], (byte) value[1], value[2], value[3]).encode(outPacket);
                break;
            case SkillSpecialAffected:
                new SkillSpecialAffectedUserEffect(value[0], (byte) value[1]).encode(outPacket);
                break;
            case SkillAffectedSelect:
                new SkillAffectedSelectUserEffect(value[0], value[1], value[2], (byte) value[3], value[4] > 0).encode(outPacket);
                break;
            case SkillSpecial:
                new SkillSpecialUserEffect(value[0], value[1], value[2], value[3]
                        , value[1] > 0, value[2] > 0, value[3], value[4], value[5], value[6]).encode(outPacket);
                break;
            case SkillPreLoopEnd:
                new SkillPreLoopEndUserEffect(value[0], value[1]);
                break;
            case Resist:
                break;
            case Quest:
                outPacket.encodeByte(itemMap.size());
                int v3 = 0;
                if (itemMap.size() <= v3) {
                    outPacket.encodeString("");
                    outPacket.encodeInt(0);
                }
                itemMap.entrySet().forEach((item) -> {
                    outPacket.encodeInt(item.getKey());
                    outPacket.encodeInt(item.getValue());
                });
                break;
            case LotteryUIResult:
                outPacket.encodeByte(0);
                outPacket.encodeByte(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                break;
            case Pet:
                new PetUserEffect(value[0], value[1]).encode(outPacket);
                break;
            case ProtectOnDieItemUse:
                outPacket.encodeInt(value[0]); // 1 = 護身符, 2 = 紡織輪, 4 = 戰鬥機器人
                outPacket.encodeByte(value[1]); // 剩餘次數
                outPacket.encodeByte(value[2]);
                switch (value[0]) {
                    case 1:
                    case 2:
                        break;
                    default:
                        outPacket.encodeInt(value[3]); // ItemId
                }
                break;
            case UNK_D:
                outPacket.encodeInt(0);
                break;
            case UNK_27:
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeByte(0);
                break;
            case PlayPortalSE:
            case JobChanged:
            case QuestComplete:
                break;
            case ActQuestComplete:
                new ActQuestCompleteUserEffect(value[0]).encode(outPacket);
                break;
            case IncDecHPEffect:
                new IncDecHPEffectUserEffect(value[0]).encode(outPacket);
                break;
            case SoulStoneUse:
                new SoulStoneUseUserEffect(value[0]).encode(outPacket);
                break;
            case BattlePvPIncDecHp:
                new BattlePvPIncDecHp(value[0], str.length > 0 ? str[0] : "").encode(outPacket);
                break;
            case IncDecHPEffectEX:
                new IncDecHPEffectEXUserEffect(value[0], value.length > 1 && value[1] > 0).encode(outPacket);
                break;
            case BuffItemEffect:
                new BuffItemEffectUserEffect(value[0]).encode(outPacket);
                break;
            case SquibEffect:
                new SquibEffectUserEffect(str[0]).encode(outPacket);
                break;
            case LotteryUse:
                new LotteryUseUserEffect(value[0], str.length > 0 ? str[0] : null).encode(outPacket);
                break;
            case MonsterBookCardGet:
                break;
            case ItemLevelUp:
                break;
            case ItemMaker:
                new ItemMakerUserEffect(value[0]).encode(outPacket);
                break;
            case FieldExpItemConsumed:
                new FieldExpItemConsumedUserEffect(value[0]).encode(outPacket);
                break;
            case ExpItemConsumed:
                break;
            case UNK_23:
                break;
            case UpgradeTombItemUse:
                outPacket.encodeByte(value[0]);
                break;
            case BattlefieldItemUse:
                //if (get_field() && get_field()->m_nType == 19)
                outPacket.encodeString(str[0]);
                break;
            case IncDecHPRegenEffect:
                outPacket.encodeInt(value[0]);
                break;
            case UNK_28:
            case UNK_29:
            case UNK_2A:
            case UNK_2B:
                break;
            case IncubatorUse:
                outPacket.encodeInt(value[0]);
                outPacket.encodeString(str[0]);
                break;
            case EffectUOL:
                outPacket.encodeString(str[0]);
                outPacket.encodeByte((byte) value[0]);
                outPacket.encodeInt(value[1]);
                outPacket.encodeInt(value[2]);
                if (value[2] == 2) {
                    outPacket.encodeInt(value[3]);
                }
                break;
            case PvPRage:
                outPacket.encodeInt(0);
                break;
            case PvPChampion:
                outPacket.encodeInt(0);
                break;
            case PvPGradeUp:
                break;
            case PvPRevive:
                break;
            case MobSkillHit:
                outPacket.encodeInt(value[0]);
                outPacket.encodeInt(value[1]);
                break;
            case AswanSiegeAttack:
                outPacket.encodeByte(value[0]);
                break;
            case BossShieldCount:
                outPacket.encodeInt(value[0]);
                outPacket.encodeInt(value[1]);
                break;
            case ResetOnStateForOnOffSkill:
                break;
            case JewelCraft:
                outPacket.encodeByte(value[0]);
                switch (value[0]) {
                    case 5:
                        break;
                    case 0:
                    case 2:
                    case 3:
                        outPacket.encodeInt(value[1]);
                        break;
                    case 1:
                    case 4:
                        outPacket.encodeInt(value[1]);
                        break;
                }
                break;
            case ConsumeEffect:
                outPacket.encodeInt(value[0]);
                break;
            case PetBuff:
                break;
            case LeftMonsterNumber:
                outPacket.encodeInt(0);
                break;
            case RobbinsBomb:
                boolean res3f = false;
                outPacket.encodeByte(res3f);
                if (!res3f) {
                    outPacket.encodeInt(0);
                    outPacket.encodeByte(0);
                }
                break;
            case SkillMode:
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                break;
            case Point:
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                break;
            case IncDecHPEffectDelayed:
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                break;
            case Lightness:
                boolean v1 = false;
                outPacket.encodeByte(v1);
                if (v1) {
                    outPacket.encodeInt(0);
                }
                break;
            case ActionSetUsed:
                outPacket.encodeShort(value[0]);
                outPacket.encodeInt(value[1]);
                outPacket.encodeByte(value[2]);
                outPacket.encodeByte(value[3]);
                outPacket.encodeByte(value[4]);
                break;
            case UNK_47:
                //if unc {
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                //}
                break;
            case UNK_48:
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                break;
            case UNK_2C:
                outPacket.encodeInt(0);
                break;
            case UNK_2D:
                break;
        }

        return outPacket;
    }

    public static OutPacket<GameClient> showLevelupEffect(Character chr) {
        return showEffect(chr == null, chr, UserEffectType.LevelUp, null, null, null, null);
    }

    public static OutPacket<GameClient> showDiceEffect(int skillid, int effectid, int effectid2, int level) {
        return showDiceEffect(null, skillid, effectid, effectid2, level);
    }

    public static OutPacket<GameClient> showDiceEffect(Character chr, int skillid, int effectid, int effectid2, int level) {
        return showEffect(chr == null, chr, UserEffectType.SkillAffectedSelect, new int[]{effectid, effectid2, skillid, level, 0}, null, null, null);
    }

    public static OutPacket<GameClient> getShowItemGain(Map<Integer, Integer> items) {
        return showEffect(true, null, UserEffectType.Quest, null, null, items, null);
    }

    public static OutPacket<GameClient> showPetLevelUp(byte index) {
        return showPetLevelUp(null, index);
    }

    public static OutPacket<GameClient> showPetLevelUp(Character chr, byte index) {
        return showEffect(chr == null, chr, UserEffectType.Pet, new int[]{0, index, 0}, null, null, null);
    }

    public static OutPacket<GameClient> showBlackBlessingEffect(Character chr, int value) {
        return showEffect(chr == null, chr, UserEffectType.SkillSpecial, new int[]{value, 0, 0, 0, 0, 0, 0, 0}, null, null, null);
    }

    public static OutPacket<GameClient> useAmulet(int amuletType, byte timesleft, byte daysleft, int itemId) {
        return showEffect(true, null, UserEffectType.ProtectOnDieItemUse, new int[]{amuletType, timesleft, daysleft, itemId}, null, null, null);
    }

    public static OutPacket<GameClient> Mulung_DojoUp() {
        return showEffect(true, null, UserEffectType.PlayPortalSE, null, null, null, null);
    }

    public static OutPacket<GameClient> showJobChangeEffect(Character chr) {
        return showEffect(chr == null, chr, UserEffectType.JobChanged, null, null, null, null);
    }

    public static OutPacket<GameClient> showQuetCompleteEffect() {
        return showQuetCompleteEffect(null);
    }

    public static OutPacket<GameClient> showQuetCompleteEffect(Character chr) {
        return showEffect(chr == null, chr, UserEffectType.QuestComplete, null, null, null, null);
    }

    public static OutPacket<GameClient> showHealed(int amount) {
        return UserEffectPacket.showHealed(null, amount);
    }

    public static OutPacket<GameClient> showHealed(Character chr, int amount) {
        return showEffect(chr == null, chr, UserEffectType.IncDecHPEffectEX, new int[]{amount}, null, null, null);
    }

    public static OutPacket<GameClient> showMonsterBookEffect() {
        return showEffect(true, null, UserEffectType.MonsterBookCardGet, null, null, null, null);
    }

    public static OutPacket<GameClient> showRewardItemAnimation(int itemId, String effect) {
        return showRewardItemAnimation(itemId, effect, null);
    }

    public static OutPacket<GameClient> showRewardItemAnimation(int itemId, String effect, Character chr) {
        return showEffect(chr == null, chr, UserEffectType.LotteryUse, new int[]{itemId}, new String[]{effect}, null, null);
    }

    public static OutPacket<GameClient> showItemLevelupEffect() {
        return showItemLevelupEffect(null);
    }

    public static OutPacket<GameClient> showItemLevelupEffect(Character chr) {
        return showEffect(chr == null, chr, UserEffectType.ItemLevelUp, null, null, null, null);
    }

    public static OutPacket<GameClient> ItemMaker_Success() {
        return ItemMaker_Success(null);
    }

    public static OutPacket<GameClient> ItemMaker_Success(Character chr) {
        return showEffect(chr == null, chr, UserEffectType.ItemMaker, null, null, null, null);
    }

    public static OutPacket<GameClient> showDodgeChanceEffect() {
        return showEffect(true, null, UserEffectType.ExpItemConsumed, null, null, null, null);
    }

    public static OutPacket<GameClient> showWZEffect(String data) {
        return showEffect(true, null, UserEffectType.ReservedEffect, new int[]{0, 0, 0}, new String[]{data}, null, null);
    }

    public static OutPacket<GameClient> showWZEffectNew(String data) {
        return showEffect(true, null, UserEffectType.AvatarOriented, null, new String[]{data}, null, null);
    }

    public static OutPacket<GameClient> playSoundEffect(String data) {
        return showEffect(true, null, UserEffectType.PlaySoundWithMuteBGM, null, new String[]{data}, null, null);
    }

    public static OutPacket<GameClient> playVoiceEffect(String data) {
        return showEffect(true, null, UserEffectType.PlayExclSoundWithDownBGM, null, new String[]{data}, null, null);
    }

    public static OutPacket<GameClient> showCashItemEffect(int itemId) {
        return showEffect(true, null, UserEffectType.SoulStoneUse, new int[]{itemId}, null, null, null);
    }

    public static OutPacket<GameClient> showChampionEffect() {
        return UserEffectPacket.showChampionEffect(null);
    }

    public static OutPacket<GameClient> showChampionEffect(Character chr) {
        return showEffect(true, null, UserEffectType.PvPChampion, new int[]{0x7530}, null, null, null);
    }

    public static OutPacket<GameClient> showCraftingEffect(String effect, byte direction, int time, int mode) {
        return UserEffectPacket.showCraftingEffect(null, effect, direction, time, mode);
    }

    public static OutPacket<GameClient> showCraftingEffect(Character chr, String effect, byte direction, int time, int mode) {
        return showEffect(true, null, UserEffectType.EffectUOL, new int[]{direction, time, mode, 0}, new String[]{effect}, null, null);
    }

    public static OutPacket<GameClient> showWeirdEffect(String effect, int itemId) {
        return showWeirdEffect(null, effect, itemId);
    }

    public static OutPacket<GameClient> showWeirdEffect(Character chr, String effect, int itemId) {
        return showEffect(true, null, UserEffectType.EffectUOL, new int[]{1, 0, 2, itemId}, new String[]{effect}, null, null);
    }

    public static OutPacket<GameClient> showBlackBGEffect(int value, int value2, int value3, byte value4) {
        return showEffect(true, null, UserEffectType.FadeInOut, new int[]{value, value2, value3, value4}, null, null, null);
    }

    public static OutPacket<GameClient> unsealBox(int reward) {
        return showEffect(true, null, UserEffectType.MobSkillHit, new int[]{reward, 1}, null, null, null);
    }

    public static OutPacket<GameClient> showDarkEffect(boolean dark) {
        return showEffect(true, null, UserEffectType.BlindEffect, new int[]{dark ? 1 : 0}, null, null, null);
    }

    public static OutPacket<GameClient> showRechargeEffect() {
        return showEffect(true, null, UserEffectType.ResetOnStateForOnOffSkill, null, null, null, null);
    }

    public static OutPacket<GameClient> showWZEffect3(String data, int[] value) {
        return showEffect(true, null, UserEffectType.ReservedEffectRepeat, value, new String[]{data}, null, null);
    }

    public static OutPacket<GameClient> showFireEffect(Character chr) {
        return showEffect(chr == null, chr, UserEffectType.SkillPreLoopEnd, null, null, null, null);
    }

    public static OutPacket<GameClient> showItemTopMsgEffect(Item item) {
        return showEffect(true, null, UserEffectType.PickUpItem, null, null, null, new Item[]{item});
    }

    public static OutPacket<GameClient> showBuffEffect(boolean self, Character chr, int skillid, UserEffectType effect, int playerLevel, int skillLevel) {
        return showBuffEffect(self, chr, skillid, effect, playerLevel, skillLevel, (byte) 3);
    }

    public static OutPacket<GameClient> showBuffEffect(boolean self, Character chr, int skillid, UserEffectType effect, int playerLevel, int skillLevel, byte direction) {
        OutPacket<GameClient> outPacket;
        if (!self && chr != null) {
            outPacket = new LP_UserEffectRemote();
            outPacket.encodeInt(chr.getId());
        } else {
            outPacket = new LP_UserEffectLocal();
        }
        switch (effect) {
            case SkillUse:
            case SkillUseBySummoned:
                break;
            default:
                System.err.println("未處理的Buff效果" + effect.name());
                return outPacket;
        }

        outPacket.encodeByte(effect.getValue());

        if (effect == UserEffectType.SkillUseBySummoned) {
            outPacket.encodeInt(0);
        }
        outPacket.encodeInt(skillid);
        outPacket.encodeByte(playerLevel);

        if (skillid == 51100006) {
            return outPacket;
        }

        outPacket.encodeByte(skillLevel);

        if (skillid == 22160000) { // 龍神的護佑
            outPacket.encodeByte(0);
        } else if (skillid == 1320016) { // 轉生
            outPacket.encodeByte(0); // boolean
        } else {
            if (skillid == 4331006) { // 隱‧鎖鏈地獄
                outPacket.encodeByte(0);
                outPacket.encodeInt(0);
            }
            if (skillid == 3211010 || skillid == 3111010) { // 飛影位移
                outPacket.encodeByte(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                return outPacket;
            }
            if (skillid == 35001006) { // 火箭推進器
                return outPacket;
            }
            if (skillid == 91001020 || skillid == 91001021 || skillid == 91001017 || skillid == 91001018) { // 起來吧,勇士 || 我還想再跑 || 前線召喚 || 公會定期聚會
                return outPacket;
            }
            if (skillid == 33111007) { // 狂獸附體
                return outPacket;
            }
            if (skillid == 30001062) { // 獵人的呼喚
                outPacket.encodeByte(0);
                outPacket.encodeShort(0);
                outPacket.encodeShort(0);
                return outPacket;
            }
            if (skillid == 30001061) { // 捕獲
                outPacket.encodeByte(0);
                outPacket.encodeShort(0);
                outPacket.encodeShort(0);
                return outPacket;
            }
            if (skillid == 60001218 || skillid == 60011218) { // 縱向連接 || 魔法起重機
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                return outPacket;
            }
            if (skillid == 20041222 || skillid == 15001021 || skillid == 20051284 || skillid == 5081021) { // 星光順移 || 閃光 || 縮地 || 縱步突打 
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                return outPacket;
            }
            if (skillid == 4221052 || skillid == 65121052) { // IDA_FUC 暗影霧殺 || 超級超新星
                outPacket.encodeInt(0);
                outPacket.encodeInt(0);
                return outPacket;
            }
            if (skillid == 12001027 || skillid == 12001028) { // 火步行
                return outPacket;
            }
            if (skillid == 80011068) { // 紫扇仰波‧烈
                return outPacket;
            }
            if (skillid == 80001132) { // 獵殺殭屍
                outPacket.encodeByte(0);
                return outPacket;
            }
        }

        return outPacket;
    }
}



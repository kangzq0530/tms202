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

package com.msemu.world.client.character.stats;

import com.msemu.commons.data.enums.SkillStat;
import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.data.templates.SetInfo;
import com.msemu.commons.data.templates.SetItemInfo;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.world.client.character.AvatarData;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.constants.GameConstants;
import com.msemu.world.constants.ItemConstants;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.data.ItemData;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.BodyPart;
import com.msemu.world.enums.Stat;
import com.msemu.world.enums.WeaponType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/5/7.
 */
public class CharacterLocalStat {

    @Getter
    private Character character;
    @Getter
    private int pad, mad;
    @Getter
    private int str, dex, inte, luk;
    @Getter
    private int maxHp, maxMp;
    @Getter
    private int shouldHealHp, shouldHealMp;
    @Getter
    private int maxDamage, pvpMaxDamage, minDamage, pvpMinDamage;
    @Getter
    private int mastery;
    @Getter
    private int cirticalRate;
    @Getter
    private CalcDamage calcDamage;

    public CharacterLocalStat(Character character) {
        this.character = character;
        this.calcDamage = new CalcDamage(character);
    }


    public void reCalculateLocalStat() {
        AvatarData ad = character.getAvatarData();
        CharacterStat cs = ad.getCharacterStat();
        TemporaryStatManager tsm = character.getTemporaryStatManager();

        int job = character.getJob();
        int baseMaxHp = cs.getMaxHp();
        int baseMaxMp = cs.getMaxMp();
        int incMaxHp = 0, incMaxMp = 0, incMaxHpR = 0, incMaxMpR = 0;

        int incStr = 0, incDex = 0, incInt = 0, incLuk = 0, incStatR = 0;

        int incPad = 0, incMad = 0, incPadR = 0, incMadR = 0, incMastery = 0;

        int pdR = 0, damR = 0;

        int incCritical = 0;

        int incAcc = 0, incSpeed = 0, incJump = 0;


        List<Integer> equippedItemIDs = new ArrayList<>();
        List<Integer> setItemIDs = new ArrayList<>();
        for (Item item : character.getEquippedInventory().getItems()) {
            equippedItemIDs.add(item.getItemId());
            Equip equip = (Equip) item;
            incMaxHp += MapleJob.is惡魔復仇者(job) ? equip.getIMaxHp() / 2 : equip.getIMaxHp();
            incMaxMp += equip.getIMaxMp();
            incMaxHpR += equip.getIMaxHpR();
            incMaxMpR += equip.getIMaxMpR();
            incStr += equip.getIStr();
            incDex += equip.getIDex();
            incInt += equip.getIInt();
            incLuk += equip.getILuk();
            incPad += equip.getIPad();
            incMad += equip.getIMad();
            incAcc += equip.getIAcc();
            incJump += equip.getIJump();
            incSpeed = equip.getISpeed();

        }

        for (Integer setItemID : setItemIDs) {
            SetItemInfo setItemInfo = ItemData.getInstance().getSetItemInfo(setItemID);
            int matchCount = (int) setItemInfo.getItemIDs().stream()
                    .filter(equippedItemIDs::contains).count();
            if (matchCount < setItemInfo.getCompleteCount()) {
                SetInfo effect = setItemInfo.getEffects().get(matchCount);
                incMaxHp += effect.getIncMHP();
                incMaxMp += effect.getIncMMP();
                incMaxHpR += effect.getIncMHPr();
                incMaxMpR += effect.getIncMMPr();
                incStr += effect.getIncSTR() + effect.getIncAllStat();
                incDex += effect.getIncDEX() + effect.getIncAllStat();
                incInt += effect.getIncINT() + effect.getIncAllStat();
                incLuk += effect.getIncLUK() + effect.getIncAllStat();
                incPad += effect.getIncPAD();
                incMad += effect.getIncMAD();
                incAcc += effect.getIncACC();
                incJump += effect.getIncJump();
                incSpeed += effect.getIncSpeed();

            }
        }


        for (Map.Entry<CharacterTemporaryStat, List<Option>> entry : tsm.getCurrentStats().entrySet()) {
            switch (entry.getKey()) {
                case MaxHP:
                case HowlingEvasion:
                case IndieMHPR:
                    for (Option option : entry.getValue()) {
                        incMaxHpR += option.nOption;
                    }
                    break;
                case IndieMMPR:
                case MaxMP:
                    for (Option option : entry.getValue()) {
                        incMaxMpR += option.nOption;
                    }
                    break;
                case IndieMHP:
                    for (Option option : entry.getValue()) {
                        incMaxHp += option.nOption;
                    }
                    break;
                case IndieMMP:
                    for (Option option : entry.getValue()) {
                        incMaxMp += option.nOption;
                    }
                    break;
                case ACC:
                case IndieACC:
                    for (Option option : entry.getValue()) {
                        incAcc += option.nOption;
                    }
                    break;
                case Speed:
                case IndieSpeed:
                    for (Option option : entry.getValue()) {
                        incAcc += option.nOption;
                    }
                    break;
                case Jump:
                case IndieJump:
                    for (Option option : entry.getValue()) {
                        incJump += option.nOption;
                    }
                    break;
                case IndiePAD:
                case PAD:
                    for (Option option : entry.getValue()) {
                        incPad += option.nValue;
                    }
                    break;
                case IndieMAD:
                case MAD:
                    for (Option option : entry.getValue()) {
                        incMad += option.nValue;
                    }
                    break;
                case IndiePADR:
                    for (Option option : entry.getValue()) {
                        incPadR += option.nValue;
                    }
                    break;
                case IndieMADR:
                    for (Option option : entry.getValue()) {
                        incMadR += option.nValue;
                    }
                    break;
                case BasicStatUp:
                    for (Option option : entry.getValue()) {
                        incStatR += option.nOption;
                    }
                    break;
                default:
                    break;
            }
        }

        Item weapon = character.getEquippedInventory().getFirstItemByBodyPart(BodyPart.WEAPON);
        WeaponType weaponType = weapon != null ? ItemConstants.類型.武器類型(weapon.getItemId()) : null;

        for (Skill skill : getCharacter().getSkills()) {
            if (skill.getCurrentLevel() == 0)
                continue;
            SkillInfo si = SkillData.getInstance().getSkillInfoById(skill.getSkillId());

            incMaxHp += getCharacter().getLevel() * si.getValue(SkillStat.lv2mhp, skill.getCurrentLevel());
            incMaxMp += getCharacter().getLevel() * si.getValue(SkillStat.lv2mmp, skill.getCurrentLevel());
            incMaxHpR += si.getValue(SkillStat.mhpR, skill.getCurrentLevel());
            incMaxMpR += si.getValue(SkillStat.mmpR, skill.getCurrentLevel());
            incPad += si.getValue(SkillStat.pad, skill.getCurrentLevel());
            incMad += si.getValue(SkillStat.mad, skill.getCurrentLevel());
            incPadR += si.getValue(SkillStat.padR, skill.getCurrentLevel());
            incMadR += si.getValue(SkillStat.madR, skill.getCurrentLevel());
            incStr += si.getValue(SkillStat.strX, skill.getCurrentLevel());
            incDex += si.getValue(SkillStat.dexX, skill.getCurrentLevel());
            incInt += si.getValue(SkillStat.intX, skill.getCurrentLevel());
            incLuk += si.getValue(SkillStat.lukX, skill.getCurrentLevel());
            incMastery += si.getValue(SkillStat.mastery, skill.getCurrentLevel());
            pdR += si.getValue(SkillStat.pdR, skill.getCurrentLevel());

            if (weapon != null) {
                int wtVal = (weapon.getItemId() / 10000) % 100;
                if (si.getPsdWT().containsKey(wtVal)) {
                    for (Map.Entry<SkillStat, Integer> entry : si.getPsdWT().get(wtVal).entrySet()) {
                        switch (entry.getKey()) {
                            case damR:
                                damR += entry.getValue();
                                break;
                        }
                    }
                }
            }
        }


        this.maxHp = (baseMaxHp + incMaxHp) * (100 + incMaxHpR) / 100;
        this.maxMp = (baseMaxMp + incMaxMp) * (100 + incMaxMpR) / 100;

        this.str = (cs.getStr()) * (100 + incStatR) / 100 + incStr;
        this.dex = (cs.getDex()) * (100 + incStatR) / 100 + incDex;
        this.inte = (cs.getInte() + incInt) * (100 + incStatR) / 100;
        this.luk = (cs.getLuk() + incLuk) * (100 + incStatR) / 100;

        this.pad = (incPad) * (100 + incPadR) / 100;
        this.mad = (incMad) * (100 + incMad) / 100;
        this.mastery = 0;

        this.cirticalRate = 5 + incCritical;

        this.shouldHealHp = 10;
        this.shouldHealMp = 3;

        this.mastery = incMastery;

        if (getCharacter().getPortableChairID() > 0) {
            ItemTemplate chairInfo = ItemData.getInstance().getItemInfo(getCharacter().getPortableChairID());
            if (chairInfo != null) {
                this.shouldHealHp += chairInfo.getRecoveryHP();
                this.shouldHealHp += chairInfo.getRecoveryMP();
            }
        }

        if (character.getStat(Stat.HP) > character.getCurrentMaxHp()) {
            character.setStat(Stat.HP, character.getCurrentMaxHp());
        }
        if (character.getStat(Stat.MP) > character.getCurrentMaxMp()) {
            character.setStat(Stat.MP, character.getCurrentMaxMp());
        }

        int maxBaseWeaponDamage = 0, pvpWeaponDamage = 0;
        int minBaseWeaponDamage = 0;


        if (weaponType != null) {
            double maxMastery = 0.95;

            if (job == 14000 || job == 14200 || job == 14210 || job == 14211 || job == 14212) {
                maxMastery = 0.99;
            }
            // 熟練技能有限制武器，需要計算技能適用的武器
            double finalMastery = Math.min((this.mastery + weaponType.getBaseMastery()) / 100.0, maxMastery);

            maxBaseWeaponDamage = calculateBaseWeaponDamage(weaponType, pad, mad, 0, false, 0);
            minBaseWeaponDamage = (int) Math.floor(finalMastery * maxBaseWeaponDamage + 0.5);

            pvpWeaponDamage = calculateBaseWeaponDamage(weaponType, pad, mad, 0, true, 0);
//            weaponDamage += (weaponDamage * ((pdR + damR) / 100.0));
//            pvpWeaponDamage += (weaponDamage * ((pdR + damR) / 100.0));
//            weaponMindamage += (weaponMindamage * ((pdR + damR) / 100.0));
        }


        this.maxDamage = maxBaseWeaponDamage;
        this.pvpMaxDamage = pvpWeaponDamage;
        this.minDamage = minBaseWeaponDamage;


    }


    private int calculateBaseWeaponDamage(WeaponType weaponType, int pad, int mad, int skillID, boolean pvp, int setBaseDamage) {

        if (setBaseDamage > 0)
            return setBaseDamage;

        CharacterStat cs = character.getAvatarData().getCharacterStat();

        final int jobId = cs.getJob();
        final int jobCate = cs.getSubJob();

        int p1 = 0;
        int p2 = 0;
        int p3 = 0;
        int ad = 0;
        double k = 0;

        if (MapleJob.isBeginner(jobId)) {
            return GameConstants.calcBaseDamage(getStr(), getDex(), 0, pad, GameConstants.getJobDamageConst(jobId) + weaponType.getMaxDamageMultiplier(), pvp);
        } else {
            if (jobCate != 2 || MapleJob.is凱撒(jobId) || MapleJob.is夜光(jobId)) {
                ad = pad;
                switch (weaponType) {
                    case 閃亮克魯:
                        ad = mad;
                        p1 = getInte();
                        p2 = getLuk();
                        break;
                    case 靈魂射手:
                    case 弓:
                    case 弩:
                    case 火槍:
                    case 雙弩槍:

                        p1 = getDex();
                        p2 = getStr();
                        break;
                    case 能量劍:
                        p1 = getStr();
                        p2 = getDex();
                        p3 = getLuk();
                        break;
                    case ESP限制器:
                        ad = mad;
                        p1 = getInte();
                        p2 = getLuk();
                        break;
                    case 單手劍:
                    case 單手斧:
                    case 單手棍:
                    case 雙手劍:
                    case 璃:
                    case 雙手斧:
                    case 雙手棍:
                    case 槍:
                    case 矛:
                    case 琉:
                    case 指虎:
                        p1 = getStr();
                        p2 = getDex();
                        break;
                    case 短劍:
                        p1 = getLuk();
                        p2 = getDex();
                        p3 = getStr();
                        break;
                    case 手杖:
                        // isStealableSkill -> Multiplier -> 1.2 otherwise 1.2;
                        p1 = getLuk();
                        p2 = getDex();
                        break;

                    case 拳套:
                        p1 = getLuk();
                        p2 = getDex();
                        break;
                    case 加農炮:
                        p1 = Math.max(getDex(), getInte());
                        p2 = Math.min(getDex(), getInte());
                        break;
                    case 重拳槍:
                        p1 = getStr();
                        p2 = getDex();
                        break;
                    default:
                        return 0;
                }

                k = GameConstants.getJobDamageConst(jobId) + weaponType.getMaxDamageMultiplier();
                if (weaponType.equals(WeaponType.能量劍)) {
                    return GameConstants.calcHybridBaseDamage(p1, p2, p3, 0, ad, k, pvp);
                }
                return GameConstants.calcBaseDamage(getStr(), getDex(), 0, ad, k, pvp);

            } else {
                k = GameConstants.getJobDamageConst(jobId) + weaponType.getMaxDamageMultiplier();
                p1 = getInte();
                p2 = getLuk();
                return GameConstants.calcBaseDamage(p1, p2, 0, mad, k, pvp);
            }
        }
    }
}

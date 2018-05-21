package com.msemu.world.client.character.stats;

import com.msemu.commons.data.enums.SkillStat;
import com.msemu.commons.data.templates.skill.SkillInfo;
import com.msemu.commons.utils.types.CRand32;
import com.msemu.commons.utils.types.Tuple;
import com.msemu.world.client.character.AttackInfo;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.constants.GameConstants;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.ChatMsgType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/5/21.
 */
public class CalcDamage {

    private Character character;
    private CRand32 rndGenForCharacter;
    //CRand32 rndForCheckDamageMiss;//not implement yet
    //CRand32 rndGenForMob;//not implement yet
    //int invalidCount;

    private int numRand = 11;//A number of random number for calculate damage

    public CalcDamage(Character character) {
        this.character = character;
        this.rndGenForCharacter = new CRand32();
    }

    public double get_range(double randomNum, double max, double min) {
        double v3 = max;
        double v4 = min;
        if (max > min) {
            v3 = min;
            v4 = max;
            return (randomNum % 10000000) * (v4 - v3) / 9999999.0 + v3;
        }
        if (max != min) {
            return (randomNum % 10000000) * (v4 - v3) / 9999999.0 + v3;
        }
        return max;
    }

    public List<Tuple<Integer, Boolean>> PDamageForPvM(AttackInfo attack) {
        final CharacterLocalStat localStat = character.getCharacterLocalStat();
        final CharacterStat cs = character.getAvatarData().getCharacterStat();
        final List<Tuple<Integer, Boolean>> realDamageList = new ArrayList<>();

        attack.getMobAttackInfo().forEach(mbi -> {
            Mob mob = character.getField().getMobByObjectId(mbi.getObjectID());
            Skill skill = character.getSkill(attack.getSkillId());
            SkillInfo si = SkillData.getInstance().getSkillInfoById(attack.getSkillId());

            int index = 0;
            long rand[] = new long[numRand];
            for (int i = 0; i < numRand; i++) {
                rand[i] = rndGenForCharacter.random();
            }
            for (Long damage : mbi.getDamages()) {
                double realDamage = 0.0;
                index++;

                long unkRand1 = rand[index++ % numRand];
                int maxDamage = localStat.getMaxDamage();
                int minDamage = localStat.getMinDamage();
                int mastery = localStat.getMastery();


                if(si != null) {
                    maxDamage = (int) (maxDamage + (maxDamage * (si.getValue(SkillStat.damR, skill.getCurrentLevel()) / 100.0)));
                    minDamage = (int) (minDamage + (minDamage * (si.getValue(SkillStat.damR, skill.getCurrentLevel()) / 100.0)));
                }

                double adjustedRandomDamage = GameConstants.getRand(rand[index++], minDamage, maxDamage);
                realDamage += adjustedRandomDamage;


                double totoal_pdam_rate = 0.1;

                realDamage = realDamage * totoal_pdam_rate + realDamage;

                /*
                v300 = thisa[3].m_mSetItemMobDamR._m_uAutoGrowLimit == 0;
          nReason = v472;
          if ( !v300 )
          {
            LODWORD(nProb) = thisa[3].m_mSetItemMobDamR._m_uAutoGrowEvery128;
            nReason = v472 - SLODWORD(nProb) * v472 / 100.0;
          }
                 */



                int cr = 0;

                if (si != null && skill != null) {
                    realDamage = realDamage * (double) si.getValue(SkillStat.damage, skill.getCurrentLevel()) / 100.0;



                    cr = si.getValue(SkillStat.cr, skill.getCurrentLevel());
                }

                boolean criticaled = false;


                if (GameConstants.getRand(rand[index++ % numRand], 100, 0) < (localStat.getCirticalRate() + cr)) {
                    criticaled = true;
                }
                character.chatMessage(ChatMsgType.NOTICE_2, String.format("[CalcDamage] Damage :%f Critical :%s Diff :%f", realDamage, String.valueOf(criticaled), Math.abs(damage - adjustedRandomDamage) / 100.0));

            }

        });

        return realDamageList;
    }

    public void setSeed(long seed1, long seed2, long seed3) {
        rndGenForCharacter.seed(seed1, seed2, seed3);
    }
}

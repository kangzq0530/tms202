package com.msemu.world.client.field.lifes.skills;

import com.msemu.commons.data.enums.MobBuffStat;
import com.msemu.commons.data.templates.skill.MobSkillInfo;
import com.msemu.world.client.character.stats.Option;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.data.SkillData;
import com.msemu.world.enums.MobSkillID;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.msemu.commons.data.enums.MobSkillStat.time;
import static com.msemu.commons.data.enums.MobSkillStat.x;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class MobSkill {
    private static final Logger log = LoggerFactory.getLogger(MobSkill.class);
    private int skillID;
    private byte action;
    private int level;
    private int effectAfter;
    private int skillAfter;
    private byte priority;
    private boolean firstAttack;
    private boolean onlyFsm;
    private boolean onlyOtherSkill;
    private int skillForbid;
    private int afterDelay;
    private int fixDamR;
    private boolean doFirst;
    private int preSkillIndex;
    private int preSkillCount;
    private String info;
    private String text;
    private boolean afterDead;
    private int afterAttack;
    private int afterAttackCount;
    private int castTime;
    private int coolTime;
    private int delay;
    private int useLimit;
    private String speak;
    private int skill;

    public void handleEffect(Mob mob) {
        MobTemporaryStat mts = mob.getTemporaryStat();
        short skill = (short) getSkill();
        short level = (short) getLevel();
        MobSkillInfo msi = SkillData.getInstance().getMobSkillInfoByIdAndLevel(skill, level);
        MobSkillID msID = MobSkillID.getMobSkillIDByValue(skill);
        Option o = new Option(skill);
        o.slv = level;
        o.tOption = msi.getSkillStatIntValue(time);
        switch (msID) {
            case PowerUp:
            case PowerUp2:
            case PowerUp3:
                o.nOption = msi.getSkillStatIntValue(x);
                mts.addMobSkillOptionsAndBroadCast(MobBuffStat.PowerUp, o);
                break;
            case PGuardUp:
                o.nOption = msi.getSkillStatIntValue(x);
                mts.addMobSkillOptionsAndBroadCast(MobBuffStat.PGuardUp, o);
                break;
            case MGuardUp:
                o.nOption = msi.getSkillStatIntValue(x);
                mts.addMobSkillOptionsAndBroadCast(MobBuffStat.MGuardUp, o);
                break;
            default:
                log.warn(String.format("Unhandled mob skill %d, slv = %d", getSkill(), getLevel()));
                break;
        }
    }
}

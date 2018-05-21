package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_ChangeSkillRecordResult;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_StatChanged;
import com.msemu.world.client.character.AvatarData;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.stats.CharacterStat;
import com.msemu.world.client.character.ExtendSP;
import com.msemu.world.client.character.skill.Skill;
import com.msemu.world.constants.JobConstants;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.constants.SkillConstants;
import com.msemu.world.enums.Stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/5/6.
 */
public class CP_UserSkillUpRequest extends InPacket<GameClient> {

    private int updateTick;
    private int skillID;
    private int distributeSP;

    public CP_UserSkillUpRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        updateTick = decodeInt();
        skillID = decodeInt();
        distributeSP = decodeInt();
        distributeSP = distributeSP == 0 ? 1 : distributeSP;
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        AvatarData ad = chr.getAvatarData();
        CharacterStat stat = ad.getCharacterStat();
        Skill skill = chr.getSkill(skillID, true);
        byte jobLevel = (byte) ((byte) SkillConstants.getSkillJobLevel(skill.getRootId()) + 1);
        Map<Stat, Object> stats = null;
        if (JobConstants.isSeparatedSp(chr.getJob())) {
            ExtendSP esp = chr.getAvatarData().getCharacterStat().getExtendSP();
            boolean isBeginnerSkill = MapleJob.isBeginner(skillID / 10000) && (skillID % 10000 == 1000
                    || // 嫩寶丟擲術
                    skillID % 10000 == 1001
                    || // 治癒
                    skillID % 10000 == 1002
                    || // 疾風之步
                    skillID % 10000 == 2
                    || // 能效
                    skillID == 140000291 // 第六感知
            );

            int currentSp;
            if (isBeginnerSkill) {
                final boolean resistance = skillID / 10000 == 3000 || skillID / 10000 == 3001;
                final int maxSp = resistance ? 9 : 6;
                int haveSp = Math.min(chr.getLevel() - 1, maxSp);
                haveSp -= chr.getSkill(((skillID / 10000) * 10000) + 1000, true).getCurrentLevel();
                haveSp -= chr.getSkill(((skillID / 10000) * 10000) + 1001, true).getCurrentLevel();
                haveSp -= chr.getSkill(((skillID / 10000) * 10000) + (resistance ? 2 : 1002), true).getCurrentLevel();
                haveSp -= chr.getSkill(140000291, true).getCurrentLevel();
                currentSp = haveSp;
            } else {
                currentSp = esp.getSpByJobLevel(jobLevel);
            }
            if (currentSp >= distributeSP) {
                int curLevel = skill.getCurrentLevel();
                int maxLevel = SkillConstants.is4thJobSkill(skill.getSkillId()) ? skill.getMasterLevel() : skill.getMaxLevel();
                int newLevel = curLevel + distributeSP > maxLevel ? maxLevel : curLevel + distributeSP;
                skill.setCurrentLevel(newLevel);
                if (!isBeginnerSkill) {
                    esp.setSpToJobLevel(jobLevel, currentSp - distributeSP);
                }
                stats = new HashMap<>();
                stats.put(Stat.SP, esp);
            }
        } else {
            int currentSp = chr.getAvatarData().getCharacterStat().getSp();
            if (currentSp >= distributeSP) {
                int curLevel = skill.getCurrentLevel();
                int max = skill.getMaxLevel();
                int newLevel = curLevel + distributeSP > max ? max : curLevel + distributeSP;
                skill.setCurrentLevel(newLevel);
                chr.getAvatarData().getCharacterStat().setSp(currentSp - distributeSP);
                stats = new HashMap<>();
                stats.put(Stat.SP, chr.getAvatarData().getCharacterStat().getSp());
            }
        }
        if (stats != null) {
            chr.write(new LP_StatChanged(stats));
            List<Skill> skills = new ArrayList<>();
            skills.add(skill);
            chr.addSkill(skill);
            chr.write(new LP_ChangeSkillRecordResult(skills, true, false, false, false));
        } else {
            chr.enableActions();
        }
    }
}

package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.skill.Skill;

import java.util.List;

/**
 * Created by Weber on 2018/4/23.
 */
public class LP_ChangeSkillRecordResult extends OutPacket<GameClient> {

    public LP_ChangeSkillRecordResult(List<Skill> skills,
                                      boolean exclRequestSendt,
                                      boolean showResult,
                                      boolean removeLinkSkill,
                                      boolean sn) {
        super(OutHeader.LP_ChangeSkillRecordResult);
        encodeByte(exclRequestSendt);
        encodeByte(showResult);
        encodeByte(removeLinkSkill);
        encodeShort(skills.size());
        skills.forEach(skill -> {
            encodeInt(skill.getSkillId());
            encodeInt(skill.getCurrentLevel());
            encodeInt(skill.getMasterLevel());
            skill.getDateExpire().encode(this);
        });
        encodeByte(4);
    }
}

package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.skill.SkillMacro;

import java.util.List;

public class LP_MacroSysDataInit extends OutPacket<GameClient> {

    public LP_MacroSysDataInit(List<SkillMacro> skillMacros) {
        super(OutHeader.LP_MacroSysDataInit);
        final int size = Math.min(skillMacros.size(), 5);
        encodeByte(size);
        for(int i = 0 ; i < size; i++) {
            encodeString(skillMacros.get(i).getName());
            encodeByte(skillMacros.get(i).getShout());
            encodeInt(skillMacros.get(i).getSkill1());
            encodeInt(skillMacros.get(i).getSkill2());
            encodeInt(skillMacros.get(i).getSkill3());
        }
    }
}

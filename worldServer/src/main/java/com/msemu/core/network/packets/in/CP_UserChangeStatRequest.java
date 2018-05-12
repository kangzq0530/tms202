package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.AvatarData;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.CharacterStat;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.enums.Stat;

/**
 * Created by Weber on 2018/5/6.
 */
public class CP_UserChangeStatRequest extends InPacket<GameClient> {
    public CP_UserChangeStatRequest(short opcode) {
        super(opcode);
    }

    private int updateTick;
    private int healHP;
    private int healMP;

    @Override
    public void read() {

        updateTick = decodeInt();
        if (available() >= 8) {
            skip(available() >= 12 ? 8 : 4);
        }
        healHP = decodeShort();
        healMP = decodeShort();
    }

    @Override
    public void runImpl() {

        Character chr = getClient().getCharacter();
        AvatarData avatarData = chr.getAvatarData();
        CharacterStat stat = avatarData.getCharacterStat();

        if (stat.getHp() <= 0)
            return;

        if (healHP != 0) {  // TODO check nodelay hack
            // TODO check real heal number
            chr.addStat(Stat.HP, healHP);
        }
        if (healMP != 0 && !MapleJob.is惡魔殺手(chr.getJob())) { // TODO check nodelay hack
            // TODO check real heal number
            chr.addStat(Stat.HP, healMP);
        }

    }
}

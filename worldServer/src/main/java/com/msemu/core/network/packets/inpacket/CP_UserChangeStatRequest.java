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

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.AvatarData;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.stats.CharacterStat;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.enums.Stat;

/**
 * Created by Weber on 2018/5/6.
 */
public class CP_UserChangeStatRequest extends InPacket<GameClient> {
    private int updateTick;
    private int healHP;
    private int healMP;
    public CP_UserChangeStatRequest(short opcode) {
        super(opcode);
    }

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

        if (healHP != 0 && chr.getStat(Stat.HP) < chr.getCurrentMaxHp()) {  // TODO check nodelay hack
            // TODO check real heal number
            chr.addStat(Stat.HP, healHP);
        }
        if (healMP != 0 && chr.getStat(Stat.MP) < chr.getCurrentMaxMp() && !MapleJob.is惡魔殺手(chr.getJob())) { // TODO check nodelay hack
            // TODO check real heal number
            chr.addStat(Stat.MP, healMP);
        }

    }
}

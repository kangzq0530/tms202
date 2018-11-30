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
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.stats.TemporaryStatManager;

/**
 * Created by Weber on 2018/5/19.
 */
public class CP_UserSkillCancelRequest extends InPacket<GameClient> {

    private int reason;
    private int flags[];

    public CP_UserSkillCancelRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        flags = new int[18];
        reason = decodeInt();
        decodeByte();
        for (int i = 0; i < 8; i++) {
            flags[i] = decodeInt();
        }
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();

        int[] _flags = tsm.getCurrentFlags();

        tsm.removeStatsBySkill(reason);

        boolean check = true;
        for (int i = 0; i < 8; i++) {
            check = flags[i] == _flags[i];
            if (!check)
                break;
        }

    }
}

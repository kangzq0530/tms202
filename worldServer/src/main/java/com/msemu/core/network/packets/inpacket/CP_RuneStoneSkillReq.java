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
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.runestones.RuneStoneManager;

public class CP_RuneStoneSkillReq extends InPacket<GameClient> {

    private boolean result;
    private int unk1;
    private int unk2;

    public CP_RuneStoneSkillReq(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        result = decodeByte() > 0;
        if (result) {
            unk1 = decodeInt();
            unk2 = decodeInt();
        }
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();
        final RuneStoneManager rsm = field.getRuneStoneManager();
        if (result) {
            rsm.useRuneStone(chr);
        }
    }
}

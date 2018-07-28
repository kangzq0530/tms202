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
import com.msemu.commons.utils.types.Tuple;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.FuncKeyMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/5/1.
 */
public class CP_FuncKeyMappedModified extends InPacket<GameClient> {

    private static final int MAX_CHANGED_COUNT = 100;
    private Map<Integer, Tuple<Byte, Integer>> keyMappings = new HashMap<>();


    public CP_FuncKeyMappedModified(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

        int type = decodeInt();

        switch (type) {
            case 0:
                int numChanged = decodeInt();
                for (int i = 0; i < numChanged && i < MAX_CHANGED_COUNT; i++) {
                    keyMappings.put(decodeInt(), new Tuple<>(decodeByte(), decodeInt()));
                }
                break;
            case 1:
            case 2:
                // [int] ( > 0 itemID, < 0 unregisterParty )
                // TODO type 1, 2
                break;
        }

    }

    @Override
    public void runImpl() {

        Character chr = getClient().getCharacter();
        FuncKeyMap funcKeyMap = chr.getFuncKeyMap();

        keyMappings.forEach((key, tuple) -> {
            funcKeyMap.putKeyBinding(key, tuple.getLeft(), tuple.getRight());
        });
    }
}

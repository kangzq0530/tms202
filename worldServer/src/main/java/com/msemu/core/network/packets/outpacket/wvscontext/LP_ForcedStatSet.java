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

package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.ForcedStat;

import java.util.Map;

/**
 * Created by Weber on 2018/5/12.
 */
public class LP_ForcedStatSet extends OutPacket<GameClient> {

    public LP_ForcedStatSet(Map<ForcedStat, Integer> forcedStats) {
        super(OutHeader.LP_ForcedStatSet);
        int mask = 0;
        for (ForcedStat forcedStat : forcedStats.keySet())
            mask |= forcedStat.getValue();
        encodeInt(mask);
        for (Map.Entry<ForcedStat, Integer> entry : forcedStats.entrySet()) {
            switch (entry.getKey()) {
                case SPEED:
                case JUMP:
                case UNKNOWN:
                    encodeByte(entry.getValue());
                    break;
                default:
                    encodeShort(entry.getValue());
                    break;
            }
        }
    }
}

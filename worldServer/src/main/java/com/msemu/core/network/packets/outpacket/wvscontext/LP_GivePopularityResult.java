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

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_GivePopularityResult extends OutPacket<GameClient> {

    public LP_GivePopularityResult(int type, String name, boolean raise, int newFame) {
        super(OutHeader.LP_GivePopularityResult);
        encodeByte(type);
        if (type == 0 || type == 5) {
            encodeString(name);
            encodeByte(raise);
            if (type == 0) {
                encodeInt(newFame);
            }
        }
    }

    public static class LP_GivePopularitySlefAddError extends LP_GivePopularityResult {
        public LP_GivePopularitySlefAddError() {
            super(1, "", false, 0);
        }
    }

    public static class LP_GivePopularityLevelBelow15Error extends LP_GivePopularityResult {
        public LP_GivePopularityLevelBelow15Error() {
            super(2, "", false, 0);
        }
    }

    public static class LP_GivePopularityNotTodayError extends LP_GivePopularityResult {
        public LP_GivePopularityNotTodayError() {
            super(3, "", false, 0);
        }
    }

    public static class LP_GivePopularityNotThisMonthError extends LP_GivePopularityResult {
        public LP_GivePopularityNotThisMonthError() {
            super(4, "", false, 0);
        }
    }

    public static class LP_GivePopularitySuccess extends LP_GivePopularityResult {
        public LP_GivePopularitySuccess(String name, boolean raise, int newFame) {
            super(0, name, raise, newFame);
        }
    }

    public static class LP_GivePopularitySuccessForTarget extends LP_GivePopularityResult {
        public LP_GivePopularitySuccessForTarget(String name, boolean raise) {
            super(5, name, raise, 0);
        }
    }
}

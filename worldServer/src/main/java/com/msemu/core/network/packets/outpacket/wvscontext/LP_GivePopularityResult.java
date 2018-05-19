package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_GivePopularityResult extends OutPacket<GameClient> {

    public static class LPGivePopularitySlefAddError extends LP_GivePopularityResult {
        public LPGivePopularitySlefAddError() {
            super(1, "", false, 0);
        }
    }

    public static class LPGivePopularityLevelBelow15Error extends LP_GivePopularityResult {
        public LPGivePopularityLevelBelow15Error() {
            super(2, "", false, 0);
        }
    }

    public static class LPGivePopularityNotTodayError extends LP_GivePopularityResult {
        public LPGivePopularityNotTodayError() {
            super(3, "", false, 0);
        }
    }

    public static class LPGivePopularityNotThisMonthError extends LP_GivePopularityResult {
        public LPGivePopularityNotThisMonthError() {
            super(4, "", false, 0);
        }
    }


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
}

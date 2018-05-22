package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_GivePopularityResult extends OutPacket<GameClient> {

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

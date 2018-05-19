package com.msemu.core.network.packets.outpacket.wvscontext;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import lombok.Getter;

import java.util.List;

/**
 * Created by Weber on 2018/4/29.
 */
public class LP_MemoResult extends OutPacket<GameClient> {

    // TODO
    @Getter
    class Memo {
        int id;
        String from, message;
        FileTime time;
        boolean gift;
    }

    public static class ShowMemoResult extends LP_MemoResult {
        public ShowMemoResult(List<Memo> memos) {
            super();
            encodeByte(4);
            encodeByte(memos.size());
            memos.forEach(each-> {
                encodeInt(each.getId());
                encodeString(each.getFrom());
                encodeString(each.getMessage());
                each.getTime().encode(this);
                encodeByte(each.isGift());
            });
        }
    }

    public LP_MemoResult() {
        super(OutHeader.LP_MemoResult);
    }
}

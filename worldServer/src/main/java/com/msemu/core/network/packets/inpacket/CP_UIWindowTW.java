package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.local.LP_UIWindowTW;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.salon.PreviewSalonResponse;
import com.msemu.world.enums.ChatMsgType;
import com.msemu.world.enums.SerializedFunctionType;

public class CP_UIWindowTW extends InPacket<GameClient> {

    private int functionId;

    public CP_UIWindowTW(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        functionId = decodeInt();
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final SerializedFunctionType function = SerializedFunctionType.getByValue(functionId);


        if( function != null) {
            switch (function) {
                case PREVIEW_SALON_REQUEST:
                    chr.write(new LP_UIWindowTW(new PreviewSalonResponse()));
                    break;
                case STORE_SALON_REQUEST:
                    // [id :int]
                    // [type :int] 2 == face, 3 == hair
                    break;
            }
        } else {
            chr.chatMessage(ChatMsgType.SYSTEM, String.format("[LP_UIWindowTW] 未知函數指針: %d", functionId));
        }

    }
}

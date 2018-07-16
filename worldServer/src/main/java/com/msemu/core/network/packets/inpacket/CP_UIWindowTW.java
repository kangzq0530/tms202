package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;

public class CP_UIWindowTW extends InPacket<GameClient> {

    private int unk;

    public CP_UIWindowTW(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        unk = decodeInt();
    }

    @Override
    public void runImpl() {

        final Character chr = getClient().getCharacter();

        switch (unk) {
            case 0x6D530290:
                chr.write(new LP_UIWindowTW());
                break;
        }
    }
}

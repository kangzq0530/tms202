package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.World;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.WhisperCommand;

/**
 * Created by Weber on 2018/5/12.
 */
public class CP_Whisper extends InPacket<GameClient> {

    private WhisperCommand command;
    private int updateTick;
    private String charName;
    private String text;

    public CP_Whisper(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        command = WhisperCommand.getByValue(decodeByte());
        updateTick = decodeInt();
        switch (command) {
            case Find:
            case FindFriend:
                charName = decodeString();
                break;
            case Whisper:
                charName = decodeString();
                text = decodeString();
                break;
        }
    }

    @Override
    public void runImpl() {

    }
}

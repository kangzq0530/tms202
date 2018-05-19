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
                for(int i = 0; i < numChanged && i < MAX_CHANGED_COUNT; i++) {
                    keyMappings.put(decodeInt(), new Tuple<>(decodeByte(), decodeInt()));
                }
                break;
            case 1:
            case 2:
                // [int] ( > 0 itemID, < 0 remove )
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

package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.World;

/**
 * Created by Weber on 2018/4/22.
 */
public class MigrateIn extends InPacket<GameClient> {

    private int worldId;

    private int characterId;

    public MigrateIn(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

        worldId = decodeInt();
        characterId = decodeInt();

    }

    @Override
    public void runImpl() {

        World world = World.getInstance();

        if(worldId != world.getWorldId()) {
            getClient().close();
            return;
        }



    }
}

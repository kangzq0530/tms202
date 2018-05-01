package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.login.WorldInformation;
import com.msemu.core.network.packets.out.login.WorldInformationEnd;
import com.msemu.login.LoginServer;

import java.util.List;

/**
 * Created by Weber on 2018/4/21.
 */
public class WorldInfoRequest extends InPacket<LoginClient> {

    public WorldInfoRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

    }

    @Override
    public void runImpl() {

        List<WorldInfo> worlds = LoginServer.getRmi().getWorlds();
        worlds.forEach(world -> getClient().write(new WorldInformation(world)));
        getClient().write(new WorldInformationEnd());

    }
}

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.outpacket.login.LP_WorldInformation;
import com.msemu.core.network.packets.outpacket.login.LP_WorldInformationEnd;
import com.msemu.login.LoginServer;

import java.util.List;

/**
 * Created by Weber on 2018/4/21.
 */
public class CP_WorldInfoRequest extends InPacket<LoginClient> {

    public CP_WorldInfoRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

    }

    @Override
    public void runImpl() {

        List<WorldInfo> worlds = LoginServer.getRmi().getWorlds();
        worlds.forEach(world -> getClient().write(new LP_WorldInformation(world)));
        getClient().write(new LP_WorldInformationEnd());

    }
}

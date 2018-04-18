package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.Login.CheckPasswordResult;
import com.msemu.core.network.packets.out.Login.SelectWorldResult;
import com.msemu.login.LoginServer;
import com.msemu.login.enums.LoginResultType;
import org.slf4j.LoggerFactory;

/**
 * Created by Weber on 2018/4/19.
 */
public class SelectWorld extends InPacket<LoginClient> {

    private int mode;

    private int worldID = -1;

    private int channelID = -1;

    public SelectWorld(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        mode = decodeByte();
        if(mode == 0) {
            worldID = decodeByte();
            channelID = decodeByte() + 1;
        } else {
            LoggerFactory.getLogger(SelectWorld.class).error("TODO: handleSelectWorld mode:2");
        }
    }

    @Override
    public void runImpl() {


        WorldInfo worldInfo = LoginServer.getRmi().getWorldById(worldID);

        int finalChannelID = channelID;
        boolean checkWorld = worldInfo != null
                && worldInfo.getChannels().stream()
                .filter(ch->ch.getChannel() == finalChannelID)
                .findFirst()
                .isPresent();

        if(!checkWorld) {
            getClient().write(new CheckPasswordResult(null, LoginResultType.ServerError, null));
            return;
        }

        client.setWorld(worldID);
        client.setChannel(channelID);

        client.write(new SelectWorldResult(client.getAccount(), worldInfo));

    }

}

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.outpacket.login.LP_CheckPasswordResult;
import com.msemu.core.network.packets.outpacket.login.LP_SelectWorldResult;
import com.msemu.login.LoginServer;
import com.msemu.login.enums.LoginResultCode;
import com.msemu.login.service.LoginCookieService;
import com.msemu.login.service.relogin.ReLoginInfo;

/**
 * Created by Weber on 2018/4/19.
 */
public class CP_SelectWorld extends InPacket<LoginClient> {

    private int mode;

    private int worldID = -1;

    private int channelID = -1;

    private String token = "";

    public CP_SelectWorld(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        mode = decodeByte();
        if (mode == 0) {
            worldID = decodeByte();
            channelID = decodeByte();
        } else {
            skip(1);
            token = decodeString();
        }
    }

    @Override
    public void runImpl() {

        if(mode != 0) {
            ReLoginInfo info = LoginCookieService.getInstance().getReLoginInfoByToken(token);
            if(info == null) {
                getClient().close();
                return;
            }
            channelID = info.getChannel();
            worldID = info.getWorld();
            LoginCookieService.getInstance().removeReLoginInfoByToken(token);
        }

        WorldInfo worldInfo = LoginServer.getRmi().getWorldById(worldID);

        int finalChannelID = channelID;
        boolean checkWorld = worldInfo != null
                && worldInfo.getChannels().stream()
                .filter(ch -> ch.getChannel() == finalChannelID)
                .findFirst()
                .isPresent();

        if (!checkWorld) {
            getClient().write(new LP_CheckPasswordResult(null, LoginResultCode.DBFail, null));
            return;
        }

        client.setWorld(worldID);
        client.setChannel(channelID);

        client.write(new LP_SelectWorldResult(client.getAccount(), worldInfo));

    }

}

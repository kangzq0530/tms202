package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.rmi.model.ChannelInfo;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.commons.utils.BCryptUtils;
import com.msemu.core.configs.LoginConfig;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.outpacket.login.LP_SelectCharacterResult;
import com.msemu.login.LoginServer;
import com.msemu.login.client.Account;
import com.msemu.login.enums.LoginResultCode;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

/**
 * Created by Weber on 2018/4/22.
 */
public class CP_SelectCharacter extends InPacket<LoginClient> {


    private String secondPassword;

    private int characterID;

    public CP_SelectCharacter(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        if (LoginConfig.CHECK_SECOND_PASSWORD) {
            secondPassword = decodeString();
        }
        characterID = decodeInt();
    }

    @Override
    public void runImpl() {
        Account account = getClient().getAccount();

        if (LoginConfig.CHECK_SECOND_PASSWORD && !BCryptUtils.checkPassword(secondPassword, account.getPic())) {
            getClient().write(new LP_SelectCharacterResult(LoginResultCode.IncorrectSPW, new byte[]{0, 0, 0, 0}, (short) 0, 0));
            return;
        }
        WorldInfo worldInfo = LoginServer.getRmi().getWorldById(getClient().getWorld());
        if (worldInfo == null) {
            getClient().write(new LP_SelectCharacterResult(LoginResultCode.DBFail, new byte[]{0, 0, 0, 0}, (short) 0, 0));
            return;
        }
        ChannelInfo channelInfo = worldInfo.getChannels().stream().filter(ch -> ch.getChannel() == getClient().getChannel())
                .findFirst().orElse(null);
        if (channelInfo == null) {
            getClient().write(new LP_SelectCharacterResult(LoginResultCode.DBFail, new byte[]{0, 0, 0, 0}, (short) 0, 0));
            return;
        }
        try {
            worldInfo.getConnection().addTransfer(channelInfo.getChannel(), account.getId(), characterID);
            InetAddress hostAddress = InetAddress.getByName(channelInfo.getHost());
            getClient().write(new LP_SelectCharacterResult(LoginResultCode.LoginSuccess, new byte[]{(byte) 192, (byte) 168, (byte) 156, 1}, channelInfo.getPort(), characterID));
        } catch (UnknownHostException | RemoteException e) {
            LoggerFactory.getLogger(CP_SelectCharacter.class).error("QQ", e);
            getClient().write(new LP_SelectCharacterResult(LoginResultCode.DBFail, new byte[]{0, 0, 0, 0}, (short) 0, 0));
        }
    }
}

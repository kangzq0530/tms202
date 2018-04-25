package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.rmi.model.ChannelInfo;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.Login.SelectCharacterResult;
import com.msemu.login.LoginServer;
import com.msemu.login.enums.LoginResultType;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

/**
 * Created by Weber on 2018/4/23.
 */
public class DirectGoToField extends InPacket<LoginClient> {
    int characterId;
    boolean offlineMode;

    public DirectGoToField(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        characterId = decodeInt();
        offlineMode = decodeByte() > 0;
    }

    @Override
    public void runImpl() {
        WorldInfo worldInfo = LoginServer.getRmi().getWorldById(getClient().getWorld());
        if (worldInfo == null) {
            getClient().write(new SelectCharacterResult(LoginResultType.ServerError, new byte[]{0, 0, 0, 0}, (short) 0, 0));
            return;
        }
        ChannelInfo channelInfo = worldInfo.getChannels().stream().filter(ch -> ch.getChannel() == getClient().getChannel())
                .findFirst().orElse(null);
        if (channelInfo == null) {
            getClient().write(new SelectCharacterResult(LoginResultType.ServerError, new byte[]{0, 0, 0, 0}, (short) 0, 0));
            return;
        }

        try {
            worldInfo.getConnection().addTransfer(channelInfo.getChannel(), channelInfo.getWorldId(), characterId);
            InetAddress hostAddress = InetAddress.getByName(channelInfo.getHost());
            getClient().write(new SelectCharacterResult(LoginResultType.LoginSuccess, new byte[]{(byte) 192, (byte) 168, (byte) 156, 1}, channelInfo.getPort(), 0));
        } catch (UnknownHostException | RemoteException e) {
            getClient().write(new SelectCharacterResult(LoginResultType.ServerError, new byte[]{0, 0, 0, 0}, (short) 0, 0));
        }
    }
}

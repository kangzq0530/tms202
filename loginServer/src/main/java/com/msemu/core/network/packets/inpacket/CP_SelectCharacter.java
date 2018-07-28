/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
            getClient().write(new LP_SelectCharacterResult(LoginResultCode.LoginSuccess, hostAddress.getAddress(), channelInfo.getPort(), characterID));

        } catch (UnknownHostException | RemoteException e) {
            LoggerFactory.getLogger(CP_SelectCharacter.class).error("QQ", e);
            getClient().write(new LP_SelectCharacterResult(LoginResultCode.DBFail, new byte[]{0, 0, 0, 0}, (short) 0, 0));
        }
    }
}

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

package com.msemu.core.network;

import com.msemu.commons.network.Client;
import com.msemu.commons.network.Connection;
import com.msemu.commons.network.crypt.MapleCrypt;
import com.msemu.core.configs.CoreConfig;
import com.msemu.core.network.packets.outpacket.login.LP_ConnectToClient;
import com.msemu.login.client.Account;
import com.msemu.login.enums.LoginResultCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/3/29.
 */
public class LoginClient extends Client<LoginClient> {

    @Getter
    @Setter
    private Account account;

    @Getter
    @Setter
    private int world;

    @Getter
    @Setter
    private int channel;

    @Getter
    @Setter
    private LoginResultCode loginResult = LoginResultCode.TempBlock;

    public LoginClient(Connection<LoginClient> connection) {
        super(connection);
    }


    @Override
    public void onOpen() {
        super.onOpen();
        write(new LP_ConnectToClient(this));
        MapleCrypt crypt = new MapleCrypt(CoreConfig.GAME_SERVICE_TYPE, (short) CoreConfig.GAME_VERSION);
        getConnection().setSendCipher(crypt);
        getConnection().setRecvCipher(crypt);
    }


}

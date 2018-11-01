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

import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.enums.ClientState;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.BCryptUtils;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.outpacket.login.LP_SetGenderResult;
import com.msemu.login.client.Account;

public class CP_SetGender extends InPacket<LoginClient> {

    private String username;
    private String pic;

    public CP_SetGender(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        username = decodeString();
        pic = decodeString();
    }

    @Override
    public void runImpl() {
        final LoginClient client = getClient();
        final Account account = client.getAccount();
        if (!client.compareAndSetState(ClientState.AUTHED_GG, ClientState.CONNECTED))
            return;
        if (account == null || !account.getUsername().equalsIgnoreCase(username)) {
            client.write(new LP_SetGenderResult(false));
        } else {
            final String picHash = BCryptUtils.hashPassword(pic);
            account.setPic(picHash);
            DatabaseFactory.getInstance().saveToDB(account);
            client.write(new LP_SetGenderResult(true));
        }
    }
}

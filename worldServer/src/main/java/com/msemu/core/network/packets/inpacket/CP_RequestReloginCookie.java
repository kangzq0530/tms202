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
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_IssueReloginCookie;
import com.msemu.world.client.Account;
import com.msemu.world.service.LoginCookieService;

import java.rmi.RemoteException;

/**
 * Created by Weber on 2018/5/1.
 */
public class CP_RequestReloginCookie extends InPacket<GameClient> {

    private String username;

    public CP_RequestReloginCookie(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        username = decodeString();
    }

    @Override
    public void runImpl() {

        Account account = getClient().getAccount();

        if (!account.getSecureUserName().equals(username)) {
            getClient().close();
            return;
        }

        String token = null;
        try {
            token = LoginCookieService.getInstance().generateToken(account.getUsername(), getClient().getWorld().getWorldId(), getClient().getChannel());
            getClient().write(new LP_IssueReloginCookie(token));
        } catch (RemoteException ignored) {
        } finally {
//            getClient().close();
        }


    }
}

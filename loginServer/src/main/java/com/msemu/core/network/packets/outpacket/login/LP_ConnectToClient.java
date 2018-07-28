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

package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.configs.CoreConfig;
import com.msemu.core.network.LoginClient;

/**
 * Created by Weber on 2018/3/30.
 */
public class LP_ConnectToClient extends OutPacket<LoginClient> {
    public LP_ConnectToClient(LoginClient client) {
        super();
        this.encodeShort(0x2E);
        this.encodeShort(CoreConfig.GAME_VERSION);
        this.encodeString(CoreConfig.GAME_PATCH_VERSION);
        this.encodeArr(client.getRiv());
        this.encodeArr(client.getSiv());
        this.encodeByte(CoreConfig.GAME_SERVICE_TYPE.getLocal());
        this.encodeByte(0);
        this.encodeShort(CoreConfig.GAME_VERSION);
        this.encodeShort(CoreConfig.GAME_VERSION);
        this.encodeShort(0);
        this.encodeArr(client.getRiv());
        this.encodeArr(client.getSiv());
        this.encodeByte(CoreConfig.GAME_SERVICE_TYPE.getLocal());
        String mPatchVer = CoreConfig.GAME_PATCH_VERSION.split(":")[0];
        this.encodeInt(Short.parseShort(mPatchVer));
        this.encodeInt(Short.parseShort(mPatchVer));
        this.encodeInt(0);
        this.encodeShort(1);
    }
}

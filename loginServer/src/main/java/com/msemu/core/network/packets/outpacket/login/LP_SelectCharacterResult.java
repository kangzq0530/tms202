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

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.login.enums.LoginResultCode;

/**
 * Created by Weber on 2018/4/22.
 */
public class LP_SelectCharacterResult extends OutPacket<LoginClient> {

    public LP_SelectCharacterResult(LoginResultCode resultType, byte[] ip, int port, int characterID) {
        super(OutHeader.LP_SelectCharacterResult);
        encodeByte(resultType.getValue());
        encodeByte(0);
        encodeArr(ip);
        encodeShort(port);
        encodeInt(0); // uChatIp
        encodeShort(0); // uChatPort
        encodeInt(characterID);
        encodeInt(characterID);
        encodeByte(0);
        encodeInt(0);
        encodeByte(0);
        encodeInt(0);

    }
}

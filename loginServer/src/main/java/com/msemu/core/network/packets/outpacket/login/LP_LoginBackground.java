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
import com.msemu.commons.utils.DateTimeUtils;
import com.msemu.core.network.LoginClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Weber on 2018/3/30.
 */
public class LP_LoginBackground extends OutPacket<LoginClient> {

    public LP_LoginBackground() {
        super(OutHeader.LP_LoginBackground);
        List<String> background = Arrays.asList(
                "MapLogin", "MapLogin0", "MapLogin1", "MapLogin2"
        );
        Collections.shuffle(background);
        this.encodeString(background.get(0));
        this.encodeTime(DateTimeUtils.getCurrentDate());
        this.encodeByte(true);
    }
}

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

package com.msemu.commons.network.crypt;

import com.msemu.commons.enums.GameServiceType;

/**
 * Created by Weber on 2018/3/14.
 */
public class MapleExCrypt extends MapleCrypt {
    public MapleExCrypt(GameServiceType serviceType, short serverVersion) {
        super(serviceType, serverVersion);
    }

    @Override
    public void crypt(byte[] data, byte[] iv) {
        int i = 0;
        int i0 = data.length;
        byte[] a0 = iv;
        if (i0 < 32) {
            i = 0;
        } else {
            int i1 = 0;
            i = 0;
            while (i1 < i0 - i0 % 32) {
                int i2 = i;
                int i3 = i;
                while (i2 < i + 32) {
                    int i4 = data[i3];
                    int i5 = a0[0];
                    int i6 = (byte) (i4 + i5);
                    i2 = i3 + 1;
                    data[i3] = (byte) i6;
                    i3 = i2;
                }
                i = i + 32;
                i1 = i;
            }
        }
        if (i < i0) {
            while (i < i0) {
                int i7 = data[i];
                int i8 = a0[0];
                int i9 = (byte) (i7 + i8);
                int i10 = i + 1;
                data[i] = (byte) i9;
                i = i10;
                i = i10;
            }
        }
        return;
    }
}

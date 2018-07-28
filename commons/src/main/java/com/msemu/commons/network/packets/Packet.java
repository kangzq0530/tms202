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

package com.msemu.commons.network.packets;

import com.msemu.commons.network.Client;
import com.msemu.commons.utils.HexUtils;

import java.util.Arrays;

/**
 * Created by Weber on 2018/3/23.
 */
public class Packet<TClient extends Client<TClient>> {
    private byte[] data;

    public Packet() {
        data = new byte[0];
    }

    public Packet(byte[] data) {
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public int getLength() {
        if (data != null) {
            return data.length;
        }
        return 0;
    }

    public int getHeader() {
        if (data.length < 2) {
            return 0xFFFF;
        }
        return (data[0] + (data[1] << 8));
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    @Override
    public String toString() {
        if (data == null) return "";
        return "[Packet] | " + HexUtils.readableByteArray(data);
    }

}

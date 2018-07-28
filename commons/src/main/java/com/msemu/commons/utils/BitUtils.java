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

package com.msemu.commons.utils;

/**
 * Created by Weber on 2018/3/16.
 */
public class BitUtils {

    public static byte[] multiplyBytes(byte[] iv, int i, int i0) {
        byte[] ret = new byte[i * i0];
        for (int x = 0; x < ret.length; x++) {
            ret[x] = iv[x % i];
        }
        return ret;
    }

    public static byte rollLeft(byte in, int count) {
        return (byte) (((in & 0xFF) << (count % 8) & 0xFF)
                | ((in & 0xFF) << (count % 8) >> 8));
    }

    public static byte rollRight(byte in, int count) {
        int tmp = (int) in & 0xFF;
        tmp = (tmp << 8) >>> (count % 8);
        return (byte) ((tmp & 0xFF) | (tmp >>> 8));
    }
}

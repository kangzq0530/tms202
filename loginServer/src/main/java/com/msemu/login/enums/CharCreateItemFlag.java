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

package com.msemu.login.enums;

/**
 * Created by Weber on 2018/4/19.
 */
public enum CharCreateItemFlag {

    臉型(0x1),
    髮型(0x2),
    臉飾(0x4),
    耳朵(0x8),
    尾巴(0x10),
    帽子(0x20),
    衣服(0x40),
    褲裙(0x80),
    披風(0x100),
    鞋子(0x200),
    手套(0x400),
    武器(0x800),
    副手(0x1000),;

    private final int value;

    CharCreateItemFlag(int value) {
        this.value = value;
    }

    public int getVelue() {
        return value;
    }

    public boolean check(int x) {
        return (value & x) != 0;
    }
}

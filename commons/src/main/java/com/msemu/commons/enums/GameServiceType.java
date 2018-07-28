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

package com.msemu.commons.enums;

import java.nio.charset.Charset;

/**
 * Created by Weber on 2018/3/14.
 */
public enum GameServiceType {
    Korean(1, "MS949"),
    KoreanTest(2, "MS949"),
    Japan(3, "MS932"),
    China(4, "MS936"),
    Tespia(5, "MS949"),
    Taiwan(6, "MS950"),
    Sea(7, "MS949"),
    Global(8, "MS949"),
    Brazil(9, "MS949");

    int local;
    Charset charset;

    GameServiceType(int local, String codePage) {
        this.local = local;
        this.charset = Charset.forName(codePage);
    }

    public int getLocal() {
        return this.local;
    }

    public Charset getCharset() {
        return this.charset;
    }
}

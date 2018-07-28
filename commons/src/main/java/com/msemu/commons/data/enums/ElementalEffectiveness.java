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

package com.msemu.commons.data.enums;

/**
 * Created by Weber on 2018/4/25.
 */
public enum ElementalEffectiveness {

    NORMAL(1.0), IMMUNE(0.0), STRONG(0.5), WEAK(1.5);

    private final double value;

    private ElementalEffectiveness(final double val) {
        this.value = val;
    }

    public static int getNumber(final ElementalEffectiveness elementalEffectiveness) {
        switch (elementalEffectiveness) {
            case NORMAL:
                return 0;
            case IMMUNE:
                return 1;
            case STRONG:
                return 2;
            case WEAK:
                return 3;
        }
        return -1;
    }

    public static ElementalEffectiveness getByNumber(final int num) {
        switch (num) {
            case 0:
                return NORMAL;
            case 1:
                return IMMUNE;
            case 2:
                return STRONG;
            case 3:
                return WEAK;
            default:
                throw new IllegalArgumentException("Unkown effectiveness: " + num);
        }
    }

    public double getValue() {
        return value;
    }
}


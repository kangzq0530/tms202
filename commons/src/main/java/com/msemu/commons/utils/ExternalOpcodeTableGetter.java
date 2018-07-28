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

import com.msemu.commons.network.packets.IHeader;

import java.util.*;

/**
 * Created by Weber on 2018/3/29.
 */
public class ExternalOpcodeTableGetter {

    final Properties props;

    public ExternalOpcodeTableGetter(Properties properties) {
        props = properties;
    }

    private static <T extends Enum<? extends IHeader> & IHeader> T valueOf(final String name, T... values) {
        for (T val : values) {
            if (val.name().equals(name)) {
                return val;
            }
        }
        return null;
    }

    public final static <T extends Enum<? extends IHeader> & IHeader> String getOpcodeTable(T... enumeration) {
        StringBuilder enumVals = new StringBuilder();
        List<T> all = new ArrayList<>(); // need a mutable list plawks
        all.addAll(Arrays.asList(enumeration));
        Collections.sort(all, (IHeader o1, IHeader o2) -> Short.valueOf(o1.getValue()).compareTo(o2.getValue()));
        all.forEach((code) -> {
            enumVals.append(code.name());
            enumVals.append(" = ");
            enumVals.append("0x");
            enumVals.append(HexUtils.toString(code.getValue()));
            enumVals.append(" (");
            enumVals.append(code.getValue());
            enumVals.append(")\n");
        });
        return enumVals.toString();
    }

    public final static <T extends Enum<? extends IHeader> & IHeader> void populateValues(Properties properties, T... values) {
        ExternalOpcodeTableGetter exc = new ExternalOpcodeTableGetter(properties);
        for (T code : values) {
            ((IHeader) code).setValue(exc.getValue(code.name(), values, (short) -2));
        }
    }

    private <T extends Enum<? extends IHeader> & IHeader> short getValue(final String name, T[] values, final short def) {
        String prop = props.getProperty(name);
        if (prop != null && prop.length() > 0) {
            String trimmed = prop.trim();
            String[] args = trimmed.split(" ");
            int base = 0;
            String offset;
            if (args.length == 2) {
                base = valueOf(args[0], values).getValue();
                if (base == def) {
                    base = getValue(args[0], values, def);
                }
                offset = args[1];
            } else {
                offset = args[0];
            }
            if (offset.length() > 2 && offset.substring(0, 2).equals("0x")) {
                return (short) (Short.parseShort(offset.substring(2), 16) + base);
            } else {
                return (short) (Short.parseShort(offset) + base);
            }
        }
        return def;
    }
}


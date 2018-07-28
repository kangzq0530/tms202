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

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.NetworkChannel;
import java.util.*;

/**
 * Created by Weber on 2018/3/14.
 */

public class SocketUtils {
    private static final Logger log = LoggerFactory.getLogger(SocketUtils.class);
    private static final SocketOption[] STANDARD_SOCKET_OPTIONS;

    static {
        ArrayList<SocketOption> stdSockOptions = new ArrayList<>();
        Class<? extends StandardSocketOptions> ssoClass = StandardSocketOptions.class;
        Field[] fields = ssoClass.getDeclaredFields();
        for (Field ssoClassField : fields) {
            if (Modifier.isStatic(ssoClassField.getModifiers()) && ssoClassField.getType().isAssignableFrom(SocketOption.class)) {
                try {
                    SocketOption ex = (SocketOption) ssoClassField.get(null);
                    stdSockOptions.add(ex);
                } catch (Exception var7) {
                    log.error("Error while parsing socket options.", var7);
                }
            }
        }
        STANDARD_SOCKET_OPTIONS = stdSockOptions.toArray(new SocketOption[stdSockOptions.size()]);
    }

    public SocketUtils() {
    }

    public static Pair<SocketOption, Object>[] parseSocketOptions(String sockOptStr) throws IllegalArgumentException {
        HashMap<SocketOption, Object> options = new HashMap<>();
        StringTokenizer st = new StringTokenizer(sockOptStr, ";,");
        while (true) {
            String result;
            do {
                if (!st.hasMoreTokens()) {
                    ArrayList<Pair<SocketOption, Object>> optList = new ArrayList<>(options.size());
                    for (Map.Entry<SocketOption, Object> o : options.entrySet()) {
                        optList.add(Pair.of(o.getKey(), o.getValue()));
                    }
                    return optList.toArray(new Pair[options.size()]);
                }

                result = st.nextToken().trim();
            } while (result.isEmpty());

            SocketOption key = null;
            Object e = null;
            for (SocketOption opt : STANDARD_SOCKET_OPTIONS) {
                if (result.startsWith(opt.name())) {
                    int vbi = opt.name().length();

                    int vei;
                    for (vei = result.length() - 1; vbi < vei && Character.isSpaceChar(result.charAt(vbi)); ++vbi) {
                        ;
                    }

                    while (vei > vbi && Character.isSpaceChar(result.charAt(vei))) {
                        --vei;
                    }

                    if (vbi != vei && result.charAt(vbi) == 40 && result.charAt(vei) == 41) {
                        String valStr = result.substring(vbi + 1, vei).trim();
                        if (opt.type() == Boolean.class) {
                            key = opt;
                            e = Boolean.parseBoolean(valStr);
                        } else {
                            if (opt.type() != Integer.class) {
                                throw new IllegalArgumentException("Unknown socket option type for \"" + result + "\"");
                            }

                            key = opt;

                            try {
                                e = Integer.parseInt(valStr);
                            } catch (NumberFormatException var14) {
                                throw new IllegalArgumentException("Unknown socket option type for \"" + result + "\"", var14);
                            }
                        }
                    }
                }
            }

            if (key == null) {
                throw new IllegalArgumentException("Unknown socket option for \"" + result + "\"");
            }

            options.put(key, e);
        }
    }

    public static <TNetworkChannel extends NetworkChannel> TNetworkChannel applySocketOptions(TNetworkChannel networkChannel, Pair<SocketOption, Object>... options) throws IOException {
        Set<SocketOption<?>> supportedOptions = networkChannel.supportedOptions();
        for (Pair<SocketOption, Object> soPair : options) {
            SocketOption option = soPair.getKey();
            Object value = soPair.getValue();
            if (!supportedOptions.contains(option)) {
                log.warn("Socket option \"{}\" is not supported.", option.name());
            } else {
                networkChannel = (TNetworkChannel) networkChannel.setOption(option, value);
            }
        }
        return networkChannel;
    }

    public static final void closeAsyncChannelSilent(AsynchronousSocketChannel socketChannel) {
        try {
            socketChannel.shutdownInput();
        } catch (IOException ignored) {
            ;
        }

        try {
            socketChannel.shutdownOutput();
        } catch (IOException ignored) {
            ;
        }

        try {
            socketChannel.close();
        } catch (IOException ignored) {
            ;
        }

    }
}
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

package com.msemu.commons.network.handler;

import com.msemu.commons.enums.InHeader;
import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.Client;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.utils.ExternalOpcodeTableGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * Created by Weber on 2018/3/29.
 */

public class PropertiesPacketHandlerFactory<T extends Client<T>> extends AbstractPacketHandlerFactory<T> implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(PropertiesPacketHandlerFactory.class);

    public PropertiesPacketHandlerFactory() {
        super(null);
        load();
    }

    public void load() {
        loadOutHeader();
        loadInHeader();
        loadPacketPrototypes();
    }

    private void loadPacketPrototypes() {
        this.removeAllPackets();
        for (InHeader header : InHeader.values()) {
            try {
                Class clazz = Class.forName("com.msemu.core.network.packets.inpacket." + header.name());
                this.addClientPacket((InPacket<T>) clazz.getDeclaredConstructor(new Class[]{Short.TYPE})
                        .newInstance(header.getValue()), header.getStates());

            } catch (ClassNotFoundException ignored) {
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                log.error("Error while creating client packet from XML definition\'s!", e);
            }
        }
        log.info("PacketPrototypes Loaded ");
    }

    private void loadOutHeader() {
        String fileName = "sendops.properties";
        Properties props = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(fileName); BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream))) {
            props.load(br);
        } catch (IOException ex) {
            InputStream in = OutHeader.class.getClassLoader().getResourceAsStream("properties/" + fileName);
            if (in == null) {
                log.error("Load OutPacket Header Error", ex);
                return;
            }
            try {
                props.load(in);
                in.close();
            } catch (IOException e) {
                throw (RuntimeException) new RuntimeException("加載 " + fileName + " 檔案出錯").initCause(e);
            }
        }
        ExternalOpcodeTableGetter.populateValues(props, OutHeader.values());
        log.info("OutHeader Loaded ");
    }

    private void loadInHeader() {
        String fileName = "recvops.properties";
        Properties props = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(fileName); BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream))) {
            props.load(br);
        } catch (IOException ex) {
            InputStream in = OutHeader.class.getClassLoader().getResourceAsStream("properties/" + fileName);
            if (in == null) {
                log.error("Load OutPacket Header Error", ex);
                return;
            }
            try {
                props.load(in);
                in.close();
            } catch (IOException e) {
                throw (RuntimeException) new RuntimeException("加載 " + fileName + " 檔案出錯", e).initCause(e);
            }
        }
        ExternalOpcodeTableGetter.populateValues(props, InHeader.values());
        log.info("InHeader Loaded ");
    }

    @Override
    public void reload() {
        this.load();
    }
}

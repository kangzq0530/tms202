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
        for(InHeader header : InHeader.values()) {
            try {
                Class clazz = Class.forName("com.msemu.core.network.packets.in." + header.name());
                this.addClientPacket((InPacket<T>)clazz.getDeclaredConstructor(new Class[]{Short.TYPE})
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
            if(in == null) {
                log.error("Load OutPacket Header Error", ex);
                return;
            }
            try {
                props.load(in);
                in.close();
            } catch (IOException e) {
                throw new RuntimeException("加載 " + fileName + " 檔案出錯", e);
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
            if(in == null) {
                log.error("Load OutPacket Header Error", ex);
                return;
            }
            try {
                props.load(in);
                in.close();
            } catch (IOException e) {
                throw new RuntimeException("加載 " + fileName + " 檔案出錯", e);
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

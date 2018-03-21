package com.msemu.commons.network.handler;

import com.msemu.commons.network.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Weber on 2018/3/15.
 */


public class PropertyPacketHandlerFactory<T extends Client<T>> extends AbstractPacketHandlerFactory<T> {
    private static final Logger log = LoggerFactory.getLogger(PropertyPacketHandlerFactory.class);


    public PropertyPacketHandlerFactory() {
        super(null, null);
        this.loadPackets();
    }


    private Properties loadProp(String fileName) {
        Properties props = new Properties();
        try (FileInputStream fs = new FileInputStream(fileName)) {
            props.load(fs);
        } catch (IOException ignore) {
            try {
                props.load(PropertyPacketHandlerFactory.class.getResourceAsStream("com.msemu.commons.network.properties"));
                return props;
            } catch (IOException ex) {
                log.error("Loading recvops.properties error", ex);
            }
        }
        return null;
    }

    private void loadPackets() {

        Properties send = loadProp("sendops.properties");
        Properties recv = loadProp("recvops.properties");


        if(send != null) {
            send.stringPropertyNames().forEach(OpcodeName -> {

            });
        }

        if(recv != null) {

        }


        // TODO : LoadPacket

        //log.info("Loaded {} server and {} client opcodes definition\'s.", Integer.valueOf(serverPacketsCount), Integer.valueOf(clientPacketCount));
    }
}

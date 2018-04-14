package com.msemu.commons.network.packets;

import com.msemu.commons.enums.InHeader;
import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.commons.utils.ExternalOpcodeTableGetter;
import com.msemu.core.startup.StartupComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/29.
 */
@Reloadable(name = "opcode",
        group = "all")
@StartupComponent("Network")
public class OpcodeLoader implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(OpcodeLoader.class);
    private static final AtomicReference<OpcodeLoader> instance = new AtomicReference<>();

    public static OpcodeLoader getInstance() {
        OpcodeLoader value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new OpcodeLoader();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public OpcodeLoader() {
        loadOutHeader();
        loadInHeader();
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
    }

    @Override
    public void reload() {

    }
}

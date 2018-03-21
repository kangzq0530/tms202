package com.msemu.login.service;

import com.msemu.commons.rmi.ILoginServerRMI;
import com.msemu.commons.rmi.IWorldServerRMI;
import com.msemu.commons.rmi.SocketFactory;
import com.msemu.commons.rmi.model.WorldChannelInfo;
import com.msemu.commons.rmi.model.WorldRegisterResult;
import com.msemu.commons.thread.TimerPool;
import com.msemu.core.configs.NetworkConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Weber on 2018/3/14.
 */
public class LoginServerRMI extends UnicastRemoteObject implements ILoginServerRMI {
    private static final Logger log = LoggerFactory.getLogger((Class) LoginServerRMI.class);
    private Map<Integer, Map<Integer, WorldChannelInfo>> worlds;

    public LoginServerRMI() throws RemoteException {
        this.worlds = new ConcurrentHashMap<>();

        try {
            Registry registry = LocateRegistry.createRegistry(NetworkConfig.RMI_PORT, new SocketFactory(), new SocketFactory());
            registry.bind("msemu_login_server", this);
            log.info("登陸伺服器等待世界伺服器連接於 PORT {}", NetworkConfig.RMI_PORT);
        } catch (Exception ex) {
            log.error("初始化 Java RMI 伺服器錯誤", ex);
        } finally {
            TimerPool.getInstance().getEtcTimer().register(this::WorldChannelsStatusWatcher, 1000, 0);
        }
    }

    private void WorldChannelsStatusWatcher() {
        Iterator iterator = this.worlds.entrySet().iterator();
        for (Map.Entry<Integer, Map<Integer, WorldChannelInfo>> entry : this.worlds.entrySet()) {
            for (Map.Entry<Integer, WorldChannelInfo> subEntry : entry.getValue().entrySet()) {
                WorldChannelInfo worldChannelInfo = subEntry.getValue();
                if (worldChannelInfo.getConnection() == null) continue;
                try {
                    worldChannelInfo.getConnection().testConnection();
                } catch (RemoteException except) {
                    worldChannelInfo.setConnection(null);
                    //TODO: 更新World伺服器資訊
                    log.info("世界伺服器 ID={} and channelId={} disconnected.", entry.getKey(), subEntry.getKey());
                    continue;
                }
                break;
            }
        }
    }

    @Override
    public boolean testConnection() throws RemoteException {
        return true;
    }

    @Override
    public WorldRegisterResult registerWorld(IWorldServerRMI rmi, WorldChannelInfo worldInfo) throws RemoteException {
        return null;
    }

    @Override
    public void updateWorld(IWorldServerRMI rmi, WorldChannelInfo worldInfo) throws RemoteException {

    }
}

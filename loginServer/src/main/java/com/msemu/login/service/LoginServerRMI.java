package com.msemu.login.service;

import com.msemu.commons.rmi.ILoginServerRMI;
import com.msemu.commons.rmi.IWorldServerRMI;
import com.msemu.commons.rmi.SocketFactory;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.commons.rmi.model.WorldRegisterResult;
import com.msemu.commons.thread.EventManager;
import com.msemu.core.configs.NetworkConfig;
import com.msemu.login.client.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/3/14.
 */
public class LoginServerRMI extends UnicastRemoteObject implements ILoginServerRMI {
    private static final Logger log = LoggerFactory.getLogger((Class) LoginServerRMI.class);
    private Map<Integer, WorldInfo> worlds;

    public LoginServerRMI() throws RemoteException {
        this.worlds = new ConcurrentHashMap<>();

        try {
            Registry registry = LocateRegistry.createRegistry(NetworkConfig.RMI_PORT, new SocketFactory(), new SocketFactory());
            registry.bind("msemu_login_server", this);
            log.info("Login Server wait for World to connect at  PORT {}", NetworkConfig.RMI_PORT);
        } catch (Exception ex) {
            log.error("Init Java RMI Error", ex);
        } finally {
            EventManager.getInstance().addFixedDelayEvent(this::WorldChannelsStatusWatcher, 0, 2000);
        }
    }

    private void WorldChannelsStatusWatcher() {
        for (Map.Entry<Integer, WorldInfo> entry : this.worlds.entrySet()) {
            WorldInfo worldInfo = entry.getValue();
            if (worldInfo.getConnection() == null) continue;
            try {
                worldInfo.getConnection().testConnection();
            } catch (RemoteException except) {
                worlds.remove(worldInfo.getWorldId());
                log.info("World ID={} is disconnected.", entry.getKey());
                continue;
            }
            break;
        }
    }

    @Override
    public boolean testConnection() throws RemoteException {
        return true;
    }

    @Override
    public WorldRegisterResult registerWorld(IWorldServerRMI rmi, WorldInfo worldInfo) throws RemoteException {
        if (worlds.containsKey(worldInfo.getWorldId())) {
            log.warn("World ID={} is already registered", worldInfo.getWorldId());
            return WorldRegisterResult.ALREADY_REGISTERED;
        }
        WorldInfo info = new WorldInfo();
        info.update(rmi, worldInfo);
        worlds.put(info.getWorldId(), info);
        log.info("World ID={} is registered", worldInfo.getWorldId());
        return WorldRegisterResult.SUCCESS;
    }

    @Override
    public void updateWorld(IWorldServerRMI rmi, WorldInfo worldInfo) throws RemoteException {
        if (!worlds.containsKey(worldInfo.getWorldId()))
            return;
        worlds.get(worldInfo.getWorldId()).update(rmi, worldInfo);
    }

    public List<WorldInfo> getWorlds() {
        return this.worlds.values().stream().collect(Collectors.toList());
    }

    public WorldInfo getWorldById(int worldId) {
        Optional<WorldInfo> opt = getWorlds().stream().filter(world -> world.getWorldId() == worldId).findAny();
        return opt.get();
    }

    public boolean hasWorldChannel(int worldId, int channel) {
        WorldInfo worldInfo = getWorldById(worldId);
        return worldInfo != null &&
                worldInfo.getChannels().stream().filter(channelInfo -> channelInfo.getChannel() == channel).findAny().isPresent();
    }


    public boolean isAccountOnline(Account accountSchema) {
        boolean online = false;
        for (Map.Entry<Integer, WorldInfo> entry : this.worlds.entrySet()) {
            final WorldInfo worldInfo = entry.getValue();
            try {
                if (worldInfo.getConnection() == null) {
                    continue;
                }
                online |= worldInfo.getConnection().isAccountOnServer(accountSchema.getId());
            } catch (RemoteException ex) {
                log.error("Error while calling isAccountOnline on serverId={} and accountId={}", worldInfo.getWorldId(), accountSchema.getId(), ex);
            }
        }
        return online;
    }

    public boolean kickPlayerByAccount(Account accountSchema) {
        boolean hasKicked = false;
        for (Map.Entry<Integer, WorldInfo> entry : this.worlds.entrySet()) {
            final WorldInfo worldInfo = entry.getValue();
            try {
                if (worldInfo.getConnection() == null) {
                    continue;
                }
                hasKicked |= worldInfo.getConnection().kickByAccountId(accountSchema.getId());
            } catch (RemoteException ex) {
                log.error("Error while calling kickPlayerByAccount on serverId={} and accountId={}", worldInfo.getWorldId(), accountSchema.getId(), ex);
            }
        }
        return hasKicked;
    }

}
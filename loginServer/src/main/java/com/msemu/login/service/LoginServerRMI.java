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

package com.msemu.login.service;

import com.msemu.commons.rmi.ILoginServerRMI;
import com.msemu.commons.rmi.IWorldServerRMI;
import com.msemu.commons.rmi.SocketFactory;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.commons.rmi.model.RMIRegisterResult;
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
            log.info("登入伺服器等待世界伺服器於 連接埠 {}", NetworkConfig.RMI_PORT);
        } catch (Exception ex) {
            log.error("初始化登入伺服器的 Java RMI 伺服器失敗", ex);
        } finally {
            EventManager.getInstance().addFixedDelayEvent(this::WorldChannelsStatusWatcher, 0, 2000);
        }
    }

    private void WorldChannelsStatusWatcher() {
        for (Map.Entry<Integer, WorldInfo> entry : this.worlds.entrySet()) {
            WorldInfo worldInfo = entry.getValue();
            if (worldInfo.getConnection() == null) continue;
            try {
                worldInfo.getConnection().checkConnection();
            } catch (RemoteException except) {
                worlds.remove(worldInfo.getWorldId());
                log.info("世界伺服器 - {}({})  is disconnected.", worldInfo.getName(), worldInfo.getWorldId());
                continue;
            }
            break;
        }
    }

    @Override
    public boolean checkConnection() throws RemoteException {
        return true;
    }

    @Override
    public RMIRegisterResult registerWorld(IWorldServerRMI rmi, WorldInfo worldInfo) throws RemoteException {
        if (worlds.containsKey(worldInfo.getWorldId())) {
            log.warn("世界伺服器 - {}({}) 已在線上", worldInfo.getName(), worldInfo.getWorldId());
            return RMIRegisterResult.ALREADY_REGISTERED;
        }
        WorldInfo info = new WorldInfo();
        info.update(rmi, worldInfo);
        info.setConnection(rmi);
        worlds.put(info.getWorldId(), info);
        log.info("世界伺服器 - {}({}) 連線成功", worldInfo.getName(), worldInfo.getWorldId());
        return RMIRegisterResult.SUCCESS;
    }

    @Override
    public void updateWorld(IWorldServerRMI rmi, WorldInfo worldInfo) throws RemoteException {
        if (!worlds.containsKey(worldInfo.getWorldId()))
            return;
        worlds.get(worldInfo.getWorldId()).update(rmi, worldInfo);
        worlds.get(worldInfo.getWorldId()).setConnection(rmi);
    }

    @Override
    public void addReLoginCookie(String token, String username, int world, int channel) throws RemoteException {
        LoginCookieService.getInstance().addToken(token, username, world, channel);
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
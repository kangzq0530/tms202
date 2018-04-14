package com.msemu.world.service;

import com.msemu.commons.rmi.ILoginServerRMI;
import com.msemu.commons.rmi.IWorldServerRMI;
import com.msemu.commons.rmi.model.WorldRegisterResult;
import com.msemu.commons.thread.EventManager;
import com.msemu.core.configs.NetworkConfig;
import com.msemu.world.World;
import com.msemu.world.client.GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by Weber on 2018/3/31.
 */
public class WorldServerRMI extends UnicastRemoteObject implements IWorldServerRMI {

    private static final Logger log = LoggerFactory.getLogger(WorldServerRMI.class);

    private final Map<Integer, GameClient> accountsInWorld;
    private ILoginServerRMI connection;
    private transient ScheduledFuture<?> reconnectTask;
    private World world;

    public WorldServerRMI() throws RemoteException {
        this.accountsInWorld = new ConcurrentHashMap<>();
        this.world = new World();
        EventManager.getInstance().addFixedRateEvent(this::watchLoginServerStatus, 2000, 5000);
    }

    private void connectToLoginServer() {
        try {
            final Registry registry = LocateRegistry.getRegistry(NetworkConfig.RMI_PORT);
            this.connection = (ILoginServerRMI) registry.lookup("msemu_login_server");
            final WorldRegisterResult registerResult = this.connection.registerWorld(this, this.world.getWorldInfo());
            switch (registerResult) {
                case SUCCESS: {
                    log.info("Connected to login server successfully.");
                    break;
                }
                default: {
                    log.warn("Connection to login server failed. Reason: {}", registerResult.toString());
                    break;
                }
            }
        } catch (ConnectException e2) {
            log.warn("Login server isn't available. Make sure it's up and running. {}", e2);
        } catch (Exception e) {
            log.error("Connection to login server failed", e);
        } finally {
            this.reconnectTask = null;
            if (this.connection == null) {
                this.onConnectionLost();
            }
        }
    }

    @Override
    public boolean testConnection() throws RemoteException {
        return true;
    }

    @Override
    public boolean isAccountOnServer(int accountId) throws RemoteException {
        return this.accountsInWorld.containsKey(accountId);
    }

    @Override
    public boolean kickByAccountId(int accountId) throws RemoteException {
        if (this.accountsInWorld.containsKey(accountId)) {
            final GameClient client = this.accountsInWorld.get(accountId);
            if (client != null) {
                client.close();
            }
            return true;
        }
        return false;
    }

    private void onConnectionLost() {
        if (this.reconnectTask != null) {
            return;
        }
        log.info("Connection with login server lost.");
        this.connection = null;
        EventManager.getInstance().addEvent(() -> {
            log.info("Reconnecting to login server...");
            connectToLoginServer();
        }, 2000);
    }

    private void watchLoginServerStatus() {
        try {
            WorldServerRMI.this.connection.testConnection();
        } catch (Exception e) {
            if (WorldServerRMI.this.reconnectTask == null) {
                WorldServerRMI.this.onConnectionLost();
            }
        }
    }
}

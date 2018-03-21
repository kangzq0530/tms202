package com.msemu.world.service;

import com.msemu.commons.rmi.ILoginServerRMI;
import com.msemu.commons.rmi.IWorldServerRMI;
import com.msemu.commons.rmi.model.WorldChannelInfo;
import com.msemu.commons.rmi.model.WorldRegisterResult;
import com.msemu.core.configs.NetworkConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by Weber on 2018/3/16.
 */
public class WorldServerRMI extends UnicastRemoteObject implements IWorldServerRMI {
    private static final Logger log = LoggerFactory.getLogger(WorldServerRMI.class);

    private ILoginServerRMI connection;
    private WorldChannelInfo worldChannelInfo;
    private transient ScheduledFuture<?> reconnectTask;


    protected WorldServerRMI() throws RemoteException {

    }

    @Override
    public boolean testConnection() throws RemoteException {
        return false;
    }

    @Override
    public boolean isAccountOnServer(long accountId) throws RemoteException {
        return false;
    }

    @Override
    public void kickByAccountId(long var1) throws RemoteException {

    }

    private void connectToLoginServer() {
//        try {
//            final Registry registry = LocateRegistry.getRegistry(NetworkConfig.RMI_HOST, NetworkConfig.RMI_PORT);
//            this.connection = (ILoginServerRMI) registry.lookup("bdo_login_server");
//            final WorldRegisterResult registerResult = this.connection.registerChannel(this, this.worldChannelInfo);
//            switch (registerResult) {
//                case SUCCESS: {
//                    GameServerRMI.log.info("Connected to login server successfully.");
//                    break;
//                }
//                default: {
//                    GameServerRMI.log.warn("Connection to login server failed. Reason: {}", registerResult.toString());
//                    break;
//                }
//            }
//        } catch (ConnectException e2) {
//            GameServerRMI.log.warn("Loginserver isn't available. Make sure it's up and running. {}", e2);
//        } catch (Exception e) {
//            GameServerRMI.log.error("Connection to login server failed", e);
//        } finally {
//            this.reconnectTask = null;
//            if (this.connection == null) {
//                this.onConnectionLost();
//            }
//        }
    }

}

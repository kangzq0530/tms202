package com.msemu.commons.rmi;

import com.msemu.commons.rmi.model.WorldChannelInfo;
import com.msemu.commons.rmi.model.WorldRegisterResult;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Weber on 2018/3/14.
 */
public interface ILoginServerRMI extends Remote {
    boolean testConnection() throws RemoteException;

    WorldRegisterResult registerWorld(IWorldServerRMI rmi, WorldChannelInfo worldInfo) throws RemoteException;

    void updateWorld(IWorldServerRMI rmi, WorldChannelInfo  worldInfo) throws RemoteException;

}

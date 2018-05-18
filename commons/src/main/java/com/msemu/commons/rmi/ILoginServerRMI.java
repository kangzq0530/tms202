package com.msemu.commons.rmi;

import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.commons.rmi.model.WorldRegisterResult;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Weber on 2018/3/14.
 */
public interface ILoginServerRMI extends Remote {
    boolean checkConnection() throws RemoteException;

    WorldRegisterResult registerWorld(IWorldServerRMI rmi, WorldInfo worldInfo) throws RemoteException;

    void updateWorld(IWorldServerRMI rmi, WorldInfo worldInfo) throws RemoteException;

    void addReLoginCookie(String token, String username, int world, int channel) throws RemoteException;
}

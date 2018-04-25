package com.msemu.commons.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Weber on 2018/3/14.
 */
public interface IWorldServerRMI extends Remote {

    boolean testConnection() throws RemoteException;

    boolean isAccountOnServer(int accountId) throws RemoteException;

    boolean kickByAccountId(int accountId) throws RemoteException;

    void addTransfer(int channelId, int accountId, int characterId) throws RemoteException;

}

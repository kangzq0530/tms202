package com.msemu.commons.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Weber on 2018/3/14.
 */
public interface IWorldServerRMI extends Remote {

    boolean testConnection() throws RemoteException;

    boolean isAccountOnServer(long accountId) throws RemoteException;

    void kickByAccountId(long var1) throws RemoteException;

}

package com.msemu.commons.rmi.model;

import com.msemu.commons.rmi.IWorldServerRMI;

import java.io.Serializable;

/**
 * Created by Weber on 2018/3/14.
 */
public class WorldChannelInfo implements Serializable {
    private int worldId;
    private String worldName;
    private int flag;
    private String worldTips;

    private IWorldServerRMI connection;

    public IWorldServerRMI getConnection() {
        return this.connection;
    }

    public void setConnection(IWorldServerRMI connection) {
        this.connection = connection;
    }

}

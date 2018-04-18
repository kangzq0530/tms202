package com.msemu.commons.rmi.model;

import com.msemu.commons.enums.WorldServerType;
import com.msemu.commons.rmi.IWorldServerRMI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/3/14.
 */
public class WorldInfo implements Serializable {

    private int worldId;

    private String name;

    private int state;

    private String worldEventDesc;

    private int worldEventExpWSE = 100;

    private int worldEventDropWSE = 100;

    private WorldServerType serverType;

    private List<ChannelInfo> channels;

    private IWorldServerRMI connection;


    public WorldInfo() {
        this.channels = new ArrayList<>();
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getWorldEventDesc() {
        return worldEventDesc;
    }

    public void setWorldEventDesc(String worldEventDesc) {
        this.worldEventDesc = worldEventDesc;
    }

    public int getWorldEventExpWSE() {
        return worldEventExpWSE;
    }

    public void setWorldEventExpWSE(int worldEventExpWSE) {
        this.worldEventExpWSE = worldEventExpWSE;
    }

    public int getWorldEventDropWSE() {
        return worldEventDropWSE;
    }

    public void setWorldEventDropWSE(int worldEventDropWSE) {
        this.worldEventDropWSE = worldEventDropWSE;
    }

    public IWorldServerRMI getConnection() {
        return this.connection;
    }

    public void setConnection(IWorldServerRMI connection) {
        this.connection = connection;
    }

    public List<ChannelInfo> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelInfo> channels) {
        this.channels = channels;
    }

    public WorldServerType getServerType() {
        return serverType;
    }

    public void setServerType(WorldServerType serverType) {
        this.serverType = serverType;
    }

    public void update(IWorldServerRMI connection, WorldInfo worldInfo) {
        this.connection = connection;
        this.worldId = worldInfo.getWorldId();
        this.name = worldInfo.getName();
        this.worldEventDesc = worldInfo.getWorldEventDesc();
        this.worldEventExpWSE = worldInfo.getWorldEventExpWSE();
        this.worldEventDropWSE = worldInfo.getWorldEventDropWSE();
        this.serverType = worldInfo.getServerType();
        this.setChannels(worldInfo.getChannels());
        this.state = worldInfo.getState();
    }
}

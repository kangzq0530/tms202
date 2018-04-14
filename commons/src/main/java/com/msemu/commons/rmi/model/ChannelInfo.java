package com.msemu.commons.rmi.model;

import java.io.Serializable;

/**
 * Created by Weber on 2018/3/30.
 */
public class ChannelInfo implements Serializable {

    private int worldId;

    private int channel;

    private String name;

    private int connectedClients;

    private int maxConnectedClients;

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConnectedClients() {
        return connectedClients;
    }

    public void setConnectedClients(int connectedClients) {
        this.connectedClients = connectedClients;
    }

    public int getMaxConnectedClients() {
        return maxConnectedClients;
    }

    public void setMaxConnectedClients(int maxConnectedClients) {
        this.maxConnectedClients = maxConnectedClients;
    }

    public int getChannelLoading() {
        if (getMaxConnectedClients() > 0)
            return getConnectedClients() * Integer.MAX_VALUE / (getMaxConnectedClients());
        else
            return 0;
    }

}

package com.msemu.commons.rmi.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Weber on 2018/3/30.
 */
public class ChannelInfo implements Serializable {

    @Getter
    @Setter
    private int worldId;

    @Getter
    @Setter
    private int channel;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int connectedClients;

    @Getter
    @Setter
    private int maxConnectedClients;

    @Getter
    @Setter
    private String host;

    @Getter
    @Setter
    private short port;

    public int getChannelLoading() {
        if (getMaxConnectedClients() > 0)
            return getConnectedClients() * Integer.MAX_VALUE / (getMaxConnectedClients());
        else
            return 0;
    }

}

package com.msemu.commons.rmi.model;

import com.msemu.commons.enums.WorldServerType;
import com.msemu.commons.rmi.IWorldServerRMI;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/3/14.
 */
@Getter
@Setter
public class WorldInfo implements Serializable {

    private int worldId = -1;

    private String name = "";

    private int state = 0;

    private String worldEventDesc = "";

    private int worldEventExpWSE = 100;

    private int worldEventDropWSE = 100;

    private WorldServerType serverType = WorldServerType.Normal;

    private List<ChannelInfo> channels = new ArrayList<>();

    private IWorldServerRMI connection = null;

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

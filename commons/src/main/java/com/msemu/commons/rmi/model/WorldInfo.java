/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

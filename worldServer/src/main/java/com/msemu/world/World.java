package com.msemu.world;

import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.core.configs.WorldConfig;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.channel.Channel;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.guild.Guild;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/3/30.
 */
@StartupComponent("Network")
public class World implements IReloadable {

    private int worldId;

    private String name;

    private int state;

    private String worldEventDesc;

    private int worldEventExpWSE = 100;

    private int worldEventDropWSE = 100;

    private static final AtomicReference<World> instance = new AtomicReference<>();

    private ConcurrentHashMap<Integer, Channel> channels;

    private final Map<Integer, Party> parties = new HashMap<>();

    private final Map<Integer, Guild> guilds = new HashMap<>();

    public static World getInstance() {
        World value = instance.get();
        if (value == null) {
            synchronized (World.instance) {
                value = instance.get();
                if (value == null) {
                    value = new World();
                }
                instance.set(value);
            }
        }
        return value;
    }

    public World() {
        channels = new ConcurrentHashMap<>();
        initWorld();
    }

    private void loadSettings() {
        this.name = WorldConfig.NAME;
        this.state = WorldConfig.STATE;
        this.worldEventDesc = WorldConfig.EVENT_DESC;
        this.worldEventDropWSE = WorldConfig.EVENT_DROP_RATE;
        this.worldEventExpWSE = WorldConfig.EVENT_EXP_RATE;
    }

    private void initWorld() {
        loadSettings();
        int channelCount = WorldConfig.CHANNEL_COUNT;
        for (int i = 1; i < channelCount; i++) {
            Channel channel = new Channel(getName() + "-" + i, getWorldId(), i);
            channels.put(i, channel);
        }
    }

    private void restartWorld() {

    }

    public WorldInfo getWorldInfo() {
        WorldInfo worldInfo = new WorldInfo();
        worldInfo.setName(name);
        worldInfo.setChannels(channels.values().stream().map(Channel::getChannelInfo).collect(Collectors.toList()));
        worldInfo.setWorldEventDesc(worldEventDesc);
        worldInfo.setWorldEventExpWSE(worldEventExpWSE);
        worldInfo.setWorldEventDropWSE(worldEventDropWSE);
        return worldInfo;
    }

    @Override
    public void reload() {
        this.loadSettings();
    }

    public void startWorld() {

    }

    public int getWorldId() {
        return worldId;
    }

    public String getName() {
        return name;
    }

    public int getState() {
        return state;
    }

    public String getWorldEventDesc() {
        return worldEventDesc;
    }

    public int getWorldEventExpWSE() {
        return worldEventExpWSE;
    }

    public int getWorldEventDropWSE() {
        return worldEventDropWSE;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setWorldEventDesc(String worldEventDesc) {
        this.worldEventDesc = worldEventDesc;
    }

    public void setWorldEventExpWSE(int worldEventExpWSE) {
        this.worldEventExpWSE = worldEventExpWSE;
    }

    public void setWorldEventDropWSE(int worldEventDropWSE) {
        this.worldEventDropWSE = worldEventDropWSE;
    }

    public Map<Integer, Party> getParties() {
        return parties;
    }


    public Map<Integer, Guild> getGuilds() {
        return guilds;
    }

    public Guild getGuildByID(int id) {
        return getGuilds().get(id);
    }
}

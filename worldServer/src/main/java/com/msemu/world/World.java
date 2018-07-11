package com.msemu.world;

import com.msemu.WorldServer;
import com.msemu.commons.enums.WorldServerType;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.core.configs.WorldConfig;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.guild.Guild;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/3/30.
 */
@StartupComponent("Network")
public class World implements IReloadable {

    private static final AtomicReference<World> instance = new AtomicReference<>();
    private final Map<Integer, Party> parties = new HashMap<>();
    private final Map<Integer, Guild> guilds = new HashMap<>();
    private int worldId;
    private String name;
    private int state;
    private String worldEventDesc;
    private int worldEventExpWSE = 100;
    private int worldEventDropWSE = 100;
    private ConcurrentHashMap<Integer, Channel> channels;

    public World() {
        channels = new ConcurrentHashMap<>();
        initWorld();
    }

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
        for (int i = 0; i < channelCount; i++) {
            Channel channel = new Channel(getName() + "-" + worldId, getWorldId(), i);
            channels.put(i, channel);
        }
    }

    private void restartWorld() {

    }

    public WorldInfo getWorldInfo() {
        WorldInfo worldInfo = new WorldInfo();
        worldInfo.setName(name);
        worldInfo.setChannels(channels.values().stream().map(Channel::getChannelInfo).collect(Collectors.toList()));
        worldInfo.setServerType(WorldServerType.Normal);
        worldInfo.setWorldEventDesc(worldEventDesc);
        worldInfo.setWorldEventExpWSE(worldEventExpWSE);
        worldInfo.setWorldEventDropWSE(worldEventDropWSE);
        worldInfo.setWorldId(worldId);
        return worldInfo;
    }

    @Override
    public void reload() {
        this.loadSettings();
        WorldServer.getRmi().updateWorld();
    }

    public void startWorld() {

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

    public Map<Integer, Party> getParties() {
        return parties;
    }


    public Map<Integer, Guild> getGuilds() {
        return guilds;
    }

    public Guild getGuildByID(int id) {
        return getGuilds().get(id);
    }


    public List<Channel> getChannels() {
        return channels.values().stream().collect(Collectors.toList());
    }

    public Character getCharacterByName(String charName) {
        Character ret = null;
        for(Channel channel : getChannels()) {
            ret = channel.getCharacterByName(charName);
            if(ret != null)
                break;
        }
        return ret;
    }
}

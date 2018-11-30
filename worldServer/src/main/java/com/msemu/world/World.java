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

package com.msemu.world;

import com.msemu.WorldServer;
import com.msemu.commons.enums.WorldServerType;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.core.configs.WorldConfig;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.client.LP_MigrateCommand;
import com.msemu.core.network.packets.outpacket.field.LP_TransferChannelReqIgnored;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.guild.Guild;
import com.msemu.world.enums.TransferChannelReqIgnoredType;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
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

    private ConcurrentHashMap<Integer, Channel> channels;
    @Getter
    @Setter
    private int worldId;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private int state;
    @Getter
    @Setter
    private String worldEventDesc;
    @Getter
    @Setter
    private int worldEventExpWSE = 100;
    @Getter
    @Setter
    private int worldEventDropWSE = 100;


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

    public List<Channel> getChannels() {
        return new ArrayList<>(channels.values());
    }


    public Character getCharacterByName(String charName) {
        Character ret = null;
        for (Channel channel : getChannels()) {
            ret = channel.getCharacterByName(charName);
            if (ret != null)
                break;
        }
        return ret;
    }

    public Channel getChannelByCharacterName(String charName) {
        Character ret = null;
        for (Channel channel : getChannels()) {
            ret = channel.getCharacterByName(charName);
            if (ret != null)
                return channel;
        }
        return null;
    }

    public Character getCharacterById(int charId) {
        Character ret = null;
        for (Channel channel : getChannels()) {
            ret = channel.getCharacterById(charId);
            if (ret != null)
                break;
        }
        return ret;
    }

    public boolean hasChannel(int targetChannel) {
        return getChannels().stream().anyMatch(ch -> ch.getChannelId() == targetChannel);
    }

    public Channel getChannel(int targetChannel) {
        return getChannels().stream().filter(ch -> ch.getChannelId() == targetChannel)
                .findFirst().orElse(null);
    }

    public void changeChannel(Character chr, Channel targetChannel) {
        final GameClient client = chr.getClient();
        final Channel channel = client.getChannelInstance();
        if ( !channel.equals(targetChannel) ) {
            targetChannel.addTransfer(client.getAccount().getId(), chr.getId());
            chr.logout();
            try {
                chr.write(new LP_MigrateCommand(true, Inet4Address.getByName(targetChannel.getHost()).getAddress(), channel.getPort()));
            } catch (UnknownHostException e) {
                LoggerFactory.getLogger("Network").error("changeChannel exception", e);
                chr.write(new LP_TransferChannelReqIgnored(TransferChannelReqIgnoredType.CANT_OPERATE_NOT));
            }
        }
    }

    public int getOnlineCount() {
        return getChannels().stream()
                .mapToInt(Channel::getOnlineCount).sum();
    }

    @Override
    public String toString()
    {
        return "世界 : " + getName() + " 頻道數 : " + channels.size() + " 上線人數 : " + getOnlineCount();
    }
}

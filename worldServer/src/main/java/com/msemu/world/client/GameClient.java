package com.msemu.world.client;

import com.msemu.WorldServer;
import com.msemu.core.configs.WorldConfig;
import com.msemu.world.World;
import com.msemu.world.client.character.Character;
import com.msemu.commons.network.netty.NettyClient;
import io.netty.channel.Channel;


/**
 * Created by Weber on 2018/3/30.
 */
public class GameClient extends NettyClient<GameClient> {

    private Account account;

    private Character character;

    private com.msemu.world.channel.Channel channelInstance;

    private int worldID;

    private int channelID;

    public GameClient(Channel c, byte[] siv, byte[] riv) {
        super(c, siv, riv);
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {

    }

    public Character getCharacter() {
        return character;
    }

    public int getWorldID() {
        return worldID;
    }

    public void setWorldID(int worldID) {
        this.worldID = worldID;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public com.msemu.world.channel.Channel getChannelInstance() {
        return channelInstance;
    }

    public void setChannel(com.msemu.world.channel.Channel channelInstance) {
        this.channelInstance = channelInstance;
        this.channelID = channelInstance.getChannelId();
    }

    public int getChannel() {
        return channelInstance.getChannelId();
    }

    public World getWorld() {
        return World.getInstance();
    }
}

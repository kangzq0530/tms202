package com.msemu.world.client;

import com.msemu.commons.enums.ClientState;
import com.msemu.commons.network.Connection;
import com.msemu.commons.network.crypt.MapleExCrypt;
import com.msemu.commons.utils.Rand;
import com.msemu.core.configs.CoreConfig;
import com.msemu.world.World;
import com.msemu.world.client.character.Character;
import com.msemu.commons.network.Client;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by Weber on 2018/3/30.
 */
public class GameClient extends Client<GameClient> {

    @Getter
    @Setter
    private Account account;

    @Getter
    @Setter
    private Character character;

    private com.msemu.world.channel.Channel channelInstance;

    private int worldID;

    private int channelID;


    public GameClient(Connection<GameClient> connection) {
        super(connection);
    }

    @Override
    public void onOpen() {
        super.onOpen();

        //TODO Hello Packet
        MapleExCrypt exCrypt = new MapleExCrypt(CoreConfig.GAME_SERVICE_TYPE, (short) CoreConfig.GAME_VERSION);
        getConnection().setCipher(exCrypt);

    }

    @Override
    public String toString() {
        try {
            switch (getState()) {
                case CONNECTED:
                    return super.toString();
                case AUTHED_GG:
                case AUTHED:
                case ENTERED:
                    return "[Account: " + getAccount().getUsername() + " - IP: " + getHostAddress() + "]";
                default:
                    return super.toString();
            }
        } catch (NullPointerException ignore) {
            return super.toString();
        }
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

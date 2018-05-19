package com.msemu.core.network;

import com.msemu.commons.network.Connection;
import com.msemu.commons.network.crypt.MapleCrypt;
import com.msemu.commons.network.crypt.MapleExCrypt;
import com.msemu.core.configs.CoreConfig;
import com.msemu.core.network.packets.outpacket.LP_ConnectToClient;
import com.msemu.world.Channel;
import com.msemu.world.World;
import com.msemu.world.client.Account;
import com.msemu.world.client.character.Character;
import com.msemu.commons.network.Client;
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

    @Getter
    private Channel channelInstance;

    public GameClient(Connection<GameClient> connection) {
        super(connection);
    }

    @Override
    public void onOpen() {
        super.onOpen();
        write(new LP_ConnectToClient(this));
        //TODO Hello Packet
        MapleExCrypt exCrypt = new MapleExCrypt(CoreConfig.GAME_SERVICE_TYPE, (short) CoreConfig.GAME_VERSION);
        MapleCrypt crypt = new MapleCrypt(CoreConfig.GAME_SERVICE_TYPE, (short) CoreConfig.GAME_VERSION);
        getConnection().setSendCipher(exCrypt);
        getConnection().setRecvCipher(crypt);

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

    public void setChannel(Channel channelInstance) {
        this.channelInstance = channelInstance;
    }

    public int getChannel() {
        return channelInstance.getChannelId();
    }

    public World getWorld() {
        return World.getInstance();
    }

}

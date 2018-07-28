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

package com.msemu.core.network;

import com.msemu.commons.network.Client;
import com.msemu.commons.network.Connection;
import com.msemu.commons.network.crypt.MapleCrypt;
import com.msemu.commons.network.crypt.MapleExCrypt;
import com.msemu.commons.thread.EventManager;
import com.msemu.core.configs.CoreConfig;
import com.msemu.core.network.packets.outpacket.LP_ConnectToClient;
import com.msemu.world.Channel;
import com.msemu.world.World;
import com.msemu.world.client.Account;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ScheduledFuture;


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

    @Getter
    private ScheduledFuture idleTask;

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
    protected void onIdle() {
        super.onIdle();
        if (idleTask != null) {
            idleTask = EventManager.getInstance().addEvent(this::close, 30 * 1000);
        }
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

    public int getChannel() {
        return channelInstance.getChannelId();
    }

    public void setChannel(Channel channelInstance) {
        this.channelInstance = channelInstance;
    }

    public World getWorld() {
        return World.getInstance();
    }

    public void removeIdleTask() {
        if (idleTask != null) {
            idleTask.cancel(true);
            idleTask = null;
        }
    }

}

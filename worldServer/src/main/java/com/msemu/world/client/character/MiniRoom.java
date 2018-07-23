package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class MiniRoom {


    private byte type;
    private int id;
    private String miniRoomTitle;
    private boolean isPrivate;
    private int gameKind;
    private int curUsers;
    private int maxUsers;
    private boolean gameOn;
    private boolean ADBoardRemote;
    private String msg;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getId());
        outPacket.encodeString(getMiniRoomTitle());
        outPacket.encodeByte(isPrivate());
        outPacket.encodeByte(getGameKind());
        outPacket.encodeByte(getCurUsers());
        outPacket.encodeByte(getMaxUsers());
        outPacket.encodeByte(isGameOn());
        outPacket.encodeByte(isADBoardRemote());
    }


}


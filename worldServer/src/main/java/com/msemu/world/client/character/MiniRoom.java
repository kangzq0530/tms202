package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/4/13.
 */
public class MiniRoom {


    private byte type;
    private int ID;
    private String miniRoomTitle;
    private boolean aPrivate;
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
        outPacket.encodeInt(getID());
        outPacket.encodeString(getMiniRoomTitle());
        outPacket.encodeByte(isPrivate());
        outPacket.encodeByte(getGameKind());
        outPacket.encodeByte(getCurUsers());
        outPacket.encodeByte(getMaxUsers());
        outPacket.encodeByte(isGameOn());
        outPacket.encodeByte(isADBoardRemote());
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMiniRoomTitle() {
        return miniRoomTitle;
    }

    public void setMiniRoomTitle(String miniRoomTitle) {
        this.miniRoomTitle = miniRoomTitle;
    }

    public boolean isPrivate() {
        return aPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        this.aPrivate = aPrivate;
    }

    public int getGameKind() {
        return gameKind;
    }

    public void setGameKind(int gameKind) {
        this.gameKind = gameKind;
    }

    public int getCurUsers() {
        return curUsers;
    }

    public void setCurUsers(int curUsers) {
        this.curUsers = curUsers;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public boolean isGameOn() {
        return gameOn;
    }

    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }

    public boolean isADBoardRemote() {
        return ADBoardRemote;
    }

    public void setADBoardRemote(boolean ADBoardRemote) {
        this.ADBoardRemote = ADBoardRemote;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}


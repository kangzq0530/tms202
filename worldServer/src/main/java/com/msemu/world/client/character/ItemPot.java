package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;

/**
 * Created by Weber on 2018/4/13.
 */
public class ItemPot {

    private int lifeID;

    private byte level;

    private byte lastState;

    private int satiety;

    private int friendly;

    private int remainAbleFriendly;

    private int remainFriendlyTime;

    private byte maximumIncLevel;

    private int maximumIncSatiety;

    private int lastEatTime;

    private FileTime lastSleepStartTime;

    private FileTime lastDecSatietyTime;

    private FileTime consumedLastTime;

    public int getLifeID() {
        return lifeID;
    }

    public void setLifeID(int lifeID) {
        this.lifeID = lifeID;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public byte getLastState() {
        return lastState;
    }

    public void setLastState(byte lastState) {
        this.lastState = lastState;
    }

    public int getSatiety() {
        return satiety;
    }

    public void setSatiety(int satiety) {
        this.satiety = satiety;
    }

    public int getFriendly() {
        return friendly;
    }

    public void setFriendly(int friendly) {
        this.friendly = friendly;
    }

    public int getRemainAbleFriendly() {
        return remainAbleFriendly;
    }

    public void setRemainAbleFriendly(int remainAbleFriendly) {
        this.remainAbleFriendly = remainAbleFriendly;
    }

    public int getRemainFriendlyTime() {
        return remainFriendlyTime;
    }

    public void setRemainFriendlyTime(int remainFriendlyTime) {
        this.remainFriendlyTime = remainFriendlyTime;
    }

    public byte getMaximumIncLevel() {
        return maximumIncLevel;
    }

    public void setMaximumIncLevel(byte maximumIncLevel) {
        this.maximumIncLevel = maximumIncLevel;
    }

    public int getMaximumIncSatiety() {
        return maximumIncSatiety;
    }

    public void setMaximumIncSatiety(int maximumIncSatiety) {
        this.maximumIncSatiety = maximumIncSatiety;
    }

    public int getLastEatTime() {
        return lastEatTime;
    }

    public void setLastEatTime(int lastEatTime) {
        this.lastEatTime = lastEatTime;
    }

    public FileTime getLastSleepStartTime() {
        return lastSleepStartTime;
    }

    public void setLastSleepStartTime(FileTime lastSleepStartTime) {
        this.lastSleepStartTime = lastSleepStartTime;
    }

    public FileTime getLastDecSatietyTime() {
        return lastDecSatietyTime;
    }

    public void setLastDecSatietyTime(FileTime lastDecSatietyTime) {
        this.lastDecSatietyTime = lastDecSatietyTime;
    }

    public FileTime getConsumedLastTime() {
        return consumedLastTime;
    }

    public void setConsumedLastTime(FileTime consumedLastTime) {
        this.consumedLastTime = consumedLastTime;
    }

    public void encode(OutPacket outPacket) {

        outPacket.encodeInt(getLifeID());
        outPacket.encodeByte(getLevel());
        outPacket.encodeByte(getLastState());
        outPacket.encodeInt(getSatiety());
        outPacket.encodeInt(getFriendly());
        outPacket.encodeInt(getRemainAbleFriendly());
        outPacket.encodeInt(getRemainFriendlyTime());
        outPacket.encodeByte(getMaximumIncLevel());
        outPacket.encodeInt(getMaximumIncSatiety());
        outPacket.encodeFT(getLastEatTime());
        outPacket.encodeFT(getLastSleepStartTime());
        outPacket.encodeFT(getLastDecSatietyTime());
        outPacket.encodeFT(getConsumedLastTime());


    }
}


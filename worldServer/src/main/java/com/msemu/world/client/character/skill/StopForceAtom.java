package com.msemu.world.client.character.skill;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/11.
 */
public class StopForceAtom {
    private int idx;
    private int count;
    private int weaponId;
    private List<Integer> angleInfo = new ArrayList<>();

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getIdx());
        outPacket.encodeInt(getCount());
        outPacket.encodeInt(getWeaponId());
        outPacket.encodeInt(getAngleInfo().size());
        getAngleInfo().forEach(outPacket::encodeInt);
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }

    public List<Integer> getAngleInfo() {
        return angleInfo;
    }

    public void setAngleInfo(List<Integer> angleInfo) {
        this.angleInfo = angleInfo;
    }
}
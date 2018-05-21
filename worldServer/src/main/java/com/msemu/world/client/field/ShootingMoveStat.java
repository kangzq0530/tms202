package com.msemu.world.client.field;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/12.
 */
public class ShootingMoveStat {
    @Getter
    @Setter
    private int movePattern, moveRange, bulletUpgrade, moveSpeed, moveAngle;

    public ShootingMoveStat(int movePattern, int moveRange, int bulletUpgrade, int moveSpeed, int moveAngle) {
        this.movePattern = movePattern;
        this.moveRange = moveRange;
        this.bulletUpgrade = bulletUpgrade;
        this.moveSpeed = moveSpeed;
        this.moveAngle = moveAngle;
    }

    public ShootingMoveStat() {
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getMovePattern());
        outPacket.encodeInt(getMoveRange());
        outPacket.encodeInt(getBulletUpgrade());
        outPacket.encodeInt(getMoveSpeed());
        outPacket.encodeInt(getMoveAngle());
    }
}

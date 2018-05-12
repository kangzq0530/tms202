package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.HitInfo;
import com.msemu.world.enums.ChatMsgType;
import com.msemu.world.enums.Stat;

/**
 * Created by Weber on 2018/5/5.
 */
public class CP_UserHit extends InPacket<GameClient> {

    private HitInfo hitInfo;

    public CP_UserHit(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

        /*
            [4][4][1][1][4 : damage][1 : 0][1 : 0][4 : 0]
            [4][4][1][1]
         */

        /***
         * DamagedFieldEtc: 0xF6
         * CSkeletonBoss::ProcessPartsAttack: 0xF7
         * DamagedByMobSkill : 0xF8
         * DamagedByHekatonFieldSkill: 0xF9
         * TrueDamagedByObtacleAtom: 0xFB
         */

        this.hitInfo = new HitInfo();
        this.hitInfo.setRandom(decodeInt());
        this.hitInfo.setUpdateTime(decodeInt());
        this.hitInfo.setType(decodeByte());

        switch (hitInfo.getType()) {
            case -1:
                decodeByte();
                hitInfo.setHPDamage(decodeInt());
                break;
        }

        getClient().getCharacter().chatMessage(ChatMsgType.GAME_DESC,
                String.format("[角色傷害] 種類: %d ", hitInfo.getType()));


        getClient().getCharacter().chatMessage(ChatMsgType.GAME_DESC,
                String.format("[剩下封包]: %s ", this.toString(true)));
    }

    @Override
    public void runImpl() {

    }
}

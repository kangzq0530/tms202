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

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.HitInfo;
import com.msemu.world.enums.ChatMsgType;

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

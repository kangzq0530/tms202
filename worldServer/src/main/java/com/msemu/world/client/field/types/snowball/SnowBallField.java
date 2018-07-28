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

package com.msemu.world.client.field.types.snowball;

import com.msemu.commons.data.templates.field.FieldTemplate;
import com.msemu.commons.data.templates.field.Portal;
import com.msemu.core.network.packets.outpacket.field.types.snowball.LP_SnowBallMsg;
import com.msemu.core.network.packets.outpacket.field.types.snowball.LP_SnowBallState;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.enums.SnowBallMsg;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/6/1.
 */
public class SnowBallField extends Field {

    @Getter
    @Setter
    private int state = -1;

    @Getter
    private SnowMan[] snowMans;
    @Getter
    private SnowBall[] snowBalls;

    public SnowBallField(FieldTemplate template) {
        super(template);
        this.snowMans = new SnowMan[2];
        this.snowBalls = new SnowBall[2];
        for (int i = 0; i < 2; i++) {
            this.snowBalls[i] = new SnowBall();
            this.snowMans[i] = new SnowMan();
        }
        this.init();
    }


    @Override
    public void enter(Character chr, Portal portal, boolean characterData) {
        setTeam(chr);
//        SnowBallInfo snowBallInfo = getFieldData().getSnowBallInfo();
//        portal = getPortalByName(snowBallInfo.getPortals()[chr.getTeam()]);
        super.enter(chr, portal, characterData);
        broadcastPacket(new LP_SnowBallState(this, true));
    }


    @Override
    public void leave(Character chr) {
        chr.setTeam(-1);
        super.leave(chr);
    }

    private synchronized void setTeam(Character chr) {
        long team0Count = getAllCharacters().stream()
                .filter(chrInMap -> chrInMap.getTeam() == 0)
                .count();
        long team1Count = getAllCharacters().stream()
                .filter(chrInMap -> chrInMap.getTeam() == 1)
                .count();
        if (team0Count > team1Count)
            chr.setTeam(1);
        else
            chr.setTeam(0);
    }

    public void start() {
        reset();
        for (int i = 0; i < 2; i++) {
            SnowBall snowBall = getSnowBalls()[i];
            broadcastPacket(new LP_SnowBallMsg(i, SnowBallMsg.SECTION0));
            this.snowBalls[i].setInvisible(false);
            broadcastPacket(new LP_SnowBallMsg(i, SnowBallMsg.RESTART));
            setState(0);
            broadcastPacket(new LP_SnowBallState(this, false));
        }
    }

    public boolean isStarted() {
        return getState() > -1;
    }


    public void init() {
        this.state = 0;
        for (int i = 0; i < 2; ++i) {
//            this.snowMans[i].setHp(getFieldData().getSnowBallInfo().getSnowManHP());
//            this.snowBalls[i].setXPos(getFieldData().getSnowBallInfo().getXMin());
            this.snowBalls[i].setInvisible(true);
        }
    }
}

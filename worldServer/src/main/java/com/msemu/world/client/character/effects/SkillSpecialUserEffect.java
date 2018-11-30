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

package com.msemu.world.client.character.effects;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.constants.SkillConstants;
import com.msemu.world.enums.UserEffectType;

/**
 * Created by Weber on 2018/5/3.
 */
public class SkillSpecialUserEffect implements IUserEffect {

    private int skillID;

    private int nx;
    private int ny;
    private int slv;

    private boolean aBoolean1;
    private boolean aBoolean2;
    private int blinkLightOrginX, blinkLightOrginY;
    private int blinkLightDestX, blinkLightDestY;

    public SkillSpecialUserEffect(int skillID, int nx, int ny, int slv) {
        this.skillID = skillID;
        this.nx = nx;
        this.ny = ny;
        this.slv = slv;
    }

    public SkillSpecialUserEffect(int skillID, boolean aBoolean1, boolean aBoolean2, int blinkLightOrginX, int blinkLightOrginY, int blinkLightDestX, int blinkLightDestY) {
        this.skillID = skillID;
        this.aBoolean1 = aBoolean1;
        this.aBoolean2 = aBoolean2;
        this.blinkLightOrginX = blinkLightOrginX;
        this.blinkLightOrginY = blinkLightOrginY;
        this.blinkLightDestX = blinkLightDestX;
        this.blinkLightDestY = blinkLightDestY;
    }

    public SkillSpecialUserEffect(int skillID, int nx, int ny, int slv, boolean aBoolean1, boolean aBoolean2, int blinkLightOrginX, int blinkLightOrginY, int blinkLightDestX, int blinkLightDestY) {
        this.skillID = skillID;
        this.nx = nx;
        this.ny = ny;
        this.slv = slv;
        this.aBoolean1 = aBoolean1;
        this.aBoolean2 = aBoolean2;
        this.blinkLightOrginX = blinkLightOrginX;
        this.blinkLightOrginY = blinkLightOrginY;
        this.blinkLightDestX = blinkLightDestX;
        this.blinkLightDestY = blinkLightDestY;
    }

    @Override
    public UserEffectType getType() {
        return UserEffectType.SkillSpecial;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(skillID);
        if (SkillConstants.isExplosionSkill(skillID)) {
            outPacket.encodeInt(nx);
            outPacket.encodeInt(ny);
            outPacket.encodeInt(slv);
        } else if (skillID == 32111016) {
            outPacket.encodeByte(aBoolean1);
            outPacket.encodeByte(aBoolean2);
            outPacket.encodeInt(blinkLightOrginX);
            outPacket.encodeInt(blinkLightOrginY);
            outPacket.encodeInt(blinkLightDestX);
            outPacket.encodeInt(blinkLightDestY);
        }
    }
}

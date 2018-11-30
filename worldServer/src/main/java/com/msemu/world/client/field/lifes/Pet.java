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

package com.msemu.world.client.field.lifes;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.field.lifes.movement.IMovement;
import com.msemu.world.enums.FieldObjectType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class Pet extends Life {
    private int id;
    private int idx;
    private String name;
    private long petLockerSN;
    private int hue;
    private short giantRate;
    private boolean transformed;
    private boolean reinforced;

    public Pet(int objectId) {
        setObjectId(objectId);
    }

    public int getActiveSkillCoolTime() {
        return -3;
    }


    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeInt(getId());
        outPacket.encodeString(getName());
        outPacket.encodeLong(getPetLockerSN());
        outPacket.encodePosition(getPosition());
        outPacket.encodeByte(getAction());
        outPacket.encodeShort(getFh());
        outPacket.encodeInt(getHue());
        outPacket.encodeShort(getGiantRate());
        outPacket.encodeByte(isTransformed());
        outPacket.encodeByte(isReinforced());
    }

    @Override
    public FieldObjectType getFieldObjectType() {
        return FieldObjectType.PET;
    }

    @Override
    public void enterScreen(GameClient client) {

    }

    @Override
    public void outScreen(GameClient client) {

    }

    @Override
    public void move(List<IMovement> movements) {

    }
}


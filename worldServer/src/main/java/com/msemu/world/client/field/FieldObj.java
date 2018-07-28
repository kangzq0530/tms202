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

package com.msemu.world.client.field;

import com.msemu.commons.data.templates.field.FieldObjectInfo;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.FieldObjectType;
import lombok.Getter;

import java.util.List;

/**
 * Created by Weber on 2018/5/12.
 */
public class FieldObj extends AbstractFieldObject {
    @Getter
    public FieldObjectInfo data;


    public FieldObj(FieldObjectInfo data) {
        this.data = data;
        this.setPosition(new Position(data.getX1(), data.getY1()));
    }

    public boolean isMove() {
        return getData().isMove();
    }

    public int getX1() {
        return getData().getX1();
    }

    public int getX2() {
        return getData().getX2();
    }

    public int getY1() {
        return getData().getY1();
    }

    public int getY2() {
        return getData().getY2();
    }

    public int getStart() {
        return getData().getStart();
    }

    public int getSpeed() {
        return getData().getSpeed();
    }

    public int getR() {
        return getData().getR();
    }

    public String getName() {
        return getData().getName();
    }

    public List<Integer> getSns() {
        return getData().getSns();
    }

    @Override
    public FieldObjectType getFieldObjectType() {
        return null;
    }

    @Override
    public void enterScreen(GameClient client) {

    }

    @Override
    public void outScreen(GameClient client) {

    }
}

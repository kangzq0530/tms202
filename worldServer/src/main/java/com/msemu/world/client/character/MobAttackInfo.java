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

package com.msemu.world.client.character;

import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/12.
 */
@Getter
@Setter
public class MobAttackInfo {
    private int objectID;
    private byte idk1;
    private byte idk2;
    private byte idk3;
    private byte idk4;
    private byte idk5;
    private byte calcDamageStatIndex;
    private int rcDstX;
    private int rectRight;
    private int oldPosX;
    private int oldPosY;
    private int hpPercent;
    private long[] damages;
    private int mobUpDownYRange;
    private byte type;
    private String currentAnimationName;
    private int animationDeltaL;
    private String[] hitPartRunTimes;
    private int templateID;
    private short idk6;
    private boolean isResWarriorLiftPress;
    private Position pos1;
    private Position pos2;
    private Rect rect;
    private int idkInt;
    private byte byteIdk1;
    private byte byteIdk2;
    private byte byteIdk3;
    private byte byteIdk4;
    private byte byteIdk5;
    private int psychicLockInfo;
    private byte rocketRushInfo;
}

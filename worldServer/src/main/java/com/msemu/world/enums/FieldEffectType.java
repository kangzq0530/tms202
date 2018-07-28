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

package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/13.
 */
public enum FieldEffectType {
    FieldEffectSummon(0),
    Tremble(1),
    Object(2),
    ObjectDisable(3),
    FieldEffectScreen(4),
    PlaySound(5),
    MobHPTag(6),
    ChangeBGM(7),
    BGMVolumeOnly(8),
    SetBGMVolume(9),
    RewardRoullet(10),
    Unk1(11),
    Unk2(12),
    ScreenDelayed(13),
    ScreenEffect(14),
    ScreenFloatingEffect(15),
    CreateWindowMaybeidk(16),
    SetGrey(17),
    OnOffLayer(18),
    SomeAnimation(19),
    MoreAnimation(20),
    RemoveAnimation(21),
    ChangeColor(22),
    StageClearExpOnly(23),
    EvenMoreAnimation(24),
    SkeletonAnimation(25),
    OneTimeSkeletonAnimation(26),;

    private byte value;

    FieldEffectType(int val) {
        this.value = (byte) val;
    }

    public byte getValue() {
        return value;
    }
}
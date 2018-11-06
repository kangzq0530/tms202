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

package com.msemu.commons.data.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/4/13.
 */
public enum EquipSpecialAttribute {
    // 道具不會被破壞
    ITEM_NOT_DESTROY(0),
    // 潛能一定成功
    ALWAYS_GRADE_UPGRADE(1),
    // 卷軸一定成功
    ALWAYS_ENCHANT_SUCCESS(2),
    ITEM_EXTENDED(3),
    SELLING_ONE_MESO(4),
    MAKING_SKILL_MEISTER_ITEM(5),
    MAKING_SKILL_MASTER_ITEM(6),
    // 裝備痕跡
    VESTIGE(7),;
    @Getter
    private final int value;

    EquipSpecialAttribute(int index) {
        this.value = (1 << index);
    }

    public int getValue() {
        return value;
    }
}


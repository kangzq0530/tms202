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

import com.msemu.world.client.character.quest.act.QuestExpAction;
import com.msemu.world.client.character.quest.act.QuestItemAction;
import com.msemu.world.client.character.quest.act.QuestMoneyAction;
import com.msemu.world.client.character.quest.act.QuestPopAction;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/13.
 */
public enum QuestActionType {
    EXP(0),
    ITEM(1),
    MONEY(2),
    POP(3);

    private byte val;

    QuestActionType(int val) {
        this.val = (byte) val;
    }

    public static QuestActionType getQPRTByObj(Object o) {
        return o instanceof QuestExpAction ? EXP :
                o instanceof QuestItemAction ? ITEM :
                        o instanceof QuestMoneyAction ? MONEY :
                                o instanceof QuestPopAction ? POP : null;
    }

    public static QuestActionType getQPRTByVal(byte val) {
        return Arrays.stream(QuestActionType.values())
                .filter(qprt -> qprt.getVal() == val).findFirst().orElse(null);
    }

    public byte getVal() {
        return val;
    }
}

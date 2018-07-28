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

import com.msemu.world.client.character.quest.req.QuestProgressItemRequirement;
import com.msemu.world.client.character.quest.req.QuestProgressLevelRequirement;
import com.msemu.world.client.character.quest.req.QuestProgressMobRequirement;
import com.msemu.world.client.character.quest.req.QuestProgressMoneyRequirement;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/13.
 */
public enum QuestProgressRequirementType {
    ITEM(0),
    LEVEL(1),
    MOB(2),
    MONEY(3);

    private byte val;

    QuestProgressRequirementType(int val) {
        this.val = (byte) val;
    }

    public static QuestProgressRequirementType getQPRTByObj(Object o) {
        return o instanceof QuestProgressItemRequirement ? ITEM :
                o instanceof QuestProgressLevelRequirement ? LEVEL :
                        o instanceof QuestProgressMobRequirement ? MOB :
                                o instanceof QuestProgressMoneyRequirement ? MONEY : null;
    }

    public static QuestProgressRequirementType getQPRTByVal(byte val) {
        return Arrays.stream(QuestProgressRequirementType.values())
                .filter(qprt -> qprt.getVal() == val).findFirst().orElse(null);
    }

    public byte getVal() {
        return val;
    }

}

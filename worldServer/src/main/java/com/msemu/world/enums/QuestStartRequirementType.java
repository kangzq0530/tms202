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

import com.msemu.world.client.character.quest.req.*;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/13.
 */
public enum QuestStartRequirementType {

    QUEST(0),
    ITEM(1),
    JOB(2),
    MARRIAGE(3),
    MAX_LEVEL(4),
    MIN_STAT(5);

    private byte val;

    QuestStartRequirementType(int val) {
        this.val = (byte) val;
    }

    public static QuestStartRequirementType getQPRTByObj(Object o) {
        return o instanceof QuestStartCompletionRequirement ? QUEST :
                o instanceof QuestStartItemRequirement ? ITEM :
                        o instanceof QuestStartJobRequirement ? JOB :
                                o instanceof QuestStartMarriageRequirement ? MARRIAGE :
                                        o instanceof QuestStartMaxLevelRequirement ? MAX_LEVEL :
                                                o instanceof QuestStartMinStatRequirement ? MIN_STAT
                                                        : null;
    }

    public static QuestStartRequirementType getQPRTByVal(byte val) {
        return Arrays.stream(QuestStartRequirementType.values())
                .filter(qprt -> qprt.getVal() == val).findFirst().orElse(null);
    }

    public byte getVal() {
        return val;
    }
}

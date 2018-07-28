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
 * Created by Weber on 2018/5/1.
 */
public enum InGameDirectionEventOpcode {

    InGameDirectionEvent_ForcedAction(0),
    InGameDirectionEvent_Delay(1),
    InGameDirectionEvent_EffectPlay(2),
    InGameDirectionEvent_ForcedInput(3),
    InGameDirectionEvent_PatternInputRequest(4),
    InGameDirectionEvent_CameraMove(5),
    InGameDirectionEvent_CameraOnCharacter(6),
    InGameDirectionEvent_CameraZoom(7),
    InGameDirectionEvent_CameraReleaseFromUserPoint(8),
    InGameDirectionEvent_VansheeMode(9),
    InGameDirectionEvent_FaceOff(10),
    InGameDirectionEvent_Monologue(11),
    InGameDirectionEvent_MonologueScroll(12),
    InGameDirectionEvent_AvatarLookSet(13),
    InGameDirectionEvent_RemoveAdditionalEffect(14),
    InGameDirectionEvent_ForcedMove(15),
    InGameDirectionEvent_ForcedFlip(16),
    InGameDirectionEvent_InputUI(17),;

    private final int value;

    private InGameDirectionEventOpcode(int value) {
        this.value = value;
    }

    public static InGameDirectionEventOpcode getType(int type) {
        for (InGameDirectionEventOpcode dt : values()) {
            if (dt.getValue() == type) {
                return dt;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}

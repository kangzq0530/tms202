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

package com.msemu.core.network.packets.outpacket.user.local;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.enums.InGameDirectionEventOpcode;

/**
 * Created by Weber on 2018/5/1.
 */
public class LP_InGameDirectionEvent extends OutPacket<GameClient> {

    public LP_InGameDirectionEvent(InGameDirectionEventOpcode type, String data, int[] value) {
        super(OutHeader.LP_UserInGameDirectionEvent);
        encodeByte(type.getValue());
        switch (type) {
            case InGameDirectionEvent_ForcedAction:
                encodeInt(value[0]);
                if (value[0] <= 0x553) {
                    encodeInt(value[1]);
                }
                break;
            case InGameDirectionEvent_Delay:
                encodeInt(value[0]);
                break;
            case InGameDirectionEvent_EffectPlay:
                encodeString(data);
                encodeInt(value[0]);
                encodeInt(value[1]);
                encodeInt(value[2]);
                encodeByte(value[3]);
                if (value[3] > 0) {
                    encodeInt(value[5]);
                }
                encodeByte(value[4]);
                if (value[4] > 0) {
                    encodeInt(value[6]);
                    encodeByte(value[6] > 0 ? 0 : 1); // 暫時解決
                    encodeByte(value[7]);
                }
                break;
            case InGameDirectionEvent_ForcedInput:
                encodeInt(value[0]);
                break;
            case InGameDirectionEvent_PatternInputRequest:
                encodeString(data);
                encodeInt(value[0]);
                encodeInt(value[1]);
                encodeInt(value[2]);
                break;
            case InGameDirectionEvent_CameraMove:
                encodeByte(value[0]);
                encodeInt(value[1]);
                if (value[1] > 0) {
                    if (value[0] == 0) {
                        encodeInt(value[2]);
                        encodeInt(value[3]);
                    }
                }
                break;
            case InGameDirectionEvent_CameraOnCharacter:
                encodeByte(value[0]);
                break;
            case InGameDirectionEvent_CameraZoom:
                encodeInt(value[0]);
                encodeInt(value[1]);
                encodeInt(value[2]);
                encodeInt(value[3]);
                encodeInt(value[4]);
                break;
            case InGameDirectionEvent_CameraReleaseFromUserPoint:
                // CCameraWork::ReleaseCameraFromUserPoint
                break;
            case InGameDirectionEvent_VansheeMode://是否隱藏角色[1 - 隱藏, 0 - 顯示]
                encodeByte(value[0]);
                break;
            case InGameDirectionEvent_FaceOff:
                encodeInt(value[0]);
                break;
            case InGameDirectionEvent_Monologue:
                encodeString(data);
                encodeByte(value[0]);
                break;
            case InGameDirectionEvent_MonologueScroll:
                encodeString(data);
                encodeByte(value[0]);
                encodeShort(value[1]);
                encodeInt(value[2]);
                encodeInt(value[3]);
                break;
            case InGameDirectionEvent_AvatarLookSet:
                encodeByte(value[0]);
                for (int i = 0; i >= value[0]; i++) {
                    encodeInt(value[1]); // 要重寫
                }
                break;
            case InGameDirectionEvent_RemoveAdditionalEffect:
                break;
            case InGameDirectionEvent_ForcedMove:
                encodeInt(value[0]);
                encodeInt(value[1]);
                break;
            case InGameDirectionEvent_ForcedFlip:
                encodeInt(value[0]);
                break;
            case InGameDirectionEvent_InputUI:
                encodeByte(value[0]);
                break;
            default:
                System.out.println("CField.getDirectionInfo() is Unknow mod :: [" + type + "]");
                break;
        }
    }
}

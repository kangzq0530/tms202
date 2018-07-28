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

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_FriendResult;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.friends.FriendList;
import com.msemu.world.client.character.friends.response.IncFriendMaxCountUnknown;
import com.msemu.world.enums.FriendResultType;

/**
 * Created by Weber on 2018/5/11.
 */
public class CP_FriendRequest extends InPacket<GameClient> {

    private FriendResultType type;
    private int otherCharID;
    private String otherCharName;
    private String otherCharGroup;
    private String otherCharNickName;

    public CP_FriendRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

        type = FriendResultType.getByValue(decodeByte());

        if (type == null)
            return;

        switch (type) {
            case FriendReq_DeleteFriend: {
                otherCharID = decodeInt();
                break;
            }
        }

    }

    @Override
    public void runImpl() {
        if (type == null)
            return;
        GameClient client = getClient();
        Character chr = client.getCharacter();
        FriendList friendList = chr.getFriendList();
        switch (type) {
            case FriendReq_SetFriend: {
                break;
            }
            case FriendReq_DeleteFriend: {
                break;
            }
            case FriendReq_IncMaxCount: {
                int maxCount = friendList.getMaxCount();
                if (maxCount >= 100 || chr.getMoney() < 50000) {
                    // TODO 訊息 沒有楓幣
                    return;
                }
                chr.addMoney(-50000);
                friendList.addMaxCount(5);
                client.write(new LP_FriendResult(new IncFriendMaxCountUnknown()));
                break;
            }
        }

    }
}

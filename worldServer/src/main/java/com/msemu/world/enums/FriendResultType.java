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
 * Created by Weber on 2018/5/7.
 */
public enum FriendResultType {
    FriendReq_LoadFriend(0x0),
    FriendReq_SetFriend(0x1),
    FriendReq_AcceptFriend(0x2),
    FriendReq_AcceptAccountFriend(0x3),
    FriendReq_DeleteFriend(0x4),
    FriendReq_DeleteAccountFriend(0x5),
    FriendReq_RefuseFriend(0x6),
    FriendReq_RefuseAccountFriend(0x7),
    FriendReq_NotifyLogin(0x8),
    FriendReq_NotifyLogout(0x9),
    FriendReq_IncMaxCount(0xA),
    FriendReq_ConvertAccountFriend(0xB),
    FriendReq_ModifyFriend(0xC),
    FriendReq_ModifyFriendGroup(0xD),
    FriendReq_ModifyAccountFriendGroup(0xE),
    FriendReq_SetOffline(0xF),
    FriendReq_SetOnline(0x10),
    FriendReq_SetBlackList(0x11),
    FriendReq_DeleteBlackList(0x12),
    FriendReq_LoadFriendPointInfo(0x13),
    FriendReq_LoadFriendChatN(0x14),
    FriendReq_InviteEventBestFriend(0x15),
    FriendReq_AcceptEventBestFriend(0x16),
    FriendReq_RefuseEventBestFriend(0x17),

    FriendRes_LoadFriend_Done(0x17),
    FriendRes_NotifyChange_FriendInfo(0x18),
    FriendRes_Invite(0x19),
    FriendRes_SetFriend_Done(0x1A),
    FriendRes_SetFriend_FullMe(0x1B),
    FriendRes_SetFriend_FullOther(0x1C),
    FriendRes_SetFriend_AlreadySet(0x1D),
    FriendRes_SetFriend_AlreadyRequested(0x1E),
    FriendRes_SetFriend_Ready(0x1F),
    FriendRes_SetFriend_CantSelf(0x20),
    FriendRes_SetFriend_Master(0x21),
    FriendRes_SetFriend_UnknownUser(0x22),
    FriendRes_SetFriend_Unknown(0x23),
    FriendRes_SetFriend_RemainCharacterFriend(0x24),
    FriendRes_SetMessengerMode(0x25),
    FriendRes_SendSingleFriendInfo(0x26),
    FriendRes_AcceptFriend_Unknown(0x27),
    FriendRes_DeleteFriend_Done(0x28),
    FriendRes_DeleteFriend_Unknown(0x29),
    FriendRes_Notify(0x2A),
    FriendRes_NotifyNewFriend(0x2B),
    FriendRes_IncMaxCount_Done(0x2C),
    FriendRes_IncMaxCount_Unknown(0x2D),
    FriendRes_SetFriend_Done_ForFarm(0x2E),
    FriendRes_Invite_ForFarm(0x2F),
    FriendRes_Accept_ForFarm(0x30),
    FriendRes_SetFriend_BlockedBehavior(0x31),
    FriendRes_Notice_Deleted(0x32),
    FriendRes_InviteEventBestFriend(0x33),
    FriendRes_RefuseEventBestFriend(0x34),
    StarFriend_Req_UpdateOnOffState(0x35),
    StarFriend_Req_GetLocalList(0x36),
    StarFriend_Req_SetFriends(0x37),
    StarFriend_Req_GoToFriend(0x38),
    StarFriend_Req_FollowBlock(0x39),
    StarFriend_Res_LoadFriend_Done(0x3A),
    StarFriend_Res_SetFriend_FullMe(0x3B),
    StarFriend_Res_SetFriend_FullMe2(0x3C),
    StarFriend_Res_SetFriend_FullOther(0x3D),
    StarFriend_Res_SetFriend_AlreadySet(0x3E),
    StarFriend_Res_SetFriend_AlreadyRequested(0x3F),
    StarFriend_Res_SetFriend_UnknownUser(0x40),
    StarFriend_Res_SetFriend_Unknown(0x41),
    StarFriend_Res_SetFriend_Master(0x42),
    StarFriend_Res_SetFriend_offline(0x43),
    StarFriend_Res_SendSingleFriendInfo(0x44),
    StarFriend_Res_SendSingleFriendInfoForRename(0x45),
    StarFriend_Res_DeleteFriend_Done(0x46),
    StarFriend_Res_DeleteFriend_Unknown(0x47),
    StarFriend_Res_LocalFriendListResult(0x48),
    StarFriend_Res_UpdateFriendPoint_Done(0x49),
    StarFriend_Res_UpdateFriendPoint_Fail(0x4A),
    StarFriend_Res_UpdateFriendPointAll_Done(0x4B),
    StarFriend_Res_Follow_fail_R_area(0x4C),
    StarFriend_Res_Follow_fail_R_blocked(0x4D),
    StarFriend_Res_Follow_fail_R_NotFound(0x4E),
    StarFriend_Res_Follow_fail_R_unknown(0x4F),
    StarFriend_Res_LoadFriendInfo_Fail(0x50),
    StarFriend_Res_FriendInRPGWorld(0x51),;


    private int value;

    FriendResultType(int code) {
        this.value = code;
    }

    public static FriendResultType getByValue(int value) {
        for (FriendResultType type : values()) {
            if (type.getValue() == value)
                return type;
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}

/**
 * enum $B6C7F635E0F6EF61E4983CA6F49772DF
 * {
 * <p>
 * };
 */

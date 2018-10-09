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

import com.msemu.commons.data.enums.InvType;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.miniroom.LP_MiniRoom;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.Inventory;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.miniroom.MiniRoom;
import com.msemu.world.client.character.miniroom.MiniRoomFactory;
import com.msemu.world.client.character.miniroom.MiniRoomInvitation;
import com.msemu.world.client.character.miniroom.TradeRoom;
import com.msemu.world.client.character.miniroom.actions.MiniRoomEnterResultAction;
import com.msemu.world.client.character.miniroom.actions.MiniRoomInviteResultAction;
import com.msemu.world.client.character.miniroom.actions.MiniRoomInvtie;
import com.msemu.world.client.field.Field;
import com.msemu.world.enums.MiniRoomEnterResult;
import com.msemu.world.enums.MiniRoomInviteResult;
import com.msemu.world.enums.MiniRoomOperation;
import com.msemu.world.enums.MiniRoomType;
import com.msemu.world.service.MiniRoomService;

public class CP_MiniRoom extends InPacket<GameClient> {

    private int opcodeValue, miniRoomTypeValue, invTypeValue,
            srcSlot, quantity, targetSlot, targetCharacterId,
            invitationSN, errorCode;

    public CP_MiniRoom(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

        opcodeValue = decodeByte();

        final MiniRoomOperation opcode = MiniRoomOperation.getByValue(opcodeValue);
        switch (opcode) {
            case TRP_PutItem1:
            case TRP_PutItem2:
            case TRP_PutItem3:
            case TRP_PutItem4:
                invTypeValue = decodeByte();
                srcSlot = decodeShort();
                quantity = decodeShort();
                targetSlot = decodeByte();
                break;
            case MRP_Create:
                miniRoomTypeValue = decodeByte();
                break;
            case MRP_Invite:
                targetCharacterId = decodeInt();
                break;
            case MRP_InviteResult:
                invitationSN = decodeInt();
                errorCode = decodeByte();
                break;
            case MRP_Enter:
                invitationSN = decodeInt();
                skip(2);
                break;

        }
    }

    @Override
    public void runImpl() {

        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();
        final MiniRoomOperation opcode = MiniRoomOperation.getByValue(opcodeValue);
        MiniRoom miniRoom;

        switch (opcode) {
            case TRP_PutItem1:
            case TRP_PutItem2:
            case TRP_PutItem3:
            case TRP_PutItem4:
                miniRoom = chr.getMiniRoom();
                if (miniRoom instanceof TradeRoom) {
                    final InvType invType = InvType.getInvTypeByValue(invTypeValue);
                    final Inventory inventory = chr.getInventoryByType(invType);
                    final Item item = inventory.getItemBySlot(srcSlot);
                    if (item == null || item.getQuantity() < quantity) {
                        return;
                    }
//
                }
                break;
            case TRP_PutMoney1:
            case TRP_PutMoney2:
            case TRP_PutMoney3:
            case TRP_PutMoney4:
                break;
            case TRP_Trade1: // 確認交易
            case TRP_Trade2:
            case TRP_Trade3:
            case TRP_Trade4:
                miniRoom = chr.getMiniRoom();
                if (miniRoom instanceof TradeRoom) {
                    ((TradeRoom) miniRoom).completeTrade(chr);
                }
                break;

            case MRP_Create:
                miniRoom = chr.getMiniRoom();
                // 檢查是否能創立
                if (miniRoom != null) {
                    chr.write(new LP_MiniRoom(new MiniRoomEnterResultAction(
                            MiniRoomEnterResult.MG_AlreadyPlaying, "")));
                } else {
                    final MiniRoomType miniRoomType = MiniRoomType.getByValue(miniRoomTypeValue);
                    miniRoom = MiniRoomFactory.getMiniRoom(miniRoomType);
                    if (miniRoom != null) {
                        field.addFieldObject(miniRoom);
                        miniRoom.create(chr);
                    }
                    break;
                }
            case MRP_Invite: {
                // 檢查是否能邀請
                // TODO 拒絕邀請狀態
                final Character invitee = field.getCharacterById(targetCharacterId);
                if (invitee == null) {
                    chr.write(new LP_MiniRoom(new MiniRoomInviteResultAction(MiniRoomInviteResult.NoCharacter, true, "")));
                } else {
                    final MiniRoomInvitation invitation = MiniRoomService.getInstance().createInvitation(chr, invitee);
                    invitee.write(new LP_MiniRoom(new MiniRoomInvtie(chr.getMiniRoom(), invitee, invitation)));
                }
                break;
            }
            case MRP_InviteResult: {
                final MiniRoomInviteResult inviteResult = MiniRoomInviteResult.getByValue(errorCode);
                if (inviteResult == MiniRoomInviteResult.Rejected) {
                    final MiniRoomInvitation invitation = MiniRoomService.getInstance().getInvitationByInvitee(chr);
                    if (invitation == null || invitation.getId() != invitationSN) {
                        chr.enableActions();
                    } else {
                        final Character inviter = invitation.getInviter();
                        final Character invitee = invitation.getInvitee();
                        inviter.write(new LP_MiniRoom(new MiniRoomInviteResultAction(MiniRoomInviteResult.Rejected, false, invitee.getName())));
                    }
                }
                break;
            }
            case MRP_Enter:
                MiniRoomInvitation invitation = MiniRoomService.getInstance().getInvitationByInvitee(chr);
                miniRoom = invitation != null && invitation.getId() == invitationSN ? invitation.getInviter().getMiniRoom() : null;
                if (miniRoom == null) {
                    chr.write(new LP_MiniRoom(new MiniRoomEnterResultAction(MiniRoomEnterResult.MR_NoRoom, "")));
                } else {
                    MiniRoomService.getInstance().removeInvitation(chr);
                    miniRoom.enter(chr);
                }
                break;
            case MRP_Leave:
                miniRoom = chr.getMiniRoom();
                if (miniRoom != null)
                    miniRoom.leave(chr);
                break;
        }

    }

    private MiniRoomInviteResult canInvite(Character chr, Character invitee) {
        final boolean inviteeExists = invitee != null;
        if (!inviteeExists)
            return MiniRoomInviteResult.NoCharacter;
        final boolean hasInvited = MiniRoomService.getInstance().hasInvitation(invitee);
        if (hasInvited)
            return MiniRoomInviteResult.Busy;
        final boolean sameField = chr.getField().equals(invitee.getField());
        if (!sameField)
            return MiniRoomInviteResult.NotSameField;
        return MiniRoomInviteResult.Success;
    }
}

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

import com.msemu.commons.data.templates.field.Portal;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.local.LP_SetDirectionMode;
import com.msemu.core.network.packets.outpacket.user.local.LP_SetInGameDirectionMode;
import com.msemu.world.Channel;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.field.Field;
import com.msemu.world.enums.ReviveType;
import com.msemu.world.enums.Stat;

/**
 * Created by Weber on 2018/5/2.
 */
public class CP_UserTransferFieldRequest extends InPacket<GameClient> {

    private int targetMapId;

    private String targetPortalName;

    private int tick;

    private ReviveType reviveType;

    public CP_UserTransferFieldRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        decodeByte();
        targetMapId = decodeInt();
        targetPortalName = decodeString();
        if (available() >= 7)
            tick = decodeInt();
        decodeByte();
        final short reviveValue = decodeShort();
        reviveType = ReviveType.getByValue(reviveValue);
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        Channel channel = getClient().getChannelInstance();
        Field field = chr.getField();


        if (targetMapId != -1) {

            if (chr.isAlive()) {
                int divi = field.getFieldId() / 100;
                boolean unlock = false;
                boolean warp = false;
                if ((divi / 10 == 1020) && (targetMapId == 1020000 || targetMapId == 4000026)) {
                    unlock = true;
                    warp = true;
                }
                if (unlock) {
                    getClient().write(new LP_SetDirectionMode(false));
                    getClient().write(new LP_SetInGameDirectionMode(false));
                }
                if (warp) {
                    chr.warp(channel.getField(targetMapId));
                }
            } else {
                // 死亡
                Field returnMap = channel.getField(chr.getField().getReturnMap());
                chr.setStat(Stat.HP, 50);
                switch (reviveType) {
                    case Normal:
                        break;
                    case UpgradeTomb:
                        if (chr.hasItem(5510000)) {
                            chr.consumeItem(5510000);
                            returnMap = chr.getField();
                        }
                        break;
                }

                chr.warp(returnMap);
            }

        } else {
            Portal portal = field.getPortalByName(targetPortalName);
            if (portal == null) {
                chr.enableActions();
            } else {
                chr.warpPortal(portal);
            }
        }
    }
}

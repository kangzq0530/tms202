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
import com.msemu.world.client.character.Character;
import com.msemu.world.client.enchant.singleui.HyperUpgradeTabHandler;
import com.msemu.world.client.enchant.singleui.UpgradeScrollTabHandler;
import com.msemu.world.enums.EquipEnchantType;
import org.slf4j.LoggerFactory;

public class CP_UserEquipmentEnchantWithSingleUIRequest extends InPacket<GameClient> {


    private int updateTick;
    private int typeValue;

    private int bagIndex;

    private boolean safeUpgrade;

    private int scrollIndex;

    public CP_UserEquipmentEnchantWithSingleUIRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        typeValue = decodeByte();

        final EquipEnchantType enchantType = EquipEnchantType.getByValue(typeValue);

        switch (enchantType) {
            case SCROLL_UPGRADE:
                updateTick = decodeInt();
                bagIndex = decodeShort();
                scrollIndex = decodeInt();
                break;
            case DISPLAY_SCROLL_UPGRADE:
                bagIndex = decodeInt();
                break;
            case DISPLAY_HYPER_UPGRADE:
                bagIndex = decodeInt();
                safeUpgrade = decodeByte() > 0;
                break;
            default:
                LoggerFactory.getLogger(CP_UserEquipmentEnchantWithSingleUIRequest.class)
                        .warn("Unhandled enchantType : " + typeValue);
        }
    }

    @Override
    public void runImpl() {
        final Character chr = getClient().getCharacter();
        final EquipEnchantType enchantType = EquipEnchantType.getByValue(typeValue);

        switch (enchantType) {
            case SCROLL_UPGRADE:
                UpgradeScrollTabHandler.getInstance().onUpgrade(chr, scrollIndex, bagIndex);
                break;
            case DISPLAY_SCROLL_UPGRADE:
                UpgradeScrollTabHandler.getInstance().onDisplay(chr, bagIndex);
                break;

            case DISPLAY_HYPER_UPGRADE:
                HyperUpgradeTabHandler.getInstance().onDisplay(chr, bagIndex, safeUpgrade);
                break;

        }
    }
}

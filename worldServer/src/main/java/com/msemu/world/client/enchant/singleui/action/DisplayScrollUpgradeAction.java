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

package com.msemu.world.client.enchant.singleui.action;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.configs.EquipEnchantConfig;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.enchant.singleui.ScrollUpgradeInfo;
import com.msemu.world.enums.EquipEnchantType;

import java.util.ArrayList;
import java.util.List;

public class DisplayScrollUpgradeAction implements ISingleUIEnchantAction {

    private final List<ScrollUpgradeInfo> scrolls = new ArrayList<>();

    public DisplayScrollUpgradeAction(List<ScrollUpgradeInfo> scrolls) {
        this.scrolls.addAll(scrolls);
    }

    @Override
    public EquipEnchantType getType() {
        return EquipEnchantType.DISPLAY_SCROLL_UPGRADE;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(EquipEnchantConfig.ENABLE_FEVER_TIME);
        outPacket.encodeByte(scrolls.size());
        for (ScrollUpgradeInfo scroll : scrolls) {
            outPacket.encodeInt(scroll.getIcon().getValue());
            outPacket.encodeString(scroll.getTitle());
            outPacket.encodeInt(scroll.getType().getValue());
            outPacket.encodeInt(scroll.getOption());
            outPacket.encodeInt(scroll.getStatsMask());
            scroll.getStats().forEach((stat, value) -> outPacket.encodeInt(value));
            outPacket.encodeInt(scroll.getCost());
            outPacket.encodeInt(scroll.getCostBefore());
            outPacket.encodeByte(false);
        }
    }
}

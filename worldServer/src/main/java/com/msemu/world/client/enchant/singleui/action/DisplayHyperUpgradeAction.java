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

import com.msemu.commons.data.enums.EnchantStat;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.enchant.singleui.HyperUpgradeInfo;
import com.msemu.world.enums.EquipEnchantType;

import java.util.Map;

public class DisplayHyperUpgradeAction implements ISingleUIEnchantAction {


    private final HyperUpgradeInfo info;

    public DisplayHyperUpgradeAction(HyperUpgradeInfo hyperUpgradeInfo) {
        this.info = hyperUpgradeInfo;
    }

    @Override
    public EquipEnchantType getType() {
        return EquipEnchantType.DISPLAY_HYPER_UPGRADE;
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(info.isDownGradable());
        outPacket.encodeLong(info.getMoneyCost());
        outPacket.encodeLong(info.getBeforeDiscount());
        outPacket.encodeLong(info.getBeforeMVP());
        outPacket.encodeByte(info.isMvp());
        outPacket.encodeByte(info.isPc());
        outPacket.encodeLong(info.getMaplePointCost());
        outPacket.encodeLong(info.getPreventDestroyCost()); // 12星-16星用楓點防止破壞的價格
        outPacket.encodeInt(info.getSuccessProp());
        outPacket.encodeInt(info.getDestroyProp());
        outPacket.encodeInt(info.getSuccessPropBefore()); // successPropBefore
        outPacket.encodeInt(0); //
        outPacket.encodeByte(info.isChanceTime());
        int mask = 0;
        for (Map.Entry<EnchantStat, Integer> entry : info.getStats().entrySet()) {
            mask |= entry.getKey().getValue();
        }
        outPacket.encodeInt(mask);
        info.getStats().forEach((key, value) -> {
            outPacket.encodeInt(value);
        });
    }
}

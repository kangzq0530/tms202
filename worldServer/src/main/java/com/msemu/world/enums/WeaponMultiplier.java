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
 * Created by Weber on 2018/5/20.
 */
public enum WeaponMultiplier {
    沒有武器(0.0f, 0),
    閃亮克魯(1.2f, 25),
    靈魂射手(1.7f, 15),
    魔劍(1.3f, 20),
    能量劍(1.3125f, 20),
    幻獸棍棒(1.34f, 20),
    單手劍(1.2f, 20),
    單手斧(1.2f, 20),
    單手棍(1.2f, 20),
    短劍(1.3f, 20),
    雙刀(1.3f, 20),
    手杖(1.3f, 25),
    短杖(1.0f, 25),//RED改版後冒險家法師係數為1.2
    長杖(1.0f, 25),//RED改版後冒險家法師係數為1.2
    雙手劍(1.34f, 20),
    雙手斧(1.34f, 20),
    雙手棍(1.34f, 20),
    槍(1.49f, 20),
    矛(1.49f, 20),
    弓(1.3f, 15),
    弩(1.35f, 15),
    拳套(1.75f, 15),
    指虎(1.7f, 20),
    火槍(1.5f, 15),
    雙弩槍(1.3f, 15), //beyond op
    加農炮(1.5f, 15),
    太刀(1.25f, 20),
    扇子(1.35f, 25),
    琉(1.49f, 20),
    璃(1.34f, 20),
    ESP限制器(1.0f, 20),
    重拳槍(1.7f, 20),
    未知(0.0f, 0),;
    private final float damageMultiplier; // 武器係數
    private final int baseMastery; // 初始武器熟練度

    WeaponMultiplier(final float maxDamageMultiplier, int baseMastery) {
        this.damageMultiplier = maxDamageMultiplier;
        this.baseMastery = baseMastery;
    }

    public final float getMaxDamageMultiplier() {
        return damageMultiplier;
    }

    public final int getBaseMastery() {
        return baseMastery;
    }
}

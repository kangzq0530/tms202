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

import com.msemu.commons.data.enums.ItemSpecStat;
import com.msemu.commons.data.templates.ItemTemplate;
import com.msemu.commons.enums.FieldLimit;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.wvscontext.LP_TemporaryStatSet;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.stats.CharacterTemporaryStat;
import com.msemu.world.client.character.stats.Option;
import com.msemu.world.client.character.stats.TemporaryStatManager;
import com.msemu.world.client.field.Field;
import com.msemu.world.enums.Stat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Weber on 2018/5/21.
 */
public class CP_UserStatChangeItemUseRequest extends InPacket<GameClient> {

    private int updateTick;
    private int slot;
    private int itemID;

    public CP_UserStatChangeItemUseRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        this.updateTick = decodeInt();
        this.slot = decodeShort();
        this.itemID = decodeInt();
    }

    @Override
    public void runImpl() {

        final Character chr = getClient().getCharacter();
        final Field field = chr.getField();

        final Item usedItem = chr.getConsumeInventory()
                .getItemBySlot(slot);

        final int usedCoolTime = field.getConsumeItemCoolTime();

        final boolean useLimited = field.checkFieldLimit(FieldLimit.ConsumeStatChangeItem);

        if (usedItem == null || usedItem.getItemId() != itemID || usedItem.getQuantity() < 1 || useLimited) {
            chr.enableActions();
            return;
        }

        if (usedCoolTime > 0 && chr.getLastUseStatChangeItemTime()
                .plus(usedCoolTime, ChronoUnit.SECONDS)
                .isBefore(LocalDateTime.now())) {
            //TODO dropMessage 目前無法使用。
            chr.enableActions();
            return;
        }

        ItemTemplate itemInfo = usedItem.getTemplate();

        // specstat 感覺可ˇ跟 skillStat 合併

        TemporaryStatManager tsm = chr.getTemporaryStatManager();

        final int tTerm = itemInfo.getItemSpec().getSpecStats().getOrDefault(ItemSpecStat.time, 0) / 1000;
        chr.consumeItem(usedItem, 1);
        itemInfo.getItemSpec().getSpecStats()
                .forEach((stat, value) -> {
                    Option option;

                    switch (stat) {
                        case hp:
                            chr.addStat(Stat.HP, value);
                            break;
                        case mp:
                            chr.addStat(Stat.MP, value);
                            break;
                        case speed:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.Speed, option);
                            break;
                        case mhpR:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.MaxMP, option);
                            break;
                        case mmpR:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.MaxHP, option);
                            break;
                        case pad:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.PAD, option);
                            break;
                        case pdd:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.PDD, option);
                            break;
                        case acc:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.ACC, option);
                            break;
                        case eva:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.EVA, option);
                            break;
                        case booster:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.Booster, option);
                            break;
                        case thaw:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.Thaw, option);
                            break;
                        case expBuff:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.ExpBuffRate, option);
                            break;
                        case itemupbyitem:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.ItemUpByItem, option);
                            break;
                        case mesoupbyitem:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.MesoUpByItem, option);
                            break;
                        case berserk:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.DojangBerserk, option);
                            break;
                        case inflation:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.Inflation, option);
                            break;
                        case str:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.STR, option);
                            break;
                        case dex:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.DEX, option);
                            break;
                        case inte:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.INT, option);
                            break;
                        case luk:
                            option = new Option();
                            option.rOption = -itemID;
                            option.nOption = value;
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.LUK, option);
                            break;
                        case indiePad:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndiePAD, option);
                            break;
                        case indiePdd:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndiePDD, option);
                            break;
                        case indieMad:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndieMAD, option);
                            break;
                        case imhp:
                            option = new Option();
                            option.nOption = -itemID;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tOption = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndieMHP, option);
                            break;
                        case immp:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndieMMP, option);
                            break;
                        case indieMhpR:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndieMHPR, option);
                            break;
                        case indieMmpR:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndieMMPR, option);
                            break;
                        case indieMhp:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndieMHP, option);
                            break;
                        case indieMmp:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndieMMP, option);
                            break;
                        case incPVPDamage:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.PVPDamage, option);
                            break;
                        case indieJump:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndieJump, option);
                            break;
                        case indieSpeed:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndieSpeed, option);
                            break;
                        case indieAcc:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndieACC, option);
                            break;
                        case indieEva:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndieEVA, option);
                            break;
                        case indieBooster:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.IndieBooster, option);
                            break;
                        case immortal:
                            option = new Option();
                            option.nKey = option.nOption = -itemID;
                            option.nValue = value;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.NotDamaged, option);
                            break;
                        case preventslip:
                            option = new Option();
                            option.nReason = -itemID;
                            option.tStart = (int) System.currentTimeMillis();
                            option.tTerm = tTerm;
                            tsm.putCharacterStatValue(CharacterTemporaryStat.Event2, option);
                            break;
                    }
                });

        getClient().write(new LP_TemporaryStatSet(tsm));

    }
}

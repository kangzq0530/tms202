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

package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

import static com.msemu.world.enums.ExpIncreaseInfoFlags.*;

/**
 * Created by Weber on 2018/4/13.
 */
@Getter
@Setter
public class ExpIncreaseInfo {
    private boolean lastHit;
    private long incEXP;
    private boolean onQuest;
    private int selectedMobBonusExp;
    private int partyBonusPercentage;
    private int questBonusRate;
    private int questBonusRemainCount;
    private int weddingBonusExp;
    private int partyBonusExp;
    private int itemBonusExp;
    private int premiumIPBonusExp;
    private int rainbowWeekEventBonusExp;
    private int boomupEventBonusExp;
    private int plusExpBuffBonusExp;
    private int psdBonusExpRate;
    private int indieBonusExp;
    private int relaxBonusExp;
    private int installItemBonusExp;
    private int aswanWinnerBonusExp;
    private int expByIncExpR;
    private int valuePackBonusExp;
    private int expByIncPQExpR;
    private int baseAddExp;
    private int bloodAllianceBonusExp;
    private int freezeHotEventBonusExp;
    private int restFieldBonusExp;
    private int restFieldExpRate;
    private int userHPRateBonusExp;
    private int fieldValueBonusExp;
    private int mobKillBonusExp;
    private int liveEventBonusExp;
    private int internetCafeBestFriendBonusExp;
    private int frozenFieldBonusExp;
    private int partyBonusExp2;
    private int cakePieBonusExp;
    private int pvpBonusExp;
    private int petTrainingBonusExp;
    private int combinationItemBonusExp;
    private int combinationItemPartyBonusExp;
    private int serverExtraBonusExp;


    public long getMask() {
        long mask = 0;
        if (getSelectedMobBonusExp() > 0) {
            mask |= SelectedMobBonusExp.getValue();
        }
        if (getPartyBonusPercentage() > 0) {
            mask |= PartyBonusPercentage.getValue();
        }
        if (getWeddingBonusExp() > 0) {
            mask |= WeddingBonusExp.getValue();
        }
        if (getPartyBonusExp() > 0) {
            mask |= PartyBonusExp.getValue();
        }
        if (getItemBonusExp() > 0) {
            mask |= ItemBonusExp.getValue();
        }
        if (getPremiumIPBonusExp() > 0) {
            mask |= PremiumIPBonusExp.getValue();
        }
        if (getRainbowWeekEventBonusExp() > 0) {
            mask |= RainbowWeekEventBonusExp.getValue();
        }
        if (getBoomupEventBonusExp() > 0) {
            mask |= BoomUpEventBonusExp.getValue();
        }
        if (getPlusExpBuffBonusExp() > 0) {
            mask |= PlusExpBuffBonusExp.getValue();
        }
        if (getPsdBonusExpRate() > 0) {
            mask |= PsdBonusExpRate.getValue();
        }
        if (getIndieBonusExp() > 0) {
            mask |= IndieBonusExp.getValue();
        }
        if (getRelaxBonusExp() > 0) {
            mask |= RelaxBonusExp.getValue();
        }
        if (getInstallItemBonusExp() > 0) {
            mask |= InstallItemBonusExp.getValue();
        }
        if (getAswanWinnerBonusExp() > 0) {
            mask |= AswanWinnerBonusExp.getValue();
        }
        if (getExpByIncExpR() > 0) {
            mask |= ExpByIncExpR.getValue();
        }
        if (getValuePackBonusExp() > 0) {
            mask |= ValuePackBonusExp.getValue();
        }
        if (getExpByIncPQExpR() > 0) {
            mask |= ExpByIncPQExpR.getValue();
        }
        if (getBaseAddExp() > 0) {
            mask |= BaseAddExp.getValue();
        }
        if (getBloodAllianceBonusExp() > 0) {
            mask |= BloodAllianceBonusExp.getValue();
        }
        if (getFreezeHotEventBonusExp() > 0) {
            mask |= FreezeHotEventBonusExp.getValue();
        }
        if (getRestFieldBonusExp() > 0 || getRestFieldExpRate() > 0) {
            mask |= RestFieldExpRate.getValue();
        }
        if (getUserHPRateBonusExp() > 0) {
            mask |= UserHPRateBonusExp.getValue();
        }
        if (getFieldValueBonusExp() > 0) {
            mask |= FieldValueBonusExp.getValue();
        }
        if (getMobKillBonusExp() > 0) {
            mask |= MobKillBonusExp.getValue();
        }
        if (getLiveEventBonusExp() > 0) {
            mask |= LiveEventBonusExp.getValue();
        }

        if (getInternetCafeBestFriendBonusExp() > 0) {
            mask |= InternetCafeBestFriendBonusExp.getValue();
        }
        if (getFrozenFieldBonusExp() > 0) {
            mask |= FrozenFieldBonusExp.getValue();
        }
        if (getPartyBonusExp2() > 0) {
            mask |= PartyBonusExp2.getValue();
        }
        if (getCakePieBonusExp() > 0) {
            mask |= CakePieBonusExp.getValue();
        }
        if (getPvpBonusExp() > 0) {
            mask |= PVPBonusExp.getValue();
        }
        if (getPetTrainingBonusExp() > 0) {
            mask |= PetTrainingBounsExp.getValue();
        }
        if (getCombinationItemBonusExp() > 0) {
            mask |= CombinationItemBonusExp.getValue();
        }
        if (getCombinationItemPartyBonusExp() > 0) {
            mask |= CombinationItemPartyBonusExp.getValue();
        }
        if (getServerExtraBonusExp() > 0) {
            mask |= ServerBonusExp.getValue();
        }
        return mask;
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(isLastHit());
        outPacket.encodeLong(getIncEXP());
        outPacket.encodeByte(isOnQuest());
        outPacket.encodeLong(getMask());
        if (getSelectedMobBonusExp() > 0) {
            outPacket.encodeInt(getSelectedMobBonusExp());
        }
        if (getPartyBonusPercentage() > 0) {
            outPacket.encodeByte(getPartyBonusPercentage());
        }
        if (isOnQuest()) {
            outPacket.encodeByte(getQuestBonusRate());
        }
        if (getQuestBonusRate() > 0) {
            outPacket.encodeByte(getQuestBonusRemainCount());
        }
        if (getWeddingBonusExp() > 0) {
            outPacket.encodeInt(getWeddingBonusExp());
        }
        if (getPartyBonusExp() > 0) {
            outPacket.encodeInt(getPartyBonusExp());
        }
        if (getItemBonusExp() > 0) {
            outPacket.encodeInt(getItemBonusExp());
        }
        if (getPremiumIPBonusExp() > 0) {
            outPacket.encodeInt(getPremiumIPBonusExp());
        }
        if (getRainbowWeekEventBonusExp() > 0) {
            outPacket.encodeInt(getRainbowWeekEventBonusExp());
        }
        if (getBoomupEventBonusExp() > 0) {
            outPacket.encodeInt(getBoomupEventBonusExp());
        }
        if (getPlusExpBuffBonusExp() > 0) {
            outPacket.encodeInt(getPlusExpBuffBonusExp());
        }
        if (getPsdBonusExpRate() > 0) {
            outPacket.encodeInt(getPsdBonusExpRate());
        }
        if (getIndieBonusExp() > 0) {
            outPacket.encodeInt(getIndieBonusExp());
        }
        if (getRelaxBonusExp() > 0) {
            outPacket.encodeInt(getRelaxBonusExp());
        }
        if (getInstallItemBonusExp() > 0) {
            outPacket.encodeInt(getInstallItemBonusExp());
        }
//        if(getAswanWinnerBonusExp() > 0) {
//            outPacket.encodeByte(getAswanWinnerBonusExp());
//        }
        if (getExpByIncExpR() > 0) {
            outPacket.encodeInt(getExpByIncExpR());
        }
        if (getValuePackBonusExp() > 0) {
            outPacket.encodeInt(getValuePackBonusExp());
        }
        if (getExpByIncPQExpR() > 0) {
            outPacket.encodeInt(getExpByIncPQExpR());
        }
        if (getBaseAddExp() > 0) {
            outPacket.encodeInt(getBaseAddExp());
        }
        if (getBloodAllianceBonusExp() > 0) {
            outPacket.encodeInt(getBloodAllianceBonusExp());
        }
        if (getFreezeHotEventBonusExp() > 0) {
            outPacket.encodeInt(getFreezeHotEventBonusExp());
        }
        if (getRestFieldBonusExp() > 0 || getRestFieldExpRate() > 0) {
            outPacket.encodeInt(getRestFieldBonusExp());
            outPacket.encodeInt(getRestFieldExpRate());
        }
        if (getUserHPRateBonusExp() > 0) {
            outPacket.encodeInt(getUserHPRateBonusExp());
        }
        if (getFieldValueBonusExp() > 0) {
            outPacket.encodeInt(getFieldValueBonusExp());
        }
        if (getMobKillBonusExp() > 0) {
            outPacket.encodeInt(getMobKillBonusExp());
        }
        if (getLiveEventBonusExp() > 0) {
            outPacket.encodeInt(getLiveEventBonusExp());
        }
        if (getInternetCafeBestFriendBonusExp() > 0) {
            outPacket.encodeInt(getInternetCafeBestFriendBonusExp());
        }
        if (getFrozenFieldBonusExp() > 0) {
            outPacket.encodeInt(getFrozenFieldBonusExp());
        }
        if (getPartyBonusExp2() > 0) {
            outPacket.encodeInt(getPartyBonusExp2());
        }
        if (getCakePieBonusExp() > 0) {
            outPacket.encodeInt(getCakePieBonusExp());
        }
        if (getPvpBonusExp() > 0) {
            outPacket.encodeInt(getPvpBonusExp());
        }
        if (getPetTrainingBonusExp() > 0) {
            outPacket.encodeInt(getPetTrainingBonusExp());
        }
        if (getCombinationItemBonusExp() > 0) {
            outPacket.encodeInt(getCombinationItemBonusExp());
        }
        if (getCombinationItemPartyBonusExp() > 0) {
            outPacket.encodeInt(getCombinationItemPartyBonusExp());
        }
        if (getServerExtraBonusExp() > 0) {
            outPacket.encodeInt(getServerExtraBonusExp());
        }
    }
}

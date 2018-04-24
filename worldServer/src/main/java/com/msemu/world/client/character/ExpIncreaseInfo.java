package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;

import static com.msemu.world.enums.ExpIncreaseInfoFlags.*;

/**
 * Created by Weber on 2018/4/13.
 */
public class ExpIncreaseInfo {
    private boolean isLastHit;
    private int incEXP;
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

    public boolean isLastHit() {
        return isLastHit;
    }

    public void setLastHit(boolean lastHit) {
        isLastHit = lastHit;
    }

    public int getIncEXP() {
        return incEXP;
    }

    public void setIncEXP(int incEXP) {
        this.incEXP = incEXP;
    }

    public boolean isOnQuest() {
        return onQuest;
    }

    public void setOnQuest(boolean onQuest) {
        this.onQuest = onQuest;
    }

    public int getSelectedMobBonusExp() {
        return selectedMobBonusExp;
    }

    public void setSelectedMobBonusExp(int selectedMobBonusExp) {
        this.selectedMobBonusExp = selectedMobBonusExp;
    }

    public int getPartyBonusPercentage() {
        return partyBonusPercentage;
    }

    public void setPartyBonusPercentage(int partyBonusPercentage) {
        this.partyBonusPercentage = partyBonusPercentage;
    }

    public int getQuestBonusRate() {
        return questBonusRate;
    }

    public void setQuestBonusRate(int questBonusRate) {
        this.questBonusRate = questBonusRate;
    }

    public int getQuestBonusRemainCount() {
        return questBonusRemainCount;
    }

    public void setQuestBonusRemainCount(int questBonusRemainCount) {
        this.questBonusRemainCount = questBonusRemainCount;
    }

    public int getWeddingBonusExp() {
        return weddingBonusExp;
    }

    public void setWeddingBonusExp(int weddingBonusExp) {
        this.weddingBonusExp = weddingBonusExp;
    }

    public int getPartyBonusExp() {
        return partyBonusExp;
    }

    public void setPartyBonusExp(int partyBonusExp) {
        this.partyBonusExp = partyBonusExp;
    }

    public int getItemBonusExp() {
        return itemBonusExp;
    }

    public void setItemBonusExp(int itemBonusExp) {
        this.itemBonusExp = itemBonusExp;
    }

    public int getPremiumIPBonusExp() {
        return premiumIPBonusExp;
    }

    public void setPremiumIPBonusExp(int premiumIPBonusExp) {
        this.premiumIPBonusExp = premiumIPBonusExp;
    }

    public int getRainbowWeekEventBonusExp() {
        return rainbowWeekEventBonusExp;
    }

    public void setRainbowWeekEventBonusExp(int rainbowWeekEventBonusExp) {
        this.rainbowWeekEventBonusExp = rainbowWeekEventBonusExp;
    }

    public int getBoomupEventBonusExp() {
        return boomupEventBonusExp;
    }

    public void setBoomupEventBonusExp(int boomupEventBonusExp) {
        this.boomupEventBonusExp = boomupEventBonusExp;
    }

    public int getPlusExpBuffBonusExp() {
        return plusExpBuffBonusExp;
    }

    public void setPlusExpBuffBonusExp(int plusExpBuffBonusExp) {
        this.plusExpBuffBonusExp = plusExpBuffBonusExp;
    }

    public int getPsdBonusExpRate() {
        return psdBonusExpRate;
    }

    public void setPsdBonusExpRate(int psdBonusExpRate) {
        this.psdBonusExpRate = psdBonusExpRate;
    }

    public int getIndieBonusExp() {
        return indieBonusExp;
    }

    public void setIndieBonusExp(int indieBonusExp) {
        this.indieBonusExp = indieBonusExp;
    }

    public int getRelaxBonusExp() {
        return relaxBonusExp;
    }

    public void setRelaxBonusExp(int relaxBonusExp) {
        this.relaxBonusExp = relaxBonusExp;
    }

    public int getInstallItemBonusExp() {
        return installItemBonusExp;
    }

    public void setInstallItemBonusExp(int installItemBonusExp) {
        this.installItemBonusExp = installItemBonusExp;
    }

    public int getAswanWinnerBonusExp() {
        return aswanWinnerBonusExp;
    }

    public void setAswanWinnerBonusExp(int aswanWinnerBonusExp) {
        this.aswanWinnerBonusExp = aswanWinnerBonusExp;
    }

    public int getExpByIncExpR() {
        return expByIncExpR;
    }

    public void setExpByIncExpR(int expByIncExpR) {
        this.expByIncExpR = expByIncExpR;
    }

    public int getValuePackBonusExp() {
        return valuePackBonusExp;
    }

    public void setValuePackBonusExp(int valuePackBonusExp) {
        this.valuePackBonusExp = valuePackBonusExp;
    }

    public int getExpByIncPQExpR() {
        return expByIncPQExpR;
    }

    public void setExpByIncPQExpR(int expByIncPQExpR) {
        this.expByIncPQExpR = expByIncPQExpR;
    }

    public int getBaseAddExp() {
        return baseAddExp;
    }

    public void setBaseAddExp(int baseAddExp) {
        this.baseAddExp = baseAddExp;
    }

    public int getBloodAllianceBonusExp() {
        return bloodAllianceBonusExp;
    }

    public void setBloodAllianceBonusExp(int bloodAllianceBonusExp) {
        this.bloodAllianceBonusExp = bloodAllianceBonusExp;
    }

    public int getFreezeHotEventBonusExp() {
        return freezeHotEventBonusExp;
    }

    public void setFreezeHotEventBonusExp(int freezeHotEventBonusExp) {
        this.freezeHotEventBonusExp = freezeHotEventBonusExp;
    }

    public int getRestFieldBonusExp() {
        return restFieldBonusExp;
    }

    public void setRestFieldBonusExp(int restFieldBonusExp) {
        this.restFieldBonusExp = restFieldBonusExp;
    }

    public int getRestFieldExpRate() {
        return restFieldExpRate;
    }

    public void setRestFieldExpRate(int restFieldExpRate) {
        this.restFieldExpRate = restFieldExpRate;
    }

    public int getUserHPRateBonusExp() {
        return userHPRateBonusExp;
    }

    public void setUserHPRateBonusExp(int userHPRateBonusExp) {
        this.userHPRateBonusExp = userHPRateBonusExp;
    }

    public int getFieldValueBonusExp() {
        return fieldValueBonusExp;
    }

    public void setFieldValueBonusExp(int fieldValueBonusExp) {
        this.fieldValueBonusExp = fieldValueBonusExp;
    }

    public int getMobKillBonusExp() {
        return mobKillBonusExp;
    }

    public void setMobKillBonusExp(int mobKillBonusExp) {
        this.mobKillBonusExp = mobKillBonusExp;
    }

    public int getLiveEventBonusExp() {
        return liveEventBonusExp;
    }

    public void setLiveEventBonusExp(int liveEventBonusExp) {
        this.liveEventBonusExp = liveEventBonusExp;
    }

    public long getMask() {
        long mask = 0;
        if(getSelectedMobBonusExp() > 0) {
            mask |= SelectedMobBonusExp.getValue();
        }
        if(getPartyBonusPercentage() > 0) {
            mask |= PartyBonusPercentage.getValue();
        }
        if(getWeddingBonusExp() > 0) {
            mask |= WeddingBonusExp.getValue();
        }
        if(getPartyBonusExp() > 0) {
            mask |= PartyBonusExp.getValue();
        }
        if(getItemBonusExp() > 0) {
            mask |= ItemBonusExp.getValue();
        }
        if(getPremiumIPBonusExp() > 0) {
            mask |= PremiumIPBonusExp.getValue();
        }
        if(getRainbowWeekEventBonusExp() > 0) {
            mask |= RainbowWeekEventBonusExp.getValue();
        }
        if(getBoomupEventBonusExp() > 0) {
            mask |= BoomUpEventBonusExp.getValue();
        }
        if(getPlusExpBuffBonusExp() > 0) {
            mask |= PlusExpBuffBonusExp.getValue();
        }
        if(getPsdBonusExpRate() > 0) {
            mask |= PsdBonusExpRate.getValue();
        }
        if(getIndieBonusExp() > 0) {
            mask |= IndieBonusExp.getValue();
        }
        if(getRelaxBonusExp() > 0) {
            mask |= RelaxBonusExp.getValue();
        }
        if(getInstallItemBonusExp() > 0) {
            mask |= InstallItemBonusExp.getValue();
        }
        if(getAswanWinnerBonusExp() > 0) {
            mask |= AswanWinnerBonusExp.getValue();
        }
        if(getExpByIncExpR() > 0) {
            mask |= ExpByIncExpR.getValue();
        }
        if(getValuePackBonusExp() > 0) {
            mask |= ValuePackBonusExp.getValue();
        }
        if(getExpByIncPQExpR() > 0) {
            mask |= ExpByIncPQExpR.getValue();
        }
        if(getBaseAddExp() > 0) {
            mask |= BaseAddExp.getValue();
        }
        if(getBloodAllianceBonusExp() > 0) {
            mask |= BloodAllianceBonusExp.getValue();
        }
        if(getFreezeHotEventBonusExp() > 0) {
            mask |= FreezeHotEventBonusExp.getValue();
        }
        if(getRestFieldBonusExp() > 0 || getRestFieldExpRate() > 0) {
            mask |= RestField.getValue();
        }
        if(getUserHPRateBonusExp() > 0) {
            mask |= UserHPRateBonusExp.getValue();
        }
        if(getFieldValueBonusExp() > 0) {
            mask |= FieldValueBonusExp.getValue();
        }
        if(getMobKillBonusExp() > 0) {
            mask |= MobKillBonusExp.getValue();
        }
        if(getLiveEventBonusExp() > 0) {
            mask |= LiveEventBonusExp.getValue();
        }

        return mask;
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(isLastHit());
        outPacket.encodeInt(getIncEXP());
        outPacket.encodeByte(isOnQuest());
        outPacket.encodeLong(getMask());
        if(getSelectedMobBonusExp() > 0) {
            outPacket.encodeInt(getSelectedMobBonusExp());
        }
        if(getPartyBonusPercentage() > 0) {
            outPacket.encodeInt(getPartyBonusPercentage());
        }
        if(isOnQuest()) {
            outPacket.encodeByte(getQuestBonusRate());
        }
        if(getQuestBonusRate() > 0) {
            outPacket.encodeByte(getQuestBonusRemainCount());
        }
        if(getWeddingBonusExp() > 0) {
            outPacket.encodeByte(getWeddingBonusExp());
        }
        if(getPartyBonusExp() > 0) {
            outPacket.encodeByte(getPartyBonusExp());
        }
        if(getItemBonusExp() > 0) {
            outPacket.encodeByte(getItemBonusExp());
        }
        if(getPremiumIPBonusExp() > 0) {
            outPacket.encodeByte(getPremiumIPBonusExp());
        }
        if(getRainbowWeekEventBonusExp() > 0) {
            outPacket.encodeByte(getRainbowWeekEventBonusExp());
        }
        if(getBoomupEventBonusExp() > 0) {
            outPacket.encodeByte(getBoomupEventBonusExp());
        }
        if(getPlusExpBuffBonusExp() > 0) {
            outPacket.encodeByte(getPlusExpBuffBonusExp());
        }
        if(getPsdBonusExpRate() > 0) {
            outPacket.encodeByte(getPsdBonusExpRate());
        }
        if(getIndieBonusExp() > 0) {
            outPacket.encodeByte(getIndieBonusExp());
        }
        if(getRelaxBonusExp() > 0) {
            outPacket.encodeByte(getRelaxBonusExp());
        }
        if(getInstallItemBonusExp() > 0) {
            outPacket.encodeByte(getInstallItemBonusExp());
        }
        if(getAswanWinnerBonusExp() > 0) {
            outPacket.encodeByte(getAswanWinnerBonusExp());
        }
        if(getExpByIncExpR() > 0) {
            outPacket.encodeByte(getExpByIncExpR());
        }
        if(getValuePackBonusExp() > 0) {
            outPacket.encodeByte(getValuePackBonusExp());
        }
        if(getExpByIncPQExpR() > 0) {
            outPacket.encodeByte(getExpByIncPQExpR());
        }
        if(getBaseAddExp() > 0) {
            outPacket.encodeByte(getBaseAddExp());
        }
        if(getBloodAllianceBonusExp() > 0) {
            outPacket.encodeByte(getBloodAllianceBonusExp());
        }
        if(getFreezeHotEventBonusExp() > 0) {
            outPacket.encodeByte(getFreezeHotEventBonusExp());
        }
        if(getRestFieldBonusExp() > 0 || getRestFieldExpRate() > 0) {
            outPacket.encodeByte(getRestFieldBonusExp());
            outPacket.encodeByte(getRestFieldExpRate());
        }
        if(getUserHPRateBonusExp() > 0) {
            outPacket.encodeByte(getUserHPRateBonusExp());
        }
        if(getFieldValueBonusExp() > 0) {
            outPacket.encodeByte(getFieldValueBonusExp());
        }
        if(getMobKillBonusExp() > 0) {
            outPacket.encodeByte(getMobKillBonusExp());
        }
        if(getLiveEventBonusExp() > 0) {
            outPacket.encodeByte(getLiveEventBonusExp());
        }

    }
}

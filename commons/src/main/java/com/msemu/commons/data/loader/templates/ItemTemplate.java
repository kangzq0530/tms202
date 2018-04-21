package com.msemu.commons.data.loader.templates;

import com.msemu.commons.enums.InvType;
import com.msemu.commons.enums.ScrollStat;
import com.msemu.commons.enums.SpecStat;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Created by Weber on 2018/4/13.
 */
public class ItemTemplate {
    @Getter
    @Setter
    private int itemId;
    @Getter
    @Setter
    private InvType invType;
    @Getter
    @Setter
    private int time;
    @Getter
    @Setter
    private int stateChangeItem;
    @Getter
    @Setter
    private boolean cash;
    @Getter
    @Setter
    private int price;
    @Getter
    @Setter
    private int slotMax;
    @Getter
    @Setter
    private boolean tradeBlock;
    @Getter
    @Setter
    private boolean notSale;
    @Getter
    @Setter
    private String path = "";
    @Getter
    @Setter
    private int rate;
    @Getter
    @Setter
    private int reqSkillLevel;
    @Getter
    @Setter
    private int masterLevel;
    @Getter
    @Setter
    private int success;
    @Getter
    @Setter
    private boolean noCursed;
    @Getter
    @Setter
    private Map<ScrollStat, Integer> scrollStats = new HashMap<>();
    @Getter
    @Setter
    private Map<SpecStat, Integer> specStats = new HashMap<>();
    @Getter
    @Setter
    private int bagType;
    @Getter
    @Setter
    private int charmEXP;
    @Getter
    @Setter
    private boolean quest;
    @Getter
    @Setter
    private int reqQuestOnProgress;
    @Getter
    @Setter
    private int senseEXP;
    @Getter
    @Setter
    private Set<Integer> questIDs = new HashSet<>();
    @Getter
    @Setter
    private int mobID;
    @Getter
    @Setter
    private int npcID;
    @Getter
    @Setter
    private int linkedID;
    @Getter
    @Setter
    private boolean monsterBook;
    @Getter
    @Setter
    private boolean notConsume;
    @Getter
    @Setter
    private String script = "";
    @Getter
    private List<Integer> skills = new ArrayList<>();
    @Getter
    @Setter
    private String noFlip = "";
    @Getter
    @Setter
    private int reqLevel;
    @Getter
    @Setter
    private int karma;
    @Getter
    @Setter
    private int flatRate, limitMin, limitLess, sharedStatCostGrade, levelVariation, maxDays
            , addTime, maxLevel, pvpChannelLimited, minLevel
            , recoveryRate, recoveryHP, recoveryMP, tamingMob, dama, sitEmotion, meso
            , mesomin, mesomax, mesostdev, floatType, type, direction, npc
            , minusLevel, skillEffectID, dressUpgrade, recover;

    @Setter
    @Getter
    private boolean accountSharable, expireOnLogout , timeLimited, soldInform, purchaseShop
            , removeBody, randstat, blackUpgrade;
    @Getter
    @Setter
    private int map;
    @Getter
    @Setter
    private String path4Top;



    public void putScrollStat(ScrollStat scrollStat, int val) {
        getScrollStats().put(scrollStat, val);
    }

    public void addQuest(int questID) {
        getQuestIDs().add(questID);
    }

    public void putSpecStat(SpecStat ss, int i) {
        getSpecStats().put(ss, i);
    }

}

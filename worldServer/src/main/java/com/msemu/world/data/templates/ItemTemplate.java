package com.msemu.world.data.templates;

import com.msemu.world.enums.InvType;
import com.msemu.world.enums.ScrollStat;
import com.msemu.world.enums.SpecStat;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

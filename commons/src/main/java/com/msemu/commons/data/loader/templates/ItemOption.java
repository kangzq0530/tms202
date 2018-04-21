package com.msemu.commons.data.loader.templates;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/4/13.
 */
public class  ItemOption {

    @Getter
    @Setter
    private int optionType;
    @Getter
    @Setter
    private String string;
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private int reqLevel;
    @Getter
    @Setter
    private String face;

    private Map<ItemOptionType, Integer> otherData = new HashMap<>();


    public void setLevelData(ItemOptionType type, Integer value) {
        otherData.put(type, value);
    }

    @Override
    public String toString() {
        return "id: " + getId() + ", optionType: " + getOptionType() + " : " + getString();
    }

    public static ItemOptionType[] getTypes() {
        return ItemOptionType.values();
    }

    public enum ItemOptionType {
        incSTR,
        incDEX,
        incINT,
        incLUK,
        incACC,
        incEVA,
        incPAD,
        incMAD,
        incPDD,
        incMDD,
        incMHP,
        incMMP,
        incSTRr,
        incDEXr,
        incINTr,
        incLUKr,
        incACCr,
        incEVAr,
        incPADr,
        incMADr,
        incPDDr,
        incMDDr,
        incMHPr,
        incMMPr,
        incSTRlv,
        incDEXlv,
        incINTlv,
        incLUKlv,
        incPADlv,
        incMADlv,
        incSpeed,
        incJump,
        incCr,
        incDAMr,
        incTerR,
        incAsrR,
        incEXPr,
        incMaxDamage,
        HP,
        MP,
        RecoveryHP,
        RecoveryMP,
        level,
        prop,
        time,
        ignoreTargetDEF,
        ignoreDAM,
        incAllskill,
        ignoreDAMr,
        RecoveryUP,
        incCriticaldamageMin,
        incCriticaldamageMax,
        DAMreflect,
        mpconReduce,
        reduceCooltime,
        incMesoProp,
        incRewardProp,
        boss,
        attackType;
    }
}

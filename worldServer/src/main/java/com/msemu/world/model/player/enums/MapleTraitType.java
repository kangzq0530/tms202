package com.msemu.world.model.player.enums;

/**
 * Created by Weber on 2018/3/21.
 */
public enum MapleTraitType { //in order

    // 領導力
    charisma(500, MapleStat.CHARISMA),
    // 洞察力
    insight(500, MapleStat.INSIGHT),
    // 意志
    will(500, MapleStat.WILL),
    // 手藝
    craft(500, MapleStat.CRAFT),
    // 感性
    sense(500, MapleStat.SENSE),
    // 魅力
    charm(5000, MapleStat.CHARM);
    final int limit;
    final MapleStat stat;

    MapleTraitType(int type, MapleStat theStat) {
        this.limit = type;
        this.stat = theStat;
    }

    public int getLimit() {
        return limit;
    }

    public MapleStat getStat() {
        return stat;
    }

    public static MapleTraitType getByQuestName(String q) {
        String qq = q.substring(0, q.length() - 3); //e.g. charmEXP, charmMin
        for (MapleTraitType t : MapleTraitType.values()) {
            if (t.name().equals(qq)) {
                return t;
            }
        }
        return null;
    }
}

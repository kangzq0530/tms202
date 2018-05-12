package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/12.
 */
public enum QuickMoveInfo {

    弓箭手村(100000000, QuickMoveNpcInfo.大陸移動碼頭.getValue() | QuickMoveNpcInfo.計程車.getValue()),
    魔法森林(101000000, QuickMoveNpcInfo.大陸移動碼頭.getValue() | QuickMoveNpcInfo.計程車.getValue()),
    勇士之村(102000000, QuickMoveNpcInfo.大陸移動碼頭.getValue() | QuickMoveNpcInfo.計程車.getValue()),
    墮落城市(103000000, QuickMoveNpcInfo.大陸移動碼頭.getValue() | QuickMoveNpcInfo.計程車.getValue()),
    維多利亞港(104000000, QuickMoveNpcInfo.大陸移動碼頭.getValue() | QuickMoveNpcInfo.計程車.getValue()),
    奇幻村(105000000, QuickMoveNpcInfo.大陸移動碼頭.getValue() | QuickMoveNpcInfo.計程車.getValue()),
    鯨魚號(120000100, QuickMoveNpcInfo.大陸移動碼頭.getValue() | QuickMoveNpcInfo.計程車.getValue()),
    耶雷弗(130000000, QuickMoveNpcInfo.大陸移動碼頭.getValue()),
    瑞恩(140000000, QuickMoveNpcInfo.大陸移動碼頭.getValue()),
    天空之城(200000000, QuickMoveNpcInfo.大陸移動碼頭.getValue()),
    玩具城(220000000, QuickMoveNpcInfo.大陸移動碼頭.getValue()),
    地球防禦總部(221000000, QuickMoveNpcInfo.大陸移動碼頭.getValue()),
    童話村(222000000, QuickMoveNpcInfo.大陸移動碼頭.getValue()),
    水世界(230000000, 0),
    神木村(240000000, QuickMoveNpcInfo.大陸移動碼頭.getValue()),
    桃花仙境(250000000, QuickMoveNpcInfo.大陸移動碼頭.getValue()),
    靈藥幻境(251000000, 0),
    納希沙漠(260000000, QuickMoveNpcInfo.大陸移動碼頭.getValue()),
    瑪迦提亞城(261000000, 0),
    埃德爾斯坦(310000000, QuickMoveNpcInfo.大陸移動碼頭.getValue()),
    萬神殿(400000000, 0);
    @Getter
    private final int map;
    @Getter
    private final long npc;
    @Getter
    private final long generalNpc
            = QuickMoveNpcInfo.怪物公園.getValue()
            | QuickMoveNpcInfo.次元之鏡.getValue()
            | QuickMoveNpcInfo.自由市場.getValue()
            | QuickMoveNpcInfo.梅斯特鎮.getValue()
            | QuickMoveNpcInfo.打工.getValue()
            | QuickMoveNpcInfo.皇家美髮.getValue()
            | QuickMoveNpcInfo.皇家整形.getValue()
            | QuickMoveNpcInfo.琳.getValue()
            //            | QuickMoveNpcInfo.楓之谷拍賣場.getValue()
            | QuickMoveNpcInfo.初音未來.getValue();
    @Getter
    public final static long GLOBAL_NPC
            = QuickMoveNpcInfo.次元傳送門.getValue()
            | QuickMoveNpcInfo.聚合功能.getValue();

    QuickMoveInfo(int map, long npc) {
        this.map = map;
        this.npc = npc | generalNpc;
    }

    public static QuickMoveInfo getByMap(int map) {
        for (QuickMoveInfo qm : QuickMoveInfo.values()) {
            if (qm.getMap() == map) {
                return qm;
            }
        }
        return null;
    }
}

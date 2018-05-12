function start() {
    if( !cm.hasQuestInProgress(21000) ) {
        cm.playerMessage(5, "去找右方的赫麗娜，可在任務進行途中離開到外面。");
    } else{
        cm.teachSkill(20000017, 0, -1);
        cm.teachSkill(20000018, 0, -1);
        cm.teachSkill(20000017, 1, 0);
        cm.teachSkill(20000018, 1, 0);
        cm.playPortalEffect();
        cm.warp(914000200, 1);
    }
}
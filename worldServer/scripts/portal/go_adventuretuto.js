function start() {
    if(cm.getFieldID() == 4000012 && ( true || cm.hasQuestCompleted(32204))){
        cm.warp(4000013, 0);
        return;
    }
    if(cm.getFieldID() == 4000013 && ( true || cm.hasQuestCompleted(32207))) {
        cm.warp(4000014, 0);
        return;
    }
    if(cm.getFieldID() == 4000014 && ( true || cm.hasQuestCompleted(32210))) {
        cm.warp(4000020, 0);
        return;
    }
    cm.openNpc(10301, "ExplorerTut01");
}
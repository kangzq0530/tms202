var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (!cm.hasQuestCompleted(32200)) {
        if(!cm.hasQuestInProgress(32200)) {
            cm.startQuest(32200);
        }
        cm.completeQuest(32200);
    }
    if (!cm.hasQuestCompleted(32201)) {
        if(!cm.hasQuestInProgress(32201)) {
            cm.startQuest(32201);
        }
        cm.completeQuest(32201);
    }
    if (!cm.hasQuestCompleted(32202)) {
        if(!cm.hasQuestInProgress(32202)) {
            cm.startQuest(32202);
        }
        cm.completeQuest(32202);
    }
    cm.onScreenDelayedFieldEffect("maplemap/enter/20000", 0);
    cm.dispose();
}
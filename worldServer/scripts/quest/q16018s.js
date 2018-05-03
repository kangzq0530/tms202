/**
 *  凌晨重置任務
 */
var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode > 0) {
        status++;
    } else {
        status--;
    }
    cm.startQuest();
    cm.dispose();
}
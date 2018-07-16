var status = -1;
var complete = false;
var num = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    if (status === 0) {
        if (!cm.hasQuestCompleted(52922)) {
            cm.dispose();
            return;
        }
        if (num === -1) {
            if (!cm.haveItem(3994895)) {
                num = 1;
            } else {
                num = 2;
            }
        }
        if (num === 1) {
            if (!complete) {
                cm.giveItem(3994895, 1, 7);
                complete = true;
            }
            cm.sayNext("重新給你一個灌輸了我力量的楓方塊");
        } else {
            cm.sayNext("還留下楓方塊的力量啊...");
        }
    } else if (status === 1) {
        cm.sayPrev("不要擔心, 若方塊消失了隨時都可以來找我, 我會為你盡我所能的.\r\n永遠...");
    } else {
        cm.dispose();
    }
}

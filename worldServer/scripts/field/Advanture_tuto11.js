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
    if (status === 0) {
        cm.setInGameCurNodeEventEnd(true);
        cm.setInGameDirectionMode(true);
        cm.onScreenDelayedFieldEffect("maplemap/enter/10000", 0);
        cm.exceTime(2000);
        cm.setInGameCurNodeEventEnd(true);
    } else if (status === 1) {
        cm.spawnNPCRequestController(10300, -240, 220);
        cm.onNpcDirectionEffect(10300, "Effect/Direction12.img/effect/tuto/BalloonMsg1/1", 900, 0, -120);
        cm.exceTime(1800);
    } else if (status === 2) {
        cm.updateNPCSpecialAction(10300, 1, 1000, 100);
        cm.cameraMove([0, 200, 200, 200]);
    } else if (status === 3) {
        cm.exceTime(4542);
    } else if (status === 4) {
        cm.cameraMove([1, 0, 0, 0]);
    } else if (status === 5) {
        cm.exceTime(900);
    } else if (status === 6) {
        cm.selfTalk("剛剛那女生是誰啊?為什麼看到我就逃走?");
    } else if (status === 7) {
        cm.selfTalk("我也先到那邊看看吧.");
    } else if (status === 8) {
        cm.removeNPCRequestController(10300);
        cm.setInGameDirectionMode(false);
        cm.dispose();
    } else {
        cm.dispose();
    }
}
var status = -1;

function start() {
    cm.setInGameDirectionMode(true);
    cm.setDirectionMode(true);
    cm.showReservedEffect("Effect/Direction3.img/archer/Scene" + (cm.hasQuestCompleted(32214) ? "0" : "1"));
    cm.dispose();
}
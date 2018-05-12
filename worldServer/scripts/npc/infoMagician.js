/*
 NPC Name: 		Grendel the Really Old
 Map(s): 		Maple Road : Spilt road of choice
 Description: 		Job tutorial, movie clip
 */

var status = -1;

function start() {
    if (cm.getFieldID() != 1020000 && cm.getFieldID() != 4000026) {
        cm.dispose();
        return
    }
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
        if (status == 0) {
            cm.sayNext("法師擁有華麗效果的屬性魔法和組隊狩獵時非常實用的各種輔助魔法。而且2轉後能夠學習的屬性魔法可以對相反屬性的敵人賦予致命的傷害。");
        } else if (status == 1) {
            cm.askYesNo("怎麼樣，要體驗法師嗎？");
        } else if (status == 2) {
            cm.setDirectionMode(true);
            cm.setInGameDirectionMode(true);
            cm.warp(1020200, 0); // Effect/Direction3.img/magician/Scene00
        }
    } else {
        status--;
        if (status == 0) {
            cm.say("如果想當法師，請再來找我吧。");
            cm.dispose();
        } else {
            dm.dispose();
        }
    }
    
}
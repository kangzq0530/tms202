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
            cm.sayNext("戰士是具有強大攻擊力和體力的職業，在戰場的最前線發揮作用。基本攻擊非常強大的職業，不斷學習高級技術，能夠發揮更強大的力量。");
        } else if (status == 1) {
            cm.askYesNo("怎麼樣，要體驗戰士嗎？");
        } else if (status == 2) {
            cm.setDirectionMode(true);
            cm.setInGameDirectionMode(true);
            cm.warp(1020100, 0); // Effect/Direction3.img/sordman/Scene00
        }
    } else {
        status--;
        if (status == 0) {
            cm.say("如果想當戰士，請再來找我吧。");
        } else {
            dm.dispose();
        }
    }
    
}
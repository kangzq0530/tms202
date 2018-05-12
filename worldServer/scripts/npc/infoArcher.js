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
            cm.sayNext("弓箭手是具有敏捷性和力量的職業，在戰場的後方負責遠距離攻擊，利用地形的狩獵也非常強有力。");
        } else if (status == 1) {
            cm.askYesNo("要想體驗弓箭手嗎？");
        } else if (status == 2) {
            cm.setDirectionMode(true);
            cm.setInGameDirectionMode(true);
            cm.warp(1020300, 0);
        }
    } else {
        status--;
        if (status == 0) {
            cm.say("如果想當弓箭手，請再來找我吧。");
        } else {
            dm.dispose();
        }
    }
    
}
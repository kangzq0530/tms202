var status = -1;

function start() {
    if (cm.getFieldID() !== 1020000 && cm.getFieldID() !== 4000026) {
        cm.dispose();
        return
    }
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
        if (status === 0) {
            cm.sayNext("盜賊是具有運氣和一定敏捷性和力量的職業，在戰場上經常使用突襲對方或藏身的特殊技能。具有相當高的機動性和回避率的盜賊，具有各種技術，操作起來非常有趣。");
        } else if (status === 1) {
            cm.askYesNo("怎麼樣，要想體驗盜賊嗎？");
        } else if (status === 2) {
            cm.setDirectionMode(true);
            cm.setInGameDirectionMode(true);
            cm.warp(1020400, 0);
        }
    } else {
        status--;
        if (status === 0) {
            cm.say("如果想當盜賊，請再來找我吧。");
        } else {
            dm.dispose();
        }
    }
    
}
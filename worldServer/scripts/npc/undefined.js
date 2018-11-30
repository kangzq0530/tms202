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
    if( status == 0 ) {
        cm.say("NPC編號: "+ parentID +" 腳本名稱: " + cm.getScriptName() + " ，這個腳本尚未完成唷");
    } else {
        cm.dispose();
    }
}
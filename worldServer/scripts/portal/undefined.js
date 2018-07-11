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
        cm.say("傳送門ID:"+ parentID +"，這個腳本尚未完成唷");
    } else {
        cm.dispose();
    }
}
var status = -1;
n
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
        cm.sayPrevNext(9330354, 9330352, 4, "煩人的打招呼禮節就先拋開吧!!!勇士阿，你想要更強大的力量嗎?");
    } else  {
        cm.dispose();
    }
}
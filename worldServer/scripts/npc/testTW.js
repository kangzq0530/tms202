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
        cm.askMenu("#L1#XXX#l");
    } else {
        cm.dispose();
    }
}
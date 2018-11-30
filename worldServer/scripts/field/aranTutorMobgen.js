function start() {
    cm.resetField();
    switch(cm.getFieldID()) {
        case 914000200:
            cm.spawnMob(9300379, 2099, 2, false);
            cm.spawnMob(9300379, 1799, 2, false);
            cm.spawnMob(9300379, 1515, 2, false);
            break;
        case 914000210:
            cm.spawnMob(9300380, 667, 2, false);
            cm.spawnMob(9300380, 382, 2, false);
            cm.spawnMob(9300380, 97, 2, false);
            break;
        case 914000220:
            cm.spawnMob(9300381, -716, 2, false);
            cm.spawnMob(9300381, -839, 2, false);
            cm.spawnMob(9300381, -1046, 2, false);
            cm.spawnMob(9300381, -1186, 2, false);
            cm.spawnMob(9300381, -1332, 2, false);
            break;
    }
    cm.dispose();
}
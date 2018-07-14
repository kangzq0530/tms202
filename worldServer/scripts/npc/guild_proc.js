/**
 * 公會負責人 海拉格 (2010007)
 */
var status = -1;
var hasGuild = false;
var select1 = -1;

function start() {
    hasGuild = cm.getGuild() != null;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }
    if (hasGuild) {
        procGuildAction(mode, type, selection);
    } else {
        createGuildAction(mode, type, selection);
    }
}

function procGuildAction(mode, type, selection) {
    if (status < 0) {
        cm.say("如果有其他事情，可以再來找我。");
    } else {
        cm.sayPrev("QQ");
    }
}

function createGuildAction(mode, type, selection) {
    if (status < 0) {
        cm.say("如果有其他事情，可以再來找我。");
    } else if (status === 0) {
        cm.sayNext("你……是因為對公會感興趣，才會來找我的嗎？");
    } else if (status === 1) {
        select1 = -1;
        cm.askMenu("你想要幹什麼呢？快告訴我吧。\r\n\r\n#b#L0#請告訴我公會是什麼#l\r\n#L1#怎麼才能創建公會呢？#l\r\n#L2#我想創建公會#l"/*\r\n#L3#我想了解有关公會系统的详细说明#l"*/);
    } else if (status === 2) {
        if (select1 === -1)
            select1 = selection;
        if (select1 === 0) {
            cm.sayPrevNext("公會……你可以把它理解成一個小的組織。是擁有相同理想的人為了同一個目的而聚集在一起成立的組織。 但是公會是經過公會總部的正式登記，是經過認可的組織。");
        } else if (select1 === 1) {
            cm.sayPrevNext("要想創建公會，至少必須達100級，身上必須有500萬楓幣。");
        } else if (select1 === 2) {
            cm.askYesNo("哦！你是來創建公會的嗎……要想創建公會，需要500萬楓幣。我相信你一定已經準備好了。好的～你想創建公會嗎？");
        } else if (select1 === 3) {
            cm.say("你想了解更多有關公會的內容？如果是那樣的話，公會負責人蕾雅會為你介紹的。");
            cm.dispose()
        }
    } else if (status === 3) {
        if (select1 === 0) {
            cm.sayPrev("通過公會活動，可以獲得很多優惠。比如，可以獲得公會技能，以及公會專用道具。");
        } else if (select1 === 1) {
            cm.sayPrev("此外還需要500萬楓幣。這是註冊公會所需的手續費。");
        } else if (select1 === 2) {
            if (cm.getLevel() < 100) {
                cm.sayPrev("喂，你的等級還太低，好像還沒辦法成為公會管理員。要想創建公會，至少必須達到100級。等你升到100級之後，再重新嘗試吧。");
            } else if (cm.getMoney() < 5000000) {
                cm.sayPrev("哎呀，手續費好像不夠。你帶够錢了嗎？請再確認一下。要想創建公會，手續費是必須的。");
            } else {
                cm.sayNext("請輸入公會名稱。");
            }
        } else if (select1 === 3) {
            cm.askYesNo("怎麼樣？你想抽點時間，接受公會集中培訓嗎？\r\n#r(點擊接受時，移動到聽取說明的場所。)#k");
        }
    } else if (status === 4) {

        cm.showInputGuildName();
        cm.dispose()

    } else {
        cm.dispose();
    }
}
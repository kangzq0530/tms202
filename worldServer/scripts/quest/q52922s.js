var status = -1;
var complete = false;

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
        cm.sayNext("為了對抗逐漸變強的黑暗力量,楓之谷的所有勇士必須一起變強.");
    } else if (status === 1) {
        cm.sayPrevNext("勇士阿...給你注入了我的力量的楓方塊");
    } else if (status === 2) {
        cm.sayPrevNext("這個方塊是古代煉金術師製作的,雖然根據使用次數需要的楓幣會逐漸增加,但這個方塊是不會遜於以前支配楓之谷的各種方塊的.");
    } else if (status === 3) {
        cm.sayPrevNext("現在要重新注入我的力量,請收下..");
    } else if (status === 4) {
        if (!complete) {
            cm.startQuest();
            cm.giveItem(3994895, 1, 12);
            cm.complteQuest();
            complete = true;
        }
        cm.sayPrevNext(1, "因為在瞬間移動中因此無法注入太多力量,但請不要擔心.");
    } else if (status === 5) {
        cm.sayPrevNext(1, "若親自來村莊找我,我會製作擁有更多力量的方塊給你.");
    } else if (status === 6) {
        cm.dispose();
    }
}
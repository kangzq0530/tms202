/**
 * 狂狼勇士任務 : 請救出小孩
 * NPC : 1209000 (赫麗娜)
 */


var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.say("不行啊！狂狼勇士…拋棄那些孩子，只剩下我們苟且偷生…那人生還有什麼意義！\r\n我知道對您來說是很大的負擔…可是請您再考慮看看吧 ！");
            cm.dispose();
            return;
        }
        status--
    }
    if (status == 0) {
        cm.askYesNo("真是的！好像還有幾個孩子留在森林內！我們不可能丟下孩子們逃走。\r\n狂狼勇士…真的很抱歉，請你去救救那些孩子吧！我知道對受傷的您說這些話真的很厚顏無恥，可是…只能拜託您了！");
    } else if (status == 1) {
        cm.startQuest();
        cm.sayNext("#b孩子們應該在森林深處#k。在黑魔法師追來這裡之前，方舟要趕緊出發，請盡快救回那些孩子吧！");
    } else if (status == 2) {
        cm.sayPrevNext("最重要的是不要驚慌。 狂狼勇士。若您想確認任務進行情況，請按下 #bQ按鍵#k確認任務欄位。");
    } else if (status == 3) {
        cm.sayPrevNext("拜託您了！狂狼勇士！請救救那些孩子！我不希望有人再犧牲於黑魔法師的魔掌之下。");
    } else if (status == 4) {
        cm.showAvatarOrientedEffect("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow1");
        cm.dispose();
    }
}
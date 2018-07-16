var status = -1;

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
        cm.startQuest();
        cm.sayNext("你好，我們是很久以前曾經支配楓之谷的雙胞胎艾尼瑪和艾尼穆斯");
    } else if (status === 1) {
        cm.sayPrevNext(9330355, 9330355, 4, "煩人的打招呼禮節就先拋開吧!!!勇士阿，你想要更強大的力量嗎?");
    } else if (status === 2) {
        cm.sayPrevNext("放棄吧,艾尼穆斯.光是一兩個勇士是無法抵抗黑暗惡勢力的...");
    } else if (status === 3) {
        cm.sayPrevNext("#i3800647#\r\n艾尼穆斯想要透過自己的方塊創造少數強大的菁英勇士...");
    } else if (status === 4) {
        cm.sayPrevNext(9330355, 9330355, 4, "#i3800646#\r\n但相反的艾尼瑪想犧牲自己的力量，提供方塊給楓之谷的所有勇士... ");
    } else if (status === 5) {
        cm.sayPrevNext(9330355, 9330355, 4, "#i3800648#艾尼瑪...你這樣犧牲自己的力量,千萬不要哪天倒下了...");
    } else if (status === 6) {
        cm.sayPrevNext("雖然我們想要對抗黑暗惡勢力的心都是一樣的,但方法有一點不同...\r\n勇士啊...請你等著...我會再來找你的.");
    } else if (status === 7) {
        cm.completeQuest();
        cm.dispose();
    }
}
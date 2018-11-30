/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


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
        cm.sayNext(8, "現在筋骨應該都鬆開了吧！此時更嚴格的鍛鍊，才能擁有完美的基礎體力。好！那麼我們繼續進行基礎體力鍛煉！");
    } else if (status === 1) {
        cm.sayNext(8, "現在前往 #b#m140020200##k 擊退 #r#o0100133#s#k 只要擊退....#r20隻#k 左右，就會鍛煉體力有很大的幫助。好，快去吧...咦？你有話想說嗎？");
    } else if (status === 2) {
        cm.sayNext(2, ".....為什麼數字愈來愈多.....");
    } else if (status === 3) {
        cm.sayNext(8, "當然會增加。哎呀，您覺得 20隻太少嗎？ 那麼去擊退100隻怎麼樣啊？不，不。既然要修煉，那麼就效法奇幻村的某人要求去抓 999隻怪物...");
    } else if (status === 4) {
        if (mode === 1)
            cm.sayNext(2, "不，不用啦！這樣就夠了。");
        else
            cm.sayNext(2, "#b(用害怕的心情按下拒絕。可是又不能這樣逃走..鎮定心情後再談看看。)#k");
    } else if (status === 5) {
        cm.askAccept("哎呀哎呀，沒有必要推辭。我完！全！的！了解英雄想快點變強的心情。英雄真的太偉大了...");
    } else if (status === 6) {
        cm.startQuest();
        cm.sayNext("#b(再聽一次好像真的要我去擊退 999隻，乾脆答應算了。)#k");
    } else if (status === 7) {
        cm.sayNext(8, "那麼請擊退 #o0100133# 20隻吧！");
    } else {
        cm.showAvatarOrientedEffect("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow3");
        cm.dispose();
    }


}
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
        cm.sayNext("好了，我覺得我解釋夠多了～該進入下一個階段了！ 什麼？你說什麼下一個階段嗎，岡不是跟你說了，要把你訓練到有足夠的能力去獨自對抗黑魔法師啊～");
    } else if (status === 1) {
        if (mode === 1) {
            cm.sayPrevNext("或許你以前是個英雄沒錯，但那也是幾百年前之前的事了。就算沒有黑魔法師的詛咒，你待在冰雪封印中這麼久的時間，身體一定變得很僵硬吧! 你至少也得先暖暖身手，你說是不是？");
        } else {
            cm.sayNext("還說您是英雄，怎麼會這麼猶豫不決？您沒聽過打鐵要趁熱嗎？想變強的話，那就快點開始吧！");
        }
    } else if (status === 2) {
        cm.askAccept("體力就是戰力！英雄的基礎就是體力！ ... 您沒聽過這些話嗎？當然要先做#b基礎體力鍛鍊#k ... 啊！ 您喪失記憶所以什麼都忘了。不知道也沒關係。那麼現在就進入基礎體力鍛鍊吧！");
    } else if (status === 3) {
        cm.startQuest();
        cm.showAvatarOrientedEffect("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow3");
        cm.dispose();
    }


}
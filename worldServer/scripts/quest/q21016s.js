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

    if (status === -1) {
        cm.sayNext("您還沒準備好獵殺 #o0100132#嗎？ 最好把該準備的都準備好再去狩獵比較好。如果不好好準備，在途中一命嗚呼了，那只會讓人遺淺罷了！");
    } else if (status === 0) {
        cm.askAccept("那麼要繼續基礎體力鍛鍊嗎？準備好了嗎？請您在確認劍是否裝備好了，技能和藥是否已經放入快捷欄內，然後就開始吧！");
    } else if (status === 1) {
        cm.startQuest();
        cm.showAvatarOrientedEffect("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow3");
        cm.dispose();
    }


}
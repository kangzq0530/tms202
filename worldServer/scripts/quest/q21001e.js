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
        cm.say("孩子呢？倘若您救了那些孩子，就快點讓他們上來吧！")
        cm.dispose();
    } else if (status === 0) {
        cm.askYesNo("啊啊，您平安無事歸來了！孩子呢？您把那些孩子帶回來了嗎？");
    } else if (status === 1) {
        var completed = cm.hasQuestCompleted();
        if (!completed)
            cm.completeQuest();
        cm.sayNext(9, "真是太好了… 真是太好了……");
    } else if (status === 2) {
        cm.sayPrevNext(3, "快點坐上飛行船吧！沒時間了。");
    } else if (status === 3) {
        cm.sayPrevNext(9, "對，對了！現在不是談這些事情的時機。 黑魔法師的氣息已經慢慢的靠近了！好像已經察覺方舟的位置了！不趕快出發的話，就會被逮個正著。");
    } else if (status === 4) {
        cm.sayPrevNext(3, "立刻出發！");
    } else if (status === 5) {
        cm.sayPrevNext(9, "狂狼勇士！你也坐上方舟吧！我雖然了解您想火拼到最後一刻的心情…可是已經太遲了！打仗這個任務就交給您的同伴，跟我們一起前往維多利亞島吧！");
    } else if (status === 6) {
        cm.sayPrevNext(3, "絕對不行！");
    } else if (status === 7) {
        cm.sayPrevNext(3, "赫麗娜，您先去維多利亞島吧！我絕對不會死的，我們後會有期。我要和同伴們一起對付黑魔法師！");
    } else {
        cm.warp(914090010, 0);
        cm.dispose();
    }
}
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
    action(1, 0, 0)
}

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }
    if (status === 0) {
        cm.sayNext("和 #p1201000#大人在一起的您，該不會…該不會…也是英雄吧？ #p1201000#大人！不要不耐煩的點頭，請確實的告訴我！這位正是英雄吧！");
    } else if (status === 1) {
        cm.sayPrevNext("   #i4001171#");
    } else if (status === 2) {
        cm.sayPrevNext("…很抱歉。我太感動了，不會再大喊大叫了。可是真的太感動了…啊啊，眼淚都快掉下來了… #p1201000#大人應該也很開心。");
    } else if (status === 3) {
        if (mode === 1) {
            cm.sayPrevNext("可是…英雄怎麼沒有帶武器呢。據我所知英雄有自己的武器…啊！應該是和黑魔法師決鬥時弄掉了。");
        } else {
            cm.sayNext("呃。看不上這把廉價的劍嗎？");
            cm.dispose();
        }
    } else if (status === 4) {
        cm.askYesNo("湊合著用可能會太寒酸，不過 #b請你先收下這把劍吧#k！這是我送給英雄的禮物。英雄空著手總是有點奇怪…\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0# \r\n#i1302000# #t1302000# 1個 \r\n\r\n#fUI/UIWindow2.img/QuestIcon/8/0# 35 exp");
    } else if (status === 5) {
        if (cm.hasQuestInProgress(21011)) {
            cm.give(1302000, 1);
            cm.give(35);
        }
        cm.completeQuest();
        cm.sayNext(3, "#b(連技能一點都不像英雄…連劍都好陌生。我之前真的有用過劍嗎？劍該怎麼配戴呢？)#k");
    } else if (status === 6) {
        cm.showTutorMsg(16, 7000);
        cm.dispose();
    }
}
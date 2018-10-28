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
        cm.sayNext("咦？這個島上的什麼人…？喔 ，您認識 #p1201000#嗎？ #p1201000#到這裡有什麼事…啊，這位是不是 #p1201000#大人認識的人呢？什麼？你說這位是英雄嗎？");
    } else if (status === 1) {
        cm.sayPrevNext("     #i4001170#");
    } else if (status === 2) {
        if (mode === 1) {
            cm.sayPrevNext("這位正是 #p1201000#家族數百年等待的英雄！喔喔！難怪看起來不是什麼平凡的人物…");
        } else {
            cm.sayNext("哎呀，不用客氣啦！送英雄一瓶藥水也不是什麼了不起的事。倘若您改變心意再告訴我吧 ！");
            cm.dispose();
        }
    } else if (status === 3) {
        cm.askYesNo("但是，因為黑魔法師的詛咒而在巨冰裡沉睡著，所以，好像英雄的體力都消耗掉了的樣子。 #b我給你一個恢復體力用的藥水，趕緊喝喝看。#k.");
    } else if (status === 4) {
        cm.giveItem(2000022, 1);
        cm.startQuest();
        cm.sayNext("您先大口喝下，我再繼續說下去~");
    } else if (status === 5) {
        cm.sayPrevNext(3, "#b(藥水該怎麼喝呢？…想不起來了…)#k");
    } else if (status === 6) {
        qm.showTutorMsg(14, 7000);
        qm.dispose();
    }
}
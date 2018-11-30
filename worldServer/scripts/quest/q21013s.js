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
        if (mode === 1) {
            cm.say("啊， 英雄…我好想你喔！  \r\n#b#L0#(害羞的樣子)#l");
        } else {
            cm.say("對英雄很有幫助的禮物。請不要拒絕。");
            cm.dispose();
        }
    } else if (status === 1) {
        cm.askYesNo("我從以前就決定遇見英雄要送您一個禮物…我知道您忙著回村莊，可是…可以收下我誠心的禮物嗎？");
    } else if (status === 2) {
        cm.startQuest();
        cm.sayNext(1, "禮物的材料就放在這附近的箱子裡面。雖然有點麻煩，可是請您將箱子打破後，將裡面的材料 #b#t4032309##k和 #b#t4032310##k帶回來。我就會立刻幫您組裝。");
    } else if (status === 3) {
        cm.showTutorMsg(18, 7000);
        cm.dispose();
    } else {
        cm.dispose();
    }
}

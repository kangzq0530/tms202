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
    if (status === -1) {
        cm.say("什麼…？不喜歡藥水嗎？");
        cm.dispose();
    } else if (status === 0) {
        cm.askYesNo("嗯…看您的表情，似乎什麼都沒想起來…可是請不要擔心。總有一天會好起來的。來，請您喝下這些藥水打起精神來： \r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0# \r\n#i2000022# #t2000022# 10個 \r\n#i2000023# #t2000023# 10個 \r\n\r\n#fUI/UIWindow2.img/QuestIcon/8/0# 57 exp");
    } else if (status === 1) {
        cm.giveItem(2000022, 10);
        cm.giveItem(2000023, 10);
        cm.giveExp(57);
        cm.completeQuest();
        cm.say(2, "#b(就算我是真正的英雄…可是什麼能力都沒有的英雄還有用處嗎？)#k");
        cm.dispose();
    }
}

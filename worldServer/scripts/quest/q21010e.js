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
        cm.sayNext("長久以來尋找英雄的痕跡，在冰雪中挖掘，果然真正的英雄出現了！預言果然是真的！ #p1201000#大人做了正確的選擇。英雄回來了，現在沒有必要再畏懼黑魔法師了！");
    } else if (status === 1) {
        cm.sayPrevNext("啊！真是的，我耽擱英雄太久了。先收拾起快樂的心情…其他企鵝應該也有同樣的想法。我知道您很忙，不過在前往村莊的路上 #b也請您和其他企鵝們談一談#k。可以和英雄談話，大家應該會很興奮！ \r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0# \r\n#i2000022# #t2000022# 5個 \r\n#i2000023# #t2000023# 5個 \r\n\r\n#fUI/UIWindow2.img/QuestIcon/8/0# 16 exp");
    } else if (status === 2) {
        if(!cm.hasQuestCompleted())
            cm.completeQuest();
        cm.sayNext("想要升級嗎？不曉得您有沒有獲得技能點數。在楓之谷內透過轉職之後每上升1級就會獲得3點的技能點數。按下 #bK鍵#k開啟技能欄位就能確認。");
    } else if (status === 3) {
        cm.sayPrevNext("#b(這麼親切的說明，可是我什麼都想不起來。我真的是英雄嗎？那麼先確認技能看看…可是該怎麼確認呢？)#k");
    } else if (status === 4) {
        cm.showTutorMsg(15, 7000);

        cm.dispose();
    }
}
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
        cm.sayNext("大英雄！您好！您說怎麼知道您是英雄嗎？前面有三個人大聲的叫喊，當然知道啊。這個島上已經流傳著英雄回來的消息。");
    } else if (status === 1) {
        if (mode === 1) {
            cm.sayPrevNext("總之，英雄的臉色怎麼會這麼難看呢？您有什麼困難嗎？您說您不曉得自己是不是真正的英雄嗎？ 英雄喪失記憶了嗎？怎麼會這樣…應該是數百年來被困在冰雪之中的副作用。");
        } else {
            cm.sayNext("嗯…用這個方法也不能恢復記憶嗎？可是沒試過也不曉得，您再考慮看看吧。");
            cm.dispose();
        }
    } else if (status === 2) {
        cm.askYesNo("啊！既然您是英雄，只要揮揮劍應該會想起些什麼吧！您想不想去 #b獵捕怪物#k呢？");
    } else if (status === 3) {
        cm.startQuest();
        cm.sayNext("正好這附近有很多 #r#o9300383##k，請您去擊退  #r3隻#k。搞不好會想起些什麼。");
    } else if (status === 4) {
        cm.sayPrevNext(1, "啊，該不會連技能使用法都忘光了吧？ #b將技能放入快捷欄就可以輕鬆使用#k。不只是技能，連消耗道具也可以放進去，請多加利用 。");
    } else if (status === 5) {
        cm.showTutorMsg(17, 7000);
        cm.dispose();
    }
}

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

    if (cm.getFieldID() !== 140090000) {
        actionNormal(mode, type, selection);
    } else {
        var qrExVal = cm.getQuestRecordEx(21002);
        if(qrExVal.equals("")) {
            actionInCaveHelp(mode, type, selection);
        } else {
            actionInCave(mode, type, selection);
        }

    }
}
function actionNormal(mode, type, selection) {
    if (status === 0) {
        cm.say("您有什麼想知道的呢？有的話我會再度說明。\r\n#b#L1#我是誰呢？#l \r\n#b#L2#這裡是哪裡呢？#l \r\n#b#L3#你是誰呀？#l\r\n#b#L4#告訴我現在要做的事。#l\r\n#b#L14#該如何使用道具？#l\r\n#b#L15#該如何提升技能？#l\r\n#b#L16#想知道如何穿上裝備。#l\r\n#b#L17#如何使用快捷欄？#l \r\n#b#L18#如何打碎箱子？#l");
    } else {
        switch (selection) {
            case 1:
                cm.showTutorMsg("您是數百年前，從黑魔法師的魔掌中拯救楓之谷的英雄之一。被黑魔法師詛咒後失去了全部的記憶。", 200, 10000);
                break;
            case 2:
                cm.showTutorMsg("這裡是您被詛咒後沉睡的瑞恩島。由冰雪形成的小島。居民大部分是企鵝。", 200, 10000);
                break;
            case 3:
                cm.showTutorMsg("我是相信預言等待您到來的瑞恩族成員莉琳。未來會繼續引導您 。", 200, 10000);
                break;
            case 4:
                cm.showTutorMsg("詳細的情況我到村莊再向您說明。我們不能在這裡停留，要快點回去村莊。跟著指示繼續走就可以了。", 200, 10000);
                break;
            default:
                cm.showTutorMsg(selection, 7000);
        }
        cm.dispose();
    }
}

function actionInCaveHelp(mode, type, selection) {
    if (status === 0) {
        cm.sayNext("您終於醒了…！");
    } else if (status === 1) {
        cm.sayPrevNext(2, "…你？");
    } else if (status === 2) {
        cm.sayPrevNext("我早就在等你了。您…和黑魔法師戰鬥的英雄甦醒了…！");
    } else if (status === 3) {
        cm.sayPrevNext(2, "…到底在說什麼？你是誰…？");
    } else if (status === 4) {
        cm.sayPrevNext(2, "不…我到底是誰…？什麼都想不起來了… 呃……！我的頭痛到快裂開了！");
    } else if (status === 5) {
        cm.setQuestRecordEx(21019, "helper=clear");
        cm.showReservedEffect("Effect/Direction1.img/aranTutorial/face");
        cm.showReservedEffect("Effect/Direction1.img/aranTutorial/ClickLirin");
        cm.dispose();
    }
}

function actionInCave(mode, type, selection) {
    if (status === 0) {
        cm.sayNext("您還好嗎？");
    } else if (status === 1) {
        cm.sayPrevNext(2, "我…什麼都不記得了…這裡是哪裡？還有你是誰？");
    } else if (status === 2) {
        cm.sayPrevNext("請您冷靜。 黑魔法師的詛咒刪除了您的記憶…沒有必要擔心什麼都想不起來。所有您想知道的事，我會慢慢的向您說明 。");
    } else if (status === 3) {
        cm.sayPrevNext("您是英雄。數百年前和黑魔法師戰鬥拯救了楓之谷。可是在最後一刻被黑魔法師詛咒，長時間封鎖在冰雪裡面。同時也失去了記憶。");
    } else if (status === 4) {
        cm.sayPrevNext("這裡是瑞恩島。黑魔法師將您囚禁在此地。詛咒後氣候混亂，經年覆蓋冰霜和雪。您是在冰之窟的深處被發現的。");
    } else if (status === 5) {
        cm.sayPrevNext("我的名字叫莉琳。是瑞恩族的成員。瑞恩族根據古老的預言從很久以前就在等待英雄回來。還有…終於找到您了。現在。就是這裡…");
    } else if (status === 6) {
        cm.sayPrevNext("好像一下說太多了。就算您不能馬上了解也沒關係。您會慢慢知道所有事… #b我們先去村莊吧#k。在抵達村莊之前，如果還有什麼想知道的，我會逐一向您說明。");
    } else if (status === 7) {
        cm.showHireTutor(true);
        cm.warp(140090100, 1);
        cm.dispose();
    }
}
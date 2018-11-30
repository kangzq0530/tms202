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
        cm.sayNext(8, "There isn't much record left of the heroes that fought against the Black Mage. Even in the Book of Prophecy, the only information available is that there were five of them. There is nothing about who they were or what they looked like. Is there anything you remember? Anything at all?");
    } else if (status === 1) {
        cm.sayPrevNext(2, "I don't remember a thing...");
    } else if (status === 2) {
        cm.sayPrevNext(8, "As I expected. Of course, the curse of the Black Mage was strong enough to wipe out all of your memory. But even if that''s the case, there has got to be a point where the past will uncover, especially now that we are certain you are one of the heroes. I know you've lost your armor and weapon during the battle but... Oh, yes, yes. I almost forgot! Your #bweapon#k!");
    } else if (status === 3) {
        cm.sayPrevNext(2, "My weapon?");
    } else if (status === 4) {
        cm.sayPrevNext(8, "I found an incredible weapon while digging through blocks of ice a while back. I figured the weapon belonged to a hero, so I brought it to town and placed it somewhere in the center of the town. Haven't you seen it? #bThe #p1201001##k... \r\r#i4032372#\r\rIt looks like this...");
    } else if (status === 5) {
        cm.sayPrevNext(2, "Come to think of it, I did see a #p1201001# in town.");
    } else if (status === 6) {
        cm.askAccept("Yes, that's it. According to what's been recorded, the weapon of a hero will recognize its rightful owner, and if you''re the hero that used the #p1201001#, the #p1201001# will react when you grab the #p1201001#. Please go find the #b#p1201001# and click on it.#k");
    } else if (status === 7) {
        cm.startQuest();
        cm.showAvatarOrientedEffect("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow1");
        cm.dispose();
    }
}
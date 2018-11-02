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

        if (mode === 1)
            cm.sayNext("Now, you will undergo a test that will determine whether you''re fit or not. All you have to do is take on the most powerful monster on this island, #o0100134#s. About #r50#k of them would suffice, but...");
        else
            cm.sayNext("Oh, is 5 not enough? If you feel the need to train further, please feel free to slay more than that. If you slay all of them, I''ll just have to look the other way even if it breaks my heart, since they will have been sacrificed for a good cause...");

    } else if (status === 1) {
        cm.askAccept("We can't have you wipe out the entire population of #o0100134#s, since they aren't many of them out there. How about 5 of them? You're here to train, not to destroy the ecosystem.");
    } else if (status === 2) {
        cm.startQuest();
        cm.showAvatarOrientedEffect("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow1");
        cm.dispose();
    }


}
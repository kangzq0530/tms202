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

function start() {
    cm.setInGameDirectionMode(true);
    cm.setStandAloneMode(true);
    var gender = cm.getGender();
    switch(cm.getFieldID()) {
        case 914090010:
            cm.showReservedEffect("Effect/Direction1.img/aranTutorial/Scene0");
            break;
        case 914090011:
            cm.showReservedEffect("Effect/Direction1.img/aranTutorial/Scene1" + (gender === 0 ? "0" : "1"));
            break;
        case 914090012:
            cm.showReservedEffect("Effect/Direction1.img/aranTutorial/Scene2" + (gender === 0 ? "0" : "1"));
            break;
        case 914090013:
            cm.showReservedEffect("Effect/Direction1.img/aranTutorial/Scene3");
            break;
        case 914090100:
            cm.showReservedEffect("Effect/Direction1.img/aranTutorial/HandedPoleArm" + (gender === 0 ? "0" : "1"));
            break;
        case 914090200:
            ms.showReservedEffect("Effect/Direction1.img/aranTutorial/Maha");
            break;
    }
    cm.dispose();
}
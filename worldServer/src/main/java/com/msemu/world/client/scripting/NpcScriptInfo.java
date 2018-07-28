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

package com.msemu.world.client.scripting;

import com.msemu.world.enums.NpcMessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/1.
 */
@Getter
@Setter
public class NpcScriptInfo {
    private int speakerType = 4;
    private int speakerTemplateID;
    private int anotherSpeakerTemplateID;
    private int overrideSpeakerTemplateID;
    private int param;
    private byte color;
    private String text;
    private NpcMessageType messageType;
    private NpcMessageType lastMessageType;
    private String[] images;
    private int min;
    private int max;
    private boolean prev;
    private boolean next;
    private int delay;
    private String defaultText;
    private int defaultNumber;
    private byte type;
    private int time;
    private String title;
    private String problemText;
    private String hintText;
    private int quizType;
    private int answer;
    private int correctAnswers;
    private int remaining;
    private int result;
    private boolean angelicBuster;
    private boolean zeroBeta;
    private short col, line, fontSize, fontTopMargin, backgroundID;
}

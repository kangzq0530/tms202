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

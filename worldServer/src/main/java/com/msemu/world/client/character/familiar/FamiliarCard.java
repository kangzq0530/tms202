package com.msemu.world.client.character.familiar;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/7.
 */
@Getter
@Setter
public class FamiliarCard {

    private byte grade;
    private int skill;
    private int options[];

    private byte level;

    public FamiliarCard(byte grade) {
        this.grade = grade;
        this.level = 1;
        this.options = new int[3];
    }

    public FamiliarCard() {
        this.options = new int[3];
    }

    public FamiliarCard(short skill, byte level, byte grade, int option1, int option2, int option3) {
        this.options = new int[3];
        this.skill = skill;
        this.level = (byte) Math.max(1, (int) level);
        this.grade = grade;
        this.options[0] = option1;
        this.options[1] = option2;
        this.options[2] = option3;
    }

    public void copy(FamiliarCard card) {
        this.skill = card.getSkill();
        this.level = card.getLevel();
        this.grade = card.getGrade();
        this.options[0] = card.getOption(0);
        this.options[1] = card.getOption(1);
        this.options[2] = card.getOption(2);
    }

    public int getOption(int index) {
        return index >= 0 && index < 3 ? options[index] : 0;
    }

}


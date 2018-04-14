package com.msemu.world.client.character.items;

import com.msemu.world.enums.ItemGrade;

/**
 * Created by Weber on 2018/4/13.
 */
public class ItemOption {
    private int optionType;
    private int weight;
    private int id;
    private int reqLevel;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getOptionType() {
        return optionType;
    }

    public void setOptionType(int optionType) {
        this.optionType = optionType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id: " + getId() + ", optionType: " + getOptionType() + ", weight: " + getWeight();
    }

    public boolean hasMatchingGrade(short itemState) {
        return ItemGrade.isMatching(itemState, ItemGrade.getGradeByOption(getId()).getValue());
    }

    public boolean isBonus() {
        return getId() / 1000 % 10 == 2;
    }

    public void setReqLevel(int reqLevel) {
        this.reqLevel = reqLevel;
    }

    public int getReqLevel() {
        return reqLevel;
    }
}

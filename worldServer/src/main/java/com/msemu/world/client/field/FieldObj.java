package com.msemu.world.client.field;

import com.msemu.commons.data.templates.field.FieldObjectInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Weber on 2018/5/12.
 */
public class FieldObj {
    @Getter
    public FieldObjectInfo data;

    @Getter
    @Setter
    private int currentX, currentY;

    public FieldObj(FieldObjectInfo data) {
        this.data = data;
        this.currentX = data.getX1();
        this.currentY = data.getY1();
    }

    public boolean isMove() {
        return getData().isMove();
    }

    public int getX1() {
        return getData().getX1();
    }

    public int getX2() {
        return getData().getX2();
    }

    public int getY1() {
        return getData().getY1();
    }

    public int getY2() {
        return getData().getY2();
    }

    public int getStart() {
        return getData().getStart();
    }

    public int getSpeed() {
        return getData().getSpeed();
    }

    public int getR() {
        return getData().getR();
    }

    public String getName() {
        return getData().getName();
    }

    public List<Integer> getSns() {
        return getData().getSns();
    }

}

package com.msemu.commons.data.templates;

import com.msemu.commons.utils.types.Position;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/11.
 */
@Getter
@Setter
public class Foothold {
    protected int id;
    protected int layerId;
    protected int groupId;
    protected int x1;
    protected int y1;
    protected int x2;
    protected int y2;
    protected int next;
    protected int prev;
    protected int force;
    protected boolean forbidFallDown;

    public Foothold(int id, int layerId, int groupId, int x1, int y1, int x2, int y2, int next, int prev, int force) {
        this.id = id;
        this.layerId = layerId;
        this.groupId = groupId;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.next = next;
        this.prev = prev;
        this.force = force;
    }

    public Foothold(int id, int layerId, int groupId) {
        this.id = id;
        this.layerId = layerId;
        this.groupId = groupId;
    }
    public Foothold() {
    }

    public Foothold deepCopy() {
        Foothold copy = new Foothold(getId(), getLayerId(), getGroupId(), getX1(), getY1(), getX2(), getY2(), getNext(), getPrev(), getForce());
        copy.setForbidFallDown(isForbidFallDown());
        return copy;
    }

    @Override
    public String toString() {
        return "Id: " + getId() + ", Start = " + new Position(getX1(), getY1()) + ", End = " + new Position(getX2(), getY2());
    }

    public int getYFromX(int x) {
        // interpolate between the two foothold ends for the y value below pos.x
        int x1 = getX1();
        int x2 = getX2() - x1;
        x = x - x1;
        double perc = (double) x / (double) x2;
        return (int) (getY1() + (perc * (getY2() - getY1())));
    }
}
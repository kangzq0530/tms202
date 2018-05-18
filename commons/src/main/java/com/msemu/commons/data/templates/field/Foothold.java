package com.msemu.commons.data.templates.field;

import com.msemu.commons.data.loader.dat.DatSerializable;
import com.msemu.commons.utils.types.Position;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/11.
 */
@Getter
@Setter
public class Foothold implements Comparable<Foothold>, DatSerializable {
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

    public boolean isWall() {
        return x1 == x2;
    }

    public boolean isPlateform() {
        return y1 == y2;
    }

    public boolean isSlope() {
        return y1 != y2;
    }

    public int getYFromX(int x) {
        // interpolate between the two foothold ends for the y value below pos.x
        int x1 = getX1();
        int x2 = getX2() - x1;
        double perc = (double) (x - x1) / (double) x2;
        return (int) (getY1() + (perc * (getY2() - getY1())));
    }

    @Override
    public int compareTo(Foothold o) {
        Foothold other = o;
        if (y2 < other.getY1()) {
            return -1;
        } else if (y1 > other.getY2()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Foothold)) {
            return false;
        }
        final Foothold oth = (Foothold) o;
        return oth.getY1() == getY1() && oth.getY2() == getY2()
                && oth.getX1() == getX1() && oth.getX2() == getX2() && getId() == oth.getId();
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(id);
        dos.writeInt(layerId);
        dos.writeInt(groupId);
        dos.writeInt(x1);
        dos.writeInt(y1);
        dos.writeInt(x2);
        dos.writeInt(y2);
        dos.writeInt(next);
        dos.writeInt(prev);
        dos.writeInt(force);
        dos.writeBoolean(forbidFallDown);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setId(dis.readInt());
        setLayerId(dis.readInt());
        setGroupId(dis.readInt());
        setX1(dis.readInt());
        setY1(dis.readInt());
        setX2(dis.readInt());
        setY2(dis.readInt());
        setNext(dis.readInt());
        setPrev(dis.readInt());
        setForce(dis.readInt());
        setForbidFallDown(dis.readBoolean());
        return this;
    }
}
package com.msemu.commons.utils.types;

/**
 * Created by Weber on 2018/3/29.
 */

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
        x = 0;
        y = 0;
    }

    public Position deepCopy() {
        return new Position(getX(), getY());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getRectAround(Rect rect) {
        int x = getX();
        int y = getY();
        return new Rect(x + rect.getLeft(), y + rect.getTop(), x + rect.getRight(), y + rect.getBottom());
    }

    public double distance(Position point) {
        return distance(point.getX(), point.getY());
    }

    public double distance(double x, double y) {
        return Math.sqrt(distanceSq(x, y));
    }

    public double distanceSq(Position point) {
        return distanceSq(point.getX(), point.getY());
    }

    public double distanceSq(double x, double y) {
        x -= getX();
        y -= getY();
        return (x * x + y * y);
    }

    @Override
    public String toString() {
        return String.format("X: %d Y: %d", getX(), getY());
    }
}

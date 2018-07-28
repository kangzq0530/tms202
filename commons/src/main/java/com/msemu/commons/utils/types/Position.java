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
        double diffX = (x - getX());
        double diffY = (y - getY());
        return (diffX * diffX + diffY * diffY);
    }

    @Override
    public String toString() {
        return String.format("X: %d Y: %d", getX(), getY());
    }
}

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

import java.awt.*;

/**
 * Created by Weber on 2018/3/28.
 */
public class Rect {
    private int left, top, right, bottom;

    public Rect() {

    }

    public Rect(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public Rect(Position lt, Position rb) {
        this.left = lt.getX();
        this.top = lt.getY();
        this.right = rb.getX();
        this.bottom = rb.getY();
    }

    public Rect(Point lt, Point rb) {
        this.left = (int) lt.getX();
        this.top = (int) lt.getY();
        this.right = (int) rb.getX();
        this.bottom = (int) rb.getY();
    }

    /**
     * Top left x coord
     *
     * @return
     */
    public int getLeft() {
        return left;
    }

    /**
     * Top left x coord
     *
     * @param left
     */
    public void setLeft(int left) {
        this.left = left;
    }

    /**
     * Top left y coord
     *
     * @return
     */
    public int getTop() {
        return top;
    }

    /**
     * Top left y coord
     *
     * @param top
     */
    public void setTop(int top) {
        this.top = top;
    }

    /**
     * Bottom right x coord
     *
     * @return
     */
    public int getRight() {
        return right;
    }

    /**
     * Bottom right x coord
     *
     * @param right
     */
    public void setRight(int right) {
        this.right = right;
    }

    /**
     * Bottom right y coord
     *
     * @return
     */
    public int getBottom() {
        return bottom;
    }

    /**
     * Bottom right y coord
     *
     * @param bottom
     */
    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    /**
     * Returns the width of this Rect.
     *
     * @return the width of this Rect.
     */
    public int getWidth() {
        return Math.abs(getLeft() - getRight());
    }

    /**
     * Returns the height of this Rect.
     *
     * @return The height of this Rect.
     */
    public int getHeight() {
        return Math.abs(getTop() - getBottom());
    }

    /**
     * Returns whether or not a {@link Position} is inside this Rect.
     *
     * @param position The Position to check.
     * @return if the position is not null and inside this Rect (rect.left < pos.x < rect.right &&
     * rect.top < pos.y < rect.bottom.
     */
    public boolean hasPositionInside(Position position) {
        int x = position.getX();
        int y = position.getY();
        return x >= left && y >= top && x <= right && y <= bottom;
    }

    public boolean hasPositionInside(Position center, Position position)
    {
        int x = position.getX();
        int y = position.getY();
        return x >= center.getX() + left && y >= center.getY() + top && x <=  center.getX() + right && y <= center.getY() + bottom;
    }

    /**
     * Move this Rect left by the width, effectively flipping around the left edge.
     *
     * @return The resulting Rect from the move.
     */
    public Rect moveLeft() {
        return new Rect(getLeft() - getWidth(), getTop(), getLeft(), getBottom());
    }

    /**
     * Move this Rect right by the width, effectively flipping around the right edge.
     *
     * @return The resulting Rect from the move.
     */
    public Rect moveRight() {
        return new Rect(getRight(), getTop(), getRight() + getWidth(), getBottom());
    }

    public boolean contains(Position p) {
        return contains(p.getX(), p.getY());
    }

    public boolean contains(int x, int y) {
        return inside(x, y);
    }

    public boolean inside(int X, int Y) {
        int w = getWidth();
        int h = getHeight();
        if ((w | h) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        int x = left;
        int y = bottom;
        if (X < x || Y < y) {
            return false;
        }
        w += x;
        h += y;
        //    overflow || intersect
        return ((w < x || w > X) &&
                (h < y || h > Y));
    }

}

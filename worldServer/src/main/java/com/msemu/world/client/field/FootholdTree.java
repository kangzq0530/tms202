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

package com.msemu.world.client.field;

import com.msemu.commons.data.templates.field.Foothold;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Tuple;
import lombok.Getter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Weber on 2018/4/24.
 */
@Getter
public class FootholdTree {
    private static final byte MAX_DEPTH = 8;
    private final List<Foothold> footholds = new LinkedList<>();
    private FootholdTree nw = null;
    private FootholdTree ne = null;
    private FootholdTree sw = null;
    private FootholdTree se = null;
    private Position leftBound;
    private Position rightBound;
    private Position center;
    private int depth = 0;
    private int maxDropX;
    private int minDropX;

    public FootholdTree() {

    }

    public FootholdTree(final Position leftBound, final Position rightBound) {
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        center = new Position((rightBound.getX() - leftBound.getX()) / 2, (rightBound.getY() - leftBound.getY()) / 2);
    }

    public FootholdTree(final Position p1, final Position p2, final int depth) {
        this.leftBound = p1;
        this.rightBound = p2;
        this.depth = depth;
        center = new Position((p2.getX() - p1.getX()) / 2, (p2.getY() - p1.getY()) / 2);
    }

    public static final int calcY(final Foothold fh, final Position p) {
        if (fh.isWall()) {
            return p.getY();
        } else if (fh.isSlope()) {
            int calcY;
            boolean bool = fh.getX1() < fh.getX2();
            int x1 = bool ? fh.getX1() : fh.getX2();
            int x2 = bool ? fh.getX2() : fh.getX1();
            int y1 = bool ? fh.getY1() : fh.getY2();
            int y2 = bool ? fh.getY2() : fh.getY1();
            if (p.getX() <= x1) {
                calcY = y1;
            } else if (p.getX() >= x2) {
                calcY = y2;
            } else {
                final double s1 = Math.abs(fh.getY2() - fh.getY1());
                final double s2 = Math.abs(fh.getX2() - fh.getX1());
                final double s4 = Math.abs(p.getX() - fh.getX1());
                final double alpha = Math.atan(s2 / s1);
                final double beta = Math.atan(s1 / s2);
                final double s5 = Math.cos(alpha) * (s4 / Math.cos(beta));
                if (fh.getY2() < fh.getY1()) {
                    calcY = fh.getY1() - (int) s5;
                } else {
                    calcY = fh.getY1() + (int) s5;
                }
            }
            return calcY;
        } else if (fh.isPlateform()) {
            return fh.getY1();
        } else {
            System.err.println("計算Fh的座標Y值錯誤");
            return p.getY();
        }
    }

    public final void insertFoothold(final Foothold foothold) {
        if (depth == 0) {
            if (foothold.getX1() > maxDropX) {
                maxDropX = foothold.getX1();
            }
            if (foothold.getX1() < minDropX) {
                minDropX = foothold.getX1();
            }
            if (foothold.getX2() > maxDropX) {
                maxDropX = foothold.getX2();
            }
            if (foothold.getX2() < minDropX) {
                minDropX = foothold.getX2();
            }
        }
        if (depth == MAX_DEPTH ||
                (foothold.getX1() >= leftBound.getX() && foothold.getX2() <= rightBound.getX() && foothold.getY1() >= leftBound.getY() && foothold.getY2() <= rightBound.getY())) {
            footholds.add(foothold);
        } else {
            if (nw == null) {
                nw = new FootholdTree(leftBound, center, depth + 1);
                ne = new FootholdTree(new Position(center.getX(), leftBound.getY()), new Position(rightBound.getX(), center.getY()), depth + 1);
                sw = new FootholdTree(new Position(leftBound.getX(), center.getY()), new Position(center.getX(), rightBound.getY()), depth + 1);
                se = new FootholdTree(center, rightBound, depth + 1);
            }
            if (foothold.getX2() <= center.getX() && foothold.getY2() <= center.getY()) {
                nw.insertFoothold(foothold);
            } else if (foothold.getX1() > center.getX() && foothold.getY2() <= center.getY()) {
                ne.insertFoothold(foothold);
            } else if (foothold.getX2() <= center.getX() && foothold.getY1() > center.getY()) {
                sw.insertFoothold(foothold);
            } else {
                se.insertFoothold(foothold);
            }
        }
    }

    public final List<Foothold> getAllRelevants() {
        return getAllRelevants(new LinkedList<>());
    }

    private List<Foothold> getAllRelevants(final List<Foothold> list) {
        list.addAll(footholds);
        if (nw != null) {
            nw.getAllRelevants(list);
            ne.getAllRelevants(list);
            sw.getAllRelevants(list);
            se.getAllRelevants(list);
        }
        return list;
    }

    private List<Foothold> getRelevants(final Position p) {
        return getRelevants(p, new LinkedList<>());
    }

    private List<Foothold> getRelevants(final Position p, final List<Foothold> list) {
        list.addAll(footholds);
        if (nw != null) {
            if (p.getX() <= center.getX() && p.getY() <= center.getY()) {
                nw.getRelevants(p, list);
            } else if (p.getX() > center.getX() && p.getY() <= center.getY()) {
                ne.getRelevants(p, list);
            } else if (p.getX() <= center.getX() && p.getY() > center.getY()) {
                sw.getRelevants(p, list);
            } else {
                se.getRelevants(p, list);
            }
        }
        return list;
    }

    private Foothold findWallR(final Position p1, final Position p2) {
        Foothold ret;
        for (final Foothold f : footholds) {
            //if (f.isWall()) System.outpacket.println(f.getX1() + " " + f.getX2());
            if (f.isWall() && f.getX1() >= p1.getX() && f.getX1() <= p2.getX() && f.getY1() >= p1.getY() && f.getY2() <= p1.getY()) {
                return f;
            }
        }
        if (nw != null) {
            if (p1.getX() <= center.getX() && p1.getY() <= center.getY()) {
                ret = nw.findWallR(p1, p2);
                if (ret != null) {
                    return ret;
                }
            }
            if ((p1.getX() > center.getX() || p2.getX() > center.getX()) && p1.getY() <= center.getY()) {
                ret = ne.findWallR(p1, p2);
                if (ret != null) {
                    return ret;
                }
            }
            if (p1.getX() <= center.getX() && p1.getY() > center.getY()) {
                ret = sw.findWallR(p1, p2);
                if (ret != null) {
                    return ret;
                }
            }
            if ((p1.getX() > center.getX() || p2.getX() > center.getX()) && p1.getY() > center.getY()) {
                ret = se.findWallR(p1, p2);
                if (ret != null) {
                    return ret;
                }
            }
        }
        return null;
    }

    public final Foothold findWall(final Position p1, final Position p2) {
        if (p1.getY() != p2.getY()) {
            throw new IllegalArgumentException();
        }
        return findWallR(p1, p2);
    }

    // To be refined, still inaccurate :(
    public final boolean checkRelevantFH(final short fromx, final short fromy, final short tox, final short toy) {
        Foothold fhdata = null;
        for (final Foothold fh : footholds) { // From
            if (fh.getX1() <= fromx && fh.getX2() >= fromx && fh.getY1() <= fromy && fh.getY2() >= fromy) { // monster pos is within
                fhdata = fh;
                break;
            }
        }
        for (final Foothold fh2 : footholds) { // To
            if (fh2.getX1() <= tox && fh2.getX2() >= tox && fh2.getY1() <= toy && fh2.getY2() >= toy) { // monster pos is within
                if (!(fhdata.getId() == fh2.getId() || fh2.getId() == fhdata.getNext() || fh2.getId() == fhdata.getPrev())) {
                    System.out.println("Couldn't find the correct pos for next/prev");
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public final List<Foothold> xMatches(final Position p) {
        final List<Foothold> relevants = getRelevants(p);
        // find fhs with matching x coordinates
        final List<Foothold> xMatches = new LinkedList<>();
        Integer minX = null;
        Integer maxX = null;
        // 初步篩選x軸在範圍內的fh
        for (final Foothold fh : relevants) {
            if (fh.isWall()) {
                continue;
            }
            if (fh.getX1() <= p.getX() && fh.getX2() >= p.getX()) {
                xMatches.add(fh);
            }
            if (minX == null) {
                minX = fh.getX1();
            } else if (fh.getX1() < minX) {
                minX = fh.getX1();
            }
            if (maxX == null) {
                maxX = fh.getX2();
            } else if (fh.getX2() > maxX) {
                maxX = fh.getX2();
            }
        }
        // 座標x軸超出最大值時選取最外圍的fh
        if (xMatches.isEmpty()) {
            int x = 0;
            if (minX != null && p.getX() <= minX) {
                x = minX;
            } else if (maxX != null && p.getX() >= maxX) {
                x = maxX;
            }
            for (final Foothold fh : relevants) {
                if (fh.isWall()) {
                    continue;
                }
                if (fh.getX1() <= x && fh.getX2() >= x) {
                    xMatches.add(fh);
                }
            }
        }
        Collections.sort(xMatches);
        return xMatches;
    }

    public final List<Foothold> yMatches(final Position p) {
        final List<Foothold> relevants = getRelevants(p);
        final List<Foothold> yMatches = new LinkedList<>();
        Integer minY = null;
        Integer maxY = null;
        // 初步篩選y軸在範圍內的fh
        for (final Foothold fh : relevants) {
            if (fh.isWall()) {
                continue;
            }
            int y1 = Math.min(fh.getY1(), fh.getY2());
            int y2 = Math.max(fh.getY1(), fh.getY2());
            if (y1 <= p.getY() && y2 >= p.getY()) {
                yMatches.add(fh);
            }
            if (minY == null) {
                minY = y1;
            } else if (y1 < minY) {
                minY = y1;
            }
            if (maxY == null) {
                maxY = y2;
            } else if (y2 > maxY) {
                maxY = y2;
            }
        }
        // 座標y軸超出最大值時選取最外圍的fh
        if (yMatches.isEmpty()) {
            int y = 0;
            if (minY != null && p.getY() <= minY) {
                y = minY;
            } else if (maxY != null && p.getY() >= maxY) {
                y = maxY;
            }
            for (final Foothold fh : relevants) {
                if (fh.isWall()) {
                    continue;
                }
                int y1 = Math.min(fh.getY1(), fh.getY2());
                int y2 = Math.max(fh.getY1(), fh.getY2());
                if (y1 <= y && y2 >= y) {
                    yMatches.add(fh);
                }
            }
        }
        Collections.sort(yMatches);
        return yMatches;
    }

    public final Foothold findBelow(final Position p, boolean flying) {
        final List<Foothold> xMatches = xMatches(p);
        Foothold lowestFh = null;
        Integer lowestY = null;
        for (final Foothold fh : xMatches) {
            if (fh.isWall()) {
                continue;
            }

            int calcY = FootholdTree.calcY(fh, p);

            if (flying) {
                if (calcY == p.getY()) {
                    return fh;
                }
                continue;
            } else if (calcY >= p.getY()) {
                return fh;
            }

            if (lowestY == null || calcY > lowestY) {
                lowestY = calcY;
                lowestFh = fh;
            }
        }
        return lowestFh;
    }

    public final Foothold findNearFloor(final Position p) {
        final List<Foothold> xMatches = xMatches(p);
        Foothold nearbyFh = null;
        Integer nearby = null;
        for (final Foothold fh : xMatches) {
            if (fh.isWall()) {
                continue;
            }
            int calcY = FootholdTree.calcY(fh, p);
            if (nearby == null || Math.abs(nearby - p.getY()) > Math.abs(calcY - p.getY())) {
                nearby = calcY;
                nearbyFh = fh;
            }
        }
        return nearbyFh;
    }

    public final Tuple<Foothold, Foothold> findFloorBorder(final Position p) {
        final List<Foothold> yMatches = yMatches(p);
        Foothold lFh = null;
        Foothold rFh = null;
        for (final Foothold fh : yMatches) {
            if (fh.isWall() || fh.isSlope()) {
                continue;
            }
            if (lFh == null || fh.getX1() < lFh.getX1()) {
                lFh = fh;
            }
            if (rFh == null || fh.getX2() > rFh.getX2()) {
                rFh = fh;
            }
        }
        return new Tuple<>(lFh, rFh);
    }

    public final int getX1() {
        return leftBound.getX();
    }

    public final int getX2() {
        return rightBound.getX();
    }

    public final int getY1() {
        return leftBound.getY();
    }

    public final int getY2() {
        return rightBound.getY();
    }

    public final int getMaxDropX() {
        return maxDropX - 25;
    }

    public final int getMinDropX() {
        return minDropX + 25;
    }

    public Foothold getFootholdById(int fh) {
        Foothold footHold = getFootholds().stream().filter(foothold -> foothold.getId() == fh).findFirst().orElse(null);
        if (footHold == null) {
            footHold = sw != null ? sw.getFootholdById(fh) : null;
        }
        if (footHold == null) {
            footHold = se != null ? se.getFootholdById(fh) : null;
        }
        if (footHold == null) {
            footHold = nw != null ? nw.getFootholdById(fh) : null;
        }
        if (footHold == null) {
            footHold = ne != null ? ne.getFootholdById(fh) : null;
        }
        return footHold;
    }
}

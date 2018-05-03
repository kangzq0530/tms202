package com.msemu.world.enums;

/**
 * Created by Weber on 2018/5/4.
 */
public enum DropEnterType {
    JustShowing(0),
    Create(1),
    OnTheFoothold(2), // ?
    FadingOut(3),;

    private byte val;

    DropEnterType(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }
}
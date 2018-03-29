package com.msemu.commons.enums;

/**
 * Created by Weber on 2018/3/14.
 */
public enum AccountTypeMask {
    NORMAL(0),
    MANAGER(4),
    TESTER(5),
    SUB_TESTER(13)
    ;
    private int mask;
    AccountTypeMask(int value) {
        this.mask |= 1 << value;
    }

    public int getMask() {
        return this.mask;
    }
}

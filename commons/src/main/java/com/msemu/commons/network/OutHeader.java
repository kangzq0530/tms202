package com.msemu.commons.network;

import com.msemu.core.startup.StartupComponent;

/**
 * Created by Weber on 2018/3/29.
 */

public enum OutHeader implements IHeader {
    ;

    private short value;

    @Override
    public short getValue() {
        return value;
    }

    @Override
    public void setValue(short value) {
        this.value = value;
    }

    public static OutHeader getOutHeaderByOp(int op) {
        for (OutHeader outHeader : OutHeader.values()) {
            if (outHeader.getValue() == op) {
                return outHeader;
            }
        }
        return null;
    }
}

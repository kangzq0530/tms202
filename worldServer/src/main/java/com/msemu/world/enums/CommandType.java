package com.msemu.world.enums;

/**
 * Created by Weber on 2018/5/18.
 */
public enum CommandType {

    NORMAL(0),

    TRADE(1);
    private final int type;

    CommandType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

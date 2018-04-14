package com.msemu.commons.enums;

/**
 * Created by Weber on 2018/4/14.
 */
public enum WorldServerType {

    Normal("normal"),
    Reboot("reboot");


    private String value;

    WorldServerType(String value) {
        this.value= value;
    }

    public String getValue() {
        return this.value;
    }

}

package com.msemu.commons.rmi.model;

import java.io.Serializable;

/**
 * Created by Weber on 2018/3/18.
 */
public enum WorldRegisterResult implements Serializable {
    SUCCESS,
    DISABLED,
    UNKNOWN,
    ALREADY_REGISTERED;

    WorldRegisterResult() {
    }
}

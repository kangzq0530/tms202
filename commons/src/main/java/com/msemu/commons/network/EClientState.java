package com.msemu.commons.network;

/**
 * Created by Weber on 2018/3/14.
 */
public enum EClientState {
    NULL,
    CONNECTED,
    AUTHED_GG,
    AUTHED,
    ENTERED,
    DISCONNECTED;

    EClientState() {
    }
}

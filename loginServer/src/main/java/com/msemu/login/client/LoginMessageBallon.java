package com.msemu.login.client;

import com.msemu.commons.utils.types.Position;

/**
 * Created by Weber on 2018/3/30.
 */
public class LoginMessageBallon {

    private final Position position;

    private final String message;

    public LoginMessageBallon(Position position, String message) {
        this.position = position;
        this.message = message;
    }

    public Position getPosition() {
        return position;
    }

    public String getMessage() {
        return message;
    }

}

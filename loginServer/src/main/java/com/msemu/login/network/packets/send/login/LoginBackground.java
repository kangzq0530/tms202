package com.msemu.login.network.packets.send.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.DateTimeUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Weber on 2018/3/30.
 */
public class LoginBackground extends OutPacket {

    public LoginBackground() {
        super(OutHeader.LoginBackground);
        List<String> background = Arrays.asList(
                "MapLogin", "MapLogin0", "MapLogin1", "MapLogin2"
        );
        Collections.shuffle(background);
        this.encodeString(background.get(0));
        this.encodeTime(DateTimeUtils.getCurrentDate());
        this.encodeByte(true);
    }
}

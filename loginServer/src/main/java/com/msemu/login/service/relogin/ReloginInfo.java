package com.msemu.login.service.relogin;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/2.
 */
@Getter
@Setter
public class ReLoginInfo {
    private String username;
    private int world;
    private int channel;
    private String token;
}

package com.msemu.login.service;

import com.msemu.core.startup.StartupComponent;
import com.msemu.login.service.relogin.ReLoginInfo;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/5/2.
 */
@StartupComponent("Service")
public class LoginCookieService {
    private static final AtomicReference<LoginCookieService> instance = new AtomicReference<>();

    @Getter
    private Map<String, ReLoginInfo> tokens = new HashMap<>();


    public static LoginCookieService getInstance() {
        LoginCookieService value = instance.get();
        if (value == null) {
            synchronized (LoginCookieService.instance) {
                value = instance.get();
                if (value == null) {
                    value = new LoginCookieService();
                }
                instance.set(value);
            }
        }
        return value;
    }

    public ReLoginInfo getReLoginInfoByToken(String token) {
        if (getTokens().containsKey(token)) {
            ReLoginInfo ret = getTokens().get(token);
            return ret;
        }
        return null;
    }

    public void removeReLoginInfoByToken(String token) {
        if(getTokens().containsKey(token)) {
            getTokens().remove(token);
        }
    }

    public void addToken(String token, String username, int world, int channel) {
        ReLoginInfo info = new ReLoginInfo();
        info.setChannel(channel);
        info.setToken(token);
        info.setUsername(username);
        info.setWorld(world);
        getTokens().put(token, info);
    }

}

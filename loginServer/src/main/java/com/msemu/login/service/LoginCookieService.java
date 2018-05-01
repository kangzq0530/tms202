package com.msemu.login.service;

import com.msemu.core.startup.StartupComponent;
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
    private Map<String, String> tokens = new HashMap<>();

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

    public String retriveUsernameByToken(String token) {
        if(getTokens().containsKey(token)) {
            String username = getTokens().get(token);
            return username;
        }
        return null;
    }

    public void addToken(String username , String token) {
        getTokens().put(token, username);
    }

}

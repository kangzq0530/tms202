package com.msemu.world.service;

import com.msemu.WorldServer;
import com.msemu.commons.utils.Rand;
import com.msemu.core.startup.StartupComponent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/5/1.
 */
@StartupComponent("Service")
public class LoginCookieService {

    private static final AtomicReference<LoginCookieService> instance = new AtomicReference<>();

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

    private String generateToken(String username) {
        String token = Rand.nextString();
        WorldServer.getRmi().requestRelogin(username, token);
        return token;
    }

}

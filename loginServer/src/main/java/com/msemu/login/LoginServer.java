package com.msemu.login;

import com.msemu.core.startup.StartupLevel;
import com.msemu.core.startup.StartupManager;
import com.msemu.login.service.LoginServerRMI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Weber on 2018/3/14.
 */
public class LoginServer {
    private static final Logger log = LoggerFactory.getLogger((Class) LoginServer.class);
    private static LoginServerRMI rmi;

    private LoginServer() throws Exception {
        StartupManager.getInstance().startup(StartupLevel.class);
        LoginServer.rmi = new LoginServerRMI();
    }

    public static void main(final String[] array) {
        try {
            new LoginServer();
        } catch (Exception except) {
            LoginServer.log.error("Error while starting LoginServer", except);
        }
    }

    public static LoginServerRMI getRmi() {
        return LoginServer.rmi;
    }
}

package com.msemu.login.data;

import com.msemu.commons.data.loader.wz.WzManager;
import com.msemu.core.startup.StartupComponent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/21.
 */
@StartupComponent("Wz")
public class LoginWzManager extends WzManager {

    private static final AtomicReference<LoginWzManager> instance = new AtomicReference<>();


    public static LoginWzManager getInstance() {
        LoginWzManager value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new LoginWzManager();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public LoginWzManager() {
        super();
        //super(WzManager.ETC, WzManager.ITEM, WzManager.CHARACTER);
    }

}

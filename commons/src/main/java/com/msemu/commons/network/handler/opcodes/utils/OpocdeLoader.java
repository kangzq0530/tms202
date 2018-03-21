package com.msemu.commons.network.handler.opcodes.utils;

import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;

/**
 * Created by Weber on 2018/3/18.
 */

@Reloadable(
        name = "all",
        group = "configs"
)
@StartupComponent("Configure")
public class OpocdeLoader implements IReloadable {
    @Override
    public void reload() {

    }
}

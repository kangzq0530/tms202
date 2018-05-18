package com.msemu.world.client.character.commands;

import com.msemu.core.network.GameClient;
import com.msemu.world.enums.CommandType;

import java.util.List;

/**
 * Created by Weber on 2018/5/18.
 */
public abstract class CommandExecute {

    public abstract boolean execute(GameClient client, List<String> args);

    public abstract String getHelpMessage();

    public CommandType getType() {
        return CommandType.NORMAL;
    }

}

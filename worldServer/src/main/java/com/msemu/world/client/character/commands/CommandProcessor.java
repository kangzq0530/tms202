package com.msemu.world.client.character.commands;

import lombok.Getter;

import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Created by Weber on 2018/5/7.
 */
public class CommandProcessor {

    @Getter
    private final static HashMap<String, Class<? extends CommandExecute>> commands = new HashMap<>();

    static {

        Class<?>[] CommandFiles = {
                AdminCommand.class
        };

        for (Class<?> clazz : CommandFiles) {
            Class<?>[] commandClazzs = clazz.getDeclaredClasses();
            for (Class<?> cmdClazz : commandClazzs) {
                if(Modifier.isAbstract(cmdClazz.getModifiers())|| cmdClazz.isSynthetic()) {
                    continue;
                }
                commands.put(clazz.getName().toLowerCase(), (Class<? extends CommandExecute>) clazz);
            }
        }
    }
}

package com.msemu.world.client.character;

import com.msemu.world.enums.ScriptType;

/**
 * Created by Weber on 2018/4/13.
 */
public class ScriptManager {

    private final Character character;

    public ScriptManager(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    public void startScript(int id, String script, ScriptType field) {

    }
}

package com.msemu.world.client.scripting;

import com.msemu.world.enums.ScriptType;
import lombok.Getter;
import lombok.Setter;

import javax.script.Invocable;
import javax.script.ScriptEngine;

/**
 * Created by Weber on 2018/4/30.
 */
@Getter
@Setter
public class ScriptInfo {
    private ScriptInteraction interaction;
    private ScriptType scriptType;
    private int parentID;
    private String scriptName;
    private Invocable invocable;
}

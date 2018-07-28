/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.world.client.scripting;

import com.msemu.commons.utils.FileUtils;
import com.msemu.core.configs.CoreConfig;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.NpcMessageType;
import com.msemu.world.enums.ScriptType;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Weber on 2018/4/13.
 */
public class ScriptManager {
    public static final Logger log = LoggerFactory.getLogger(ScriptManager.class);

    public static final String SCRIPT_ENGINE_EXTENSION = ".js";
    public static final String QUEST_START_SCRIPT_END_TAG = "s";
    public static final String QUEST_COMPLETE_SCRIPT_END_TAG = "e";
    private static final String SCRIPT_ENGINE_NAME = "nashorn";
    private static final String DEFAULT_SCRIPT = "undefined";

    @Getter
    private static final ScriptEngineManager engineManager = new ScriptEngineManager();
    @Getter
    private final Character character;
    @Getter
    @Setter
    private ScriptInfo scriptInfo;
    @Getter
    private ReentrantLock lock = new ReentrantLock();

    public ScriptManager(Character character) {
        this.character = character;
    }


    public void startScript(int parentID, String scriptName, ScriptType scriptType) {
        startScript(parentID, scriptName, scriptType, "start");
    }

    public void startScript(int parentID, String scriptName, ScriptType scriptType, String functionName) {
        withLock(() -> {
            try {
                if (getScriptInfo() != null) {
                    getCharacter().chatMessage(String.format("執行腳本失敗 ID: %d 腳本名稱: %s 種類: %s ( 目前已經有腳本執行中 ID: %d 腳本名稱: %s 種類: %s )"
                            , parentID, scriptName, scriptType.name()
                            , getScriptInfo().getParentID(), getScriptInfo().getScriptName(), getScriptInfo().getScriptType().name()));
                } else {
                    getCharacter().chatMessage(String.format("開始執行腳本 ID: %d 腳本名稱: %s 種類: %s", parentID, scriptName, scriptType.name()));
                    ScriptInfo scriptInfo = new ScriptInfo();
                    scriptInfo.setParentID(parentID);
                    scriptInfo.setScriptName(scriptName);
                    scriptInfo.setScriptType(scriptType);
                    scriptInfo.setInvocable(getInvocable(scriptName, scriptType));
                    scriptInfo.setInteraction(new ScriptInteraction(scriptType, parentID, scriptName, getCharacter()));
                    Invocable engine = scriptInfo.getInvocable();
                    if (engine != null) {
                        setScriptInfo(scriptInfo);
                        ((ScriptEngine) engine).put("cm", scriptInfo.getInteraction());
                        ((ScriptEngine) engine).put("parentID", scriptInfo.getParentID());
                        engine.invokeFunction(functionName);
                    }
                }
            } catch (NoSuchMethodException | ScriptException e) {
                log.error("startScript error", e);
            }
        });
    }

    public void stopScript() {
        getLock().lock();
        try {
            setScriptInfo(null);
            getCharacter().enableActions();
        } finally {
            getLock().unlock();
        }
    }

    private void stopScriptWithoutLock() {
        setScriptInfo(null);
        getCharacter().enableActions();
    }

    private ScriptEngine getScriptEngine() {
        return getEngineManager().getEngineByName(SCRIPT_ENGINE_NAME);
    }

    private Invocable getInvocable(String scriptName, ScriptType scriptType) {
        String path = String.format("%s/%s/%s%s", CoreConfig.SCRIPT_PATH,
                scriptType.name().toLowerCase(), scriptName, SCRIPT_ENGINE_EXTENSION);
        boolean exists = new File(path).exists();
        if (!exists) {
            log.error(String.format("[Error] Could not find script %s/%s.", scriptType.toString().toLowerCase(), scriptName));
            path = String.format("%s/%s/%s%s", CoreConfig.SCRIPT_PATH,
                    scriptType.toString().toLowerCase(), DEFAULT_SCRIPT, SCRIPT_ENGINE_EXTENSION);
        }
        try {
            ScriptEngine scriptEngine = getScriptEngine();
            String content = FileUtils.readFile(path, Charset.forName("UTF-8"));
            CompiledScript cs = ((Compilable) scriptEngine).compile(content);
            cs.eval();
            return (Invocable) scriptEngine;
        } catch (IOException e) {
            log.error(String.format("Unable to read script file %s!", path), e);
        } catch (ScriptException e) {
            log.error(String.format("Unable to execute script file %s!", path), e);
        }
        return null;
    }

    public void handleAction(NpcMessageType lastType, byte action, int selection) {
        if (getScriptInfo() != null) {
            withLock(() -> {
                try {
                    ScriptInteraction cm = getScriptInfo().getInteraction();
                    if (lastType.getValue() != cm.getNpcScriptInfo().getLastMessageType().getValue()) {
                        stopScriptWithoutLock();
                    } else if (cm.getNpcScriptInfo().getLastMessageType() == NpcMessageType.NM_SAY && !cm.getNpcScriptInfo().isPrev()
                            && !cm.getNpcScriptInfo().isNext()) {
                        stopScriptWithoutLock();
                    } else {
                        if (getScriptInfo() != null) {
                            getScriptInfo().getInvocable().invokeFunction("action", action, lastType.getValue(), selection);
                        }
                    }
                } catch (NoSuchMethodException | ScriptException e) {
                    log.error("Unable to execute script function \"action\"", e);
                    stopScriptWithoutLock();
                }
            });
        }
    }

    private void withLock(Runnable runnable) {
        getLock().lock();
        try {
            runnable.run();
        } finally {
            getLock().unlock();
        }
    }
}

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
    private static final String SCRIPT_ENGINE_NAME = "nashorn";
    public static final String SCRIPT_ENGINE_EXTENSION = ".js";
    private static final String DEFAULT_SCRIPT = "undefined";
    public static final String QUEST_START_SCRIPT_END_TAG = "s";
    public static final String QUEST_COMPLETE_SCRIPT_END_TAG = "e";
    @Getter
    private static final ScriptEngineManager engineManager = new ScriptEngineManager();
    @Getter
    private final Character character;
    @Getter
    @Setter
    private ScriptInfo scriptInfo;
    @Getter
    private ReentrantLock lock = new ReentrantLock();

    public static final Logger log = LoggerFactory.getLogger(ScriptManager.class);

    public ScriptManager(Character character) {
        this.character = character;
    }

    public void startScript(int parentID, String scriptName, ScriptType scriptType) {
        startScript(parentID, scriptName, scriptType, "start");
    }

    public void startScript(int parentID, String scriptName, ScriptType scriptType, String functionName) {
        getLock().lock();
        try {
            if (getScriptInfo() != null) {
                getCharacter().chatMessage(String.format("執行腳本失敗 ID: %d 腳本名稱: %s 種類: %s ( 目前已經有腳本執行中 ID: %d 腳本名稱: %s 種類: %s )"
                        , parentID, scriptName, scriptType.name()
                        , getScriptInfo().getParentID(), getScriptInfo().getScriptName(), getScriptInfo().getScriptType().name()));
                return;
            }
            getCharacter().chatMessage(String.format("開始執行腳本 ID: %d 腳本名稱: %s 種類: %s", parentID, scriptName, scriptType.name()));
            ScriptInfo scriptInfo = new ScriptInfo();
            scriptInfo.setParentID(parentID);
            scriptInfo.setScriptName(scriptName);
            scriptInfo.setScriptType(scriptType);
            scriptInfo.setInvocable(getInvocable(scriptName, scriptType));
            scriptInfo.setInteraction(new ScriptInteraction(scriptType, parentID, scriptName, getCharacter()));
            Invocable engine = scriptInfo.getInvocable();
            if (engine == null)
                return;
            setScriptInfo(scriptInfo);
            ((ScriptEngine) engine).put("cm", scriptInfo.getInteraction());
            ((ScriptEngine) engine).put("parentID", scriptInfo.getParentID());
            engine.invokeFunction(functionName);
        } catch (NoSuchMethodException | ScriptException e) {
            e.printStackTrace();
        } finally {
            getLock().unlock();
        }
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
        getLock().lock();
        try {
            if(getScriptInfo() == null)
                return;
            ScriptInteraction cm = getScriptInfo().getInteraction();
            if(lastType.getValue() != cm.getNpcScriptInfo().getLastMessageType().getValue()) {
                stopScriptWithoutLock();
                return;
            } else if (cm.getNpcScriptInfo().getLastMessageType() == NpcMessageType.NM_SAY_OK) {
                stopScriptWithoutLock();
                return;
            }

            if (getScriptInfo() != null) {
                getScriptInfo().getInvocable().invokeFunction("action", action, lastType.getValue(), selection);
            }
        } catch (NoSuchMethodException | ScriptException e) {
            log.error("Unable to execute script function \"action\"", e);
            stopScriptWithoutLock();
        } finally {
            getLock().unlock();
        }
    }
}

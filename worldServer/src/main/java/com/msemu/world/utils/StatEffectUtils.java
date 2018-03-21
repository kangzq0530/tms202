package com.msemu.world.utils;

import com.msemu.commons.data.provider.MapleDataTool;
import com.msemu.commons.data.provider.MapleDataType;
import com.msemu.commons.data.provider.interfaces.MapleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Weber on 2018/3/21.
 */
public class StatEffectUtils {

    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    private static final Logger log = LoggerFactory.getLogger(StatEffectUtils.class);

    static {
        String scriptEval = "\n" +
                "function log20(x) {\n" +
                "    return Math.log(x)/Math.log(20);\n" +
                "}\n" +
                "\n" +
                "function log40(x) {\n" +
                "    return Math.log(x)/Math.log(40);\n" +
                "}\n" +
                "\n" +
                "function log70(x) {\n" +
                "    return Math.log(x)/Math.log(70);\n" +
                "}\n" +
                "\n" +
                "function d(x) {\n" +
                "    return Math.floor(x);\n" +
                "}\n" +
                "\n" +
                "function u(x) {\n" +
                "    return Math.ceil(x);\n" +
                "}\n" +
                "\n" +
                "function min(a, b) {\n" +
                "    return Math.min(a, b);\n" +
                "}\n" +
                "\n" +
                "function max(a, b) {\n" +
                "    return Math.max(a, b);\n" +
                "}\n" +
                "\n" +
                "function atan2(a, b) {\n" +
                "    return Math.atan2(a, b);\n" +
                "}\n" +
                "\n" +
                "function execute(expression) {\n" +
                "    expression = expression.replace('%', '/100.0');" +
                "    return eval(expression);\n" +
                "}";

        try {
            engine.eval(scriptEval);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    public static int parseEval(String path, MapleData source, int def, String vars, int value) {
        if (vars == null)
            return MapleDataTool.getInt(path, source, def);
        final MapleData data = source.getChildByPath(path);
        if (data == null)
            return def;
        if (data.getType() != MapleDataType.STRING)
            return MapleDataTool.getInt(path, source, def);

        String expression = MapleDataTool.getString(data);
        return evalEffectValue(expression, vars, value, def);
    }


    public static int evalEffectValue(String expression, String vars, int level, int def) {

        try {
            engine.put("y", level);
            engine.put(vars.toUpperCase(), level);
            engine.put(vars, level);
            Object result = ((Invocable) engine).invokeFunction("execute", expression);
            if (result instanceof Integer) {
                return (Integer) result;
            }
            return ((Double) result).intValue();

        } catch (ScriptException | NoSuchMethodException e) {
            log.error("evaluate error Expression: " + expression, e);
        }
        return def;
    }
}

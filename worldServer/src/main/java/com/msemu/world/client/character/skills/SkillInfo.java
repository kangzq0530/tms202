package com.msemu.world.client.character.skills;

import com.msemu.commons.utils.StringUtils;
import com.msemu.commons.utils.types.Rect;
import com.msemu.world.enums.SkillStat;
import lombok.Getter;
import lombok.Setter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/4/11.
 */
public class SkillInfo {
    @Getter
    @Setter
    private int skillId;
    @Getter
    @Setter
    private int rootId;
    @Getter
    @Setter
    private int maxLevel;
    @Getter
    @Setter
    private int currentLevel;
    @Getter
    @Setter
    private boolean invisible;
    @Getter
    @Setter
    private int masterLevel;
    @Getter
    @Setter
    private int fixLevel;
    @Getter
    @Setter
    private boolean massSpell;

    @Getter
    private List<Rect> rects = new ArrayList<>();
    @Getter
    private Map<SkillStat, String> skillStatInfo = new HashMap<>();

    public void addSkillStatInfo(SkillStat sc, String value) {
        getSkillStatInfo().put(sc, value);
    }

    public int getValue(SkillStat skillStat, int slv) {
        int result = 0;
        String value = getSkillStatInfo().get(skillStat);
        if(value == null) {
            return 0;
        }
        if(StringUtils.isNumber(value)) {
            result = Integer.parseInt(value);
        } else {
           result = evaluateValue(value, slv);
        }
        return result;
    }

    private int evaluateValue(String expression, int slv) {
        int result = 0;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        try {
            expression = expression.replace("u", "Math.ceil");
            expression = expression.replace("d", "Math.floor");
            Object res = engine.eval(expression.replace("x", slv + ""));
            if(res instanceof Integer) {
                result = (Integer) res;
            } else if(res instanceof Double) {
                result = ((Double) res).intValue();
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addRect(Rect rect) {
        getRects().add(rect);
    }

    public Rect getLastRect() {
        return rects != null && rects.size() > 0 ? rects.get(rects.size() - 1) : null;
    }

    public Rect getFirstRect() {
        return rects != null && rects.size() > 0 ? rects.get(0) : null;
    }

}

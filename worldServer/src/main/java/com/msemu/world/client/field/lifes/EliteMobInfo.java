package com.msemu.world.client.field.lifes;

import com.msemu.commons.utils.Rand;
import com.msemu.world.enums.EliteMobAttribute;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/5/14.
 */
public class EliteMobInfo {

    @Getter
    private final Map<EliteMobAttribute, Integer> eliteAttrs = new HashMap<>();
    @Getter
    private int grade;

    public EliteMobInfo() {
        this.grade = Rand.get(3);
        this.addEliteAttribute(EliteMobAttribute.getRandomAttribute(), 100);
    }

    public EliteMobInfo(int grade) {
        this.grade = grade;
        this.addEliteAttribute(EliteMobAttribute.getRandomAttribute(), 100);
    }

    public EliteMobInfo(int grade, Map<EliteMobAttribute, Integer> eliteAttrs) {
        this.grade = grade;
        this.eliteAttrs.putAll(eliteAttrs);
    }


    public final void addEliteAttribute(EliteMobAttribute att, int unk) {
        eliteAttrs.put(att, unk);
    }

    public void removeEliteAttribute(EliteMobAttribute att) {
        eliteAttrs.remove(att);
    }

}

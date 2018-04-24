package com.msemu.commons.data.templates;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/22.
 */
@Getter
@Setter
public class NpcTemplate {

    private int id;

    private String name;

    private List<String> scripts = new ArrayList<>();

    @Override
    public String toString() {
        return String.format("[NPC] %s(%d) Scripts: %s", getName(), getId(), getScripts().stream().collect(Collectors.joining(", ")));
    }
}

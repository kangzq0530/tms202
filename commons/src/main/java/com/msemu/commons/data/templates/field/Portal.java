package com.msemu.commons.data.templates.field;

import com.msemu.commons.data.enums.PortalType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/23.
 */
@Getter
@Setter
public class Portal {
    protected PortalType type;
    protected String name = "";
    protected int targetMapId;
    protected String targetPortalName = "";
    protected int x;
    protected int y;
    protected int horizontalImpact;
    protected int verticalImpact;
    protected String script = "";
    protected boolean onlyOnce;
    protected boolean hideTooltip;
    protected int delay;
    protected int id;

    public Portal() {

    }

    public Portal(int id, PortalType type, String name, int targetMapId, String targetPortalName, int x, int y,
                  int horizontalImpact, int verticalImpact, String script, boolean onlyOnce, boolean hideTooltip,
                  int delay) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.targetMapId = targetMapId;
        this.targetPortalName = targetPortalName;
        this.x = x;
        this.y = y;
        this.horizontalImpact = horizontalImpact;
        this.verticalImpact = verticalImpact;
        this.script = script;
        this.onlyOnce = onlyOnce;
        this.hideTooltip = hideTooltip;
        this.delay = delay;
    }
}

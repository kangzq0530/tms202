package com.msemu.commons.data.templates.field;

import com.msemu.commons.data.enums.PortalType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/11.
 */
@Getter
@Setter
public class Portal {
    private PortalType type;
    private String name = "";
    private int targetMapId;
    private String targetPortalName = "";
    private int x;
    private int y;
    private int horizontalImpact;
    private int verticalImpact;
    private String script = "";
    private boolean onlyOnce;
    private boolean hideTooltip;
    private int delay;
    private int id;

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

    public Portal deepCopy() {
        Portal copy = new Portal(getId(), getType(), getName(), getTargetMapId(), getTargetPortalName(), getX(), getY(), getHorizontalImpact(), getVerticalImpact(), getScript(), isOnlyOnce(), isHideTooltip(), getDelay());
        return copy;
    }
}
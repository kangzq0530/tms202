package com.msemu.world.client.field.lifes;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/13.
 */
public abstract class InternalLife extends Life {

    @Getter
    private final int templateId;

    @Getter
    @Setter
    private int f, cy, rx0, rx1;
    @Getter
    @Setter
    private boolean hide;

    public InternalLife(int templateId) {
        this.templateId = templateId;
        this.hide = false;
    }


}

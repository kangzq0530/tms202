package com.msemu.world.client.field.lifes;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/13.
 */
public abstract class AbstractInternalAnimatedLife extends AbstractAnimatedFieldLife {

    @Getter
    private final int templateId;

    @Getter
    @Setter
    private int f, fh, cy, rx0, rx1;
    @Getter
    @Setter
    private boolean hide;

    public AbstractInternalAnimatedLife(int templateId) {
        this.templateId = templateId;
        this.hide = false;
    }


}

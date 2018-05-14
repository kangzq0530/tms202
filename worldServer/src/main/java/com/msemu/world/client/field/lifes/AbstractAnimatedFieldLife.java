package com.msemu.world.client.field.lifes;


import com.msemu.world.client.field.AbstractFieldObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/5/13.
 */
public abstract class AbstractAnimatedFieldLife extends AbstractFieldObject {

    @Getter
    @Setter
    private byte action;

    @Getter
    @Setter
    private int fh;

    public boolean isLeft() {
        return getAction() % 2 != 0;
    }


}

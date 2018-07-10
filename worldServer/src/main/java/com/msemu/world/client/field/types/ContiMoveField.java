package com.msemu.world.client.field.types;

import com.msemu.commons.data.templates.field.FieldTemplate;
import com.msemu.world.client.field.Field;
import com.msemu.world.enums.ContiState;

/**
 * Created by Weber on 2018/6/1.
 */
public class ContiMoveField extends Field {

    private ContiState state;


    public ContiMoveField(FieldTemplate template) {
        super(template);
    }

}

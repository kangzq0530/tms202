package com.msemu.commons.utils.types;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/21.
 */
public class Size {

    @Getter
    @Setter
    private int width;

    @Getter
    @Setter
    private int height;

    public Size(int width, int height) {
        this.width= width;
        this.height = height;
    }
}

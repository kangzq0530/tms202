package com.msemu.commons.captcha;

import lombok.Getter;

import java.util.Arrays;

/**
 * Created by Weber on 2018/4/19.
 */
public class Captcha {

    @Getter
    private final String answer;
    @Getter
    private final byte[] imageData;

    public Captcha(final String answer, final byte[] imageData) {
        this.answer = answer;
        this.imageData = Arrays.copyOf(imageData, imageData.length);
    }

}

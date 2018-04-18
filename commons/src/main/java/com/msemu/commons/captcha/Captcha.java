package com.msemu.commons.captcha;

import io.netty.util.AttributeKey;
import lombok.Getter;

/**
 * Created by Weber on 2018/4/19.
 */
public class Captcha {

    @Getter
    private final String answer;
    @Getter
    private final byte[] imageData;

    public Captcha(String answer, byte[] imageData) {
        this.answer = answer;
        this.imageData = imageData;
    }

}

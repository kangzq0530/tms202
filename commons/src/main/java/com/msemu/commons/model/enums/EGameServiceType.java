package com.msemu.commons.model.enums;

import java.nio.charset.Charset;

/**
 * Created by Weber on 2018/3/14.
 */
public enum EGameServiceType {
    Korean(1, "MS949"),
    KoreanTest(2, "MS949"),
    Japan(3, "MS932"),
    China(4, "MS936"),
    Tespia(5, "MS949"),
    Taiwan(6, "MS950"),
    Sea(7, "MS949"),
    Global(8, "MS949"),
    Brazil(9, "MS949");

    int local;
    Charset charset;

    EGameServiceType(int local, String codePage) {
        this.local = local;
        this.charset = Charset.forName(codePage);
    }

    public int getLocal() {
        return this.local;
    }

    public Charset getCharset() {
        return this.charset;
    }
}

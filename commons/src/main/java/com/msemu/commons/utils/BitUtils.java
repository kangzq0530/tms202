package com.msemu.commons.utils;

/**
 * Created by Weber on 2018/3/16.
 */
public class BitUtils {

    public static byte[] multiplyBytes(byte[] iv, int i, int i0) {
        byte[] ret = new byte[i * i0];
        for (int x = 0; x < ret.length; x++) {
            ret[x] = iv[x % i];
        }
        return ret;
    }

    public static byte rollLeft(byte in, int count) {
        return (byte) (((in & 0xFF) << (count % 8) & 0xFF)
                | ((in & 0xFF) << (count % 8) >> 8));
    }

    public static byte rollRight(byte in, int count) {
        int tmp = (int) in & 0xFF;
        tmp = (tmp << 8) >>> (count % 8);
        return (byte) ((tmp & 0xFF) | (tmp >>> 8));
    }
}

package com.msemu.commons.utils;

import com.msemu.commons.enums.GameServiceType;
import com.msemu.core.configs.CoreConfig;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by Weber on 2018/3/14.
 */
public class HexUtils {
    public HexUtils() {
    }

    public static void hexToString(String string) {
        String[] stringArray = string.split(" ");
        byte[] bytes = new byte[stringArray.length];

        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte) Integer.parseInt(stringArray[i], 16);
        }

        System.out.println(new String(bytes));
    }

    public static String stringToHex(String string) {
        byte[] chars = string.getBytes();
        return byteArraytoHex(chars);
    }

    public static String byteArraytoHex(byte[] b) {
        StringBuilder out = new StringBuilder();
        byte[] var2 = b;
        int var3 = b.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            byte c = var2[var4];
            out.append(Integer.toHexString(c & 255)).append(" ");
        }

        return out.toString();
    }

    public static String readableByteArray(byte[] arr) {
        StringBuilder res = new StringBuilder();
        for (byte b : arr) {
            res.append(String.format("%02X ", b));
        }
        return res.toString();
    }

    public static String toHex(ByteBuffer data, int endPOsition) {
        StringBuilder result = new StringBuilder();
        int counter = 0;

        while (data.hasRemaining() && (endPOsition <= 0 || data.position() < endPOsition)) {
            if (counter % 16 == 0) {
                result.append(String.format("%04X: ", new Object[]{Integer.valueOf(counter)}));
            }

            int b = data.get() & 255;
            result.append(String.format("%02X ", new Object[]{Integer.valueOf(b)}));
            ++counter;
            if (counter % 16 == 0) {
                result.append("  ");
                toText(data, result, 16);
                result.append("\n");
            }
        }

        int rest = counter % 16;
        if (rest > 0) {
            for (int i = 0; i < 17 - rest; ++i) {
                result.append("   ");
            }

            toText(data, result, rest);
        }

        return result.toString();
    }

    private static void toText(ByteBuffer data, StringBuilder result, int cnt) {
        int charPos = data.position() - cnt;

        for (int a = 0; a < cnt; ++a) {
            byte c = data.get(charPos++);
            if (c > 31 && c < 128) {
                result.append((char) c);
            } else {
                result.append('.');
            }
        }

    }

    public static String toAscii(final byte[] bytes) {
        byte[] ret = new byte[bytes.length];
        for (int x = 0; x < bytes.length; x++) {
            if (bytes[x] < 32 && bytes[x] >= 0) {
                ret[x] = '.';
            } else {
                int chr = ((short) bytes[x]) & 0xFF;
                ret[x] = (byte) chr;
            }
        }
        Charset encode = CoreConfig.GAME_SERVICE_TYPE.getCharset();
        try {
            String str = new String(ret, encode);
            return str;
        } catch (Exception e) {
        }
        return "";
    }

    public static String fillHex(int data, int digits) {
        String hex = Integer.toHexString(data);
        StringBuilder number = new StringBuilder(Math.max(hex.length(), digits));

        for (int i = hex.length(); i < digits; ++i) {
            number.append(0);
        }

        number.append(hex);
        return number.toString();
    }

    public static byte[] hex2Byte(String str) {
        str = str.trim().replace(" ", "");
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static String toString(final int intValue) {
        return Integer.toHexString(intValue);
    }

}

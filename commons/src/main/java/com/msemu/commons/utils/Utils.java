package com.msemu.commons.utils;

import java.util.List;

public class Utils {
    public static <T> T getRandomFromList(List<T> list) {
        if(list != null && list.size() > 0) {
            return list.get(Rand.get(list.size()));
        }
        return null;
    }
}

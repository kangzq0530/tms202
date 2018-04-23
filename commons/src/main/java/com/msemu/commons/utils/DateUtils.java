package com.msemu.commons.utils;

import java.util.Calendar;

/**
 * Created by Weber on 2018/4/23.
 */
public class DateUtils {
    public static int getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}

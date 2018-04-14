package com.msemu.commons.utils;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Weber on 2018/3/29.
 */
public class DateTimeUtils {

    public static int getCurrentDate() {
        LocalDateTime date = LocalDateTime.now();
        return date.getYear() << 6 + date.getMonthValue() << 4 + date.getDayOfMonth() << 2 + date.getHour();
    }
}

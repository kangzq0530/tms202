/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.commons.utils;

import com.sun.management.HotSpotDiagnosticMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Weber on 2018/3/14.
 */
public final class ServerInfoUtils {
    private static final Logger log = LoggerFactory.getLogger(ServerInfoUtils.class);

    private ServerInfoUtils() {
    }

    public static void printSection(String sectionName) {
        StringBuilder sb = new StringBuilder(120);

        for (int i = 0; i < 117 - sectionName.length() - 2; ++i) {
            sb.append('-');
        }

        sb.append("={ ").append(sectionName).append(" }");
        log.info(sb.toString());
    }

    public static String getDoneMessage(String s) {
        StringBuilder builder = new StringBuilder();
        builder.append(s);
        while (builder.length() < 83) {
            builder.append(" ");
        }
        builder.append(" ...done");
        return builder.toString();
    }

    public static int getAvailableProcessors() {
        Runtime rt = Runtime.getRuntime();
        return rt.availableProcessors();
    }

    public static String getOSName() {
        return System.getProperty("os.name");
    }

    public static String getOSVersion() {
        return System.getProperty("os.version");
    }

    public static String getOSArch() {
        return System.getProperty("os.arch");
    }

    public static String[] getMemUsage() {
        return getMemoryUsageStatistics();
    }

    public static String formatNumber(long value) {
        return NumberFormat.getInstance(Locale.ENGLISH).format(value);
    }

    public static String[] getMemoryUsageStatistics() {
        double max = (double) Runtime.getRuntime().maxMemory() / 1024.0D;
        double allocated = (double) Runtime.getRuntime().totalMemory() / 1024.0D;
        double nonAllocated = max - allocated;
        double cached = (double) Runtime.getRuntime().freeMemory() / 1024.0D;
        double used = allocated - cached;
        double useable = max - used;
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
        DecimalFormat df = new DecimalFormat(" (0.0000\'%\')");
        DecimalFormat df2 = new DecimalFormat(" # \'KB\'");
        return new String[]{"+----", "| Global Memory Informations at " + sdf.format(new Date()) + ":", "|    |", "| Allowed Memory:" + df2.format(max), "|    |= Allocated Memory:" + df2.format(allocated) + df.format(allocated / max * 100.0D), "|    |= Non-Allocated Memory:" + df2.format(nonAllocated) + df.format(nonAllocated / max * 100.0D), "| Allocated Memory:" + df2.format(allocated), "|    |= Used Memory:" + df2.format(used) + df.format(used / max * 100.0D), "|    |= Unused (cached) Memory:" + df2.format(cached) + df.format(cached / max * 100.0D), "| Useable Memory:" + df2.format(useable) + df.format(useable / max * 100.0D), "+----"};
    }

    public static long usedMemory() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576L;
    }

    public static void load(Class<?> clazz) {
        try {
            Class.forName(clazz.getName());
        } catch (ClassNotFoundException var2) {
            throw new Error(var2);
        }
    }

    public static String getUptime() {
        long uptimeInSec = (long) Math.ceil((double) ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0D);
        long s = uptimeInSec / 1L % 60L;
        long m = uptimeInSec / 60L % 60L;
        long h = uptimeInSec / 3600L % 24L;
        long d = uptimeInSec / 86400L;
        StringBuilder tb = new StringBuilder();
        if (d > 0L) {
            tb.append(d + " day(s), ");
        }

        if (h > 0L || tb.length() != 0) {
            tb.append(h + " hour(s), ");
        }

        if (m > 0L || tb.length() != 0) {
            tb.append(m + " minute(s), ");
        }

        if (s > 0L || tb.length() != 0) {
            tb.append(s + " second(s)");
        }

        return tb.toString();
    }

    public static String getShortUptime() {
        long uptimeInSec = (long) Math.ceil((double) ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0D);
        long s = uptimeInSec / 1L % 60L;
        long m = uptimeInSec / 60L % 60L;
        long h = uptimeInSec / 3600L % 24L;
        long d = uptimeInSec / 86400L;
        StringBuilder tb = new StringBuilder();
        if (d > 0L) {
            tb.append(d).append("d");
        }

        if (h > 0L || tb.length() != 0) {
            tb.append(h).append("h");
        }

        if (m > 0L || tb.length() != 0) {
            tb.append(m).append("m");
        }

        if (s > 0L || tb.length() != 0) {
            tb.append(s).append("s");
        }

        return tb.toString();
    }

    private static final class HotSpotDiagnosticMXBeanHolder {
        public static final HotSpotDiagnosticMXBean INSTANCE;

        static {
            try {
                MBeanServer e = ManagementFactory.getPlatformMBeanServer();
                String mXBeanName = "com.sun.management:type=HotSpotDiagnostic";
                Class mXBeanInterface = HotSpotDiagnosticMXBean.class;
                INSTANCE = (HotSpotDiagnosticMXBean) ManagementFactory.newPlatformMXBeanProxy(e, "com.sun.management:type=HotSpotDiagnostic", mXBeanInterface);
            } catch (Exception var3) {
                throw new Error(var3);
            }
        }

        private HotSpotDiagnosticMXBeanHolder() {
        }
    }
}

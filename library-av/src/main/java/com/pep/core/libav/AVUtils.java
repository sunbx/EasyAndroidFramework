package com.pep.core.libav;

import java.util.Formatter;
import java.util.Locale;

/**
 * @author sunbaixin QQ:283122529
 * @name pepCore
 * @class name：com.pep.core.libav
 * @class describe
 * @time 2019-09-19 16:59
 * @change
 * @chang time
 * @class describe
 */
public class AVUtils {

    /**
     * string time format
     */
    private static StringBuilder mFormatBuilder = new StringBuilder();

    /**
     * string time format
     */
    private static Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    /**
     * stringToTime. 时间戳转时间
     *
     * @param timeMs times
     */
    public static String stringToTime(long timeMs) {
        long totalSeconds = timeMs / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

}

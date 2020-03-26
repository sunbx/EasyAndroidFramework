package com.pep.core.libcommon;


import java.text.DecimalFormat;

public class EasyUtils {
    public static String getFileSize(long file) {
        String size = "";
        DecimalFormat df = new DecimalFormat("#.0");
        if (file < 1024) {
            size = df.format((double) file) + "B";
        } else if (file < 1048576) {
            size = df.format((double) file / 1024) + "K";
        } else if (file < 1073741824) {
            size = df.format((double) file / 1048576) + "M";
        } else {
            size = df.format((double) file / 1073741824) + "G";
        }
        return size;
    }

    public static String getPublishTime(String publish_time) {
        return publish_time.substring(0,10).replace("-",".");
    }
}

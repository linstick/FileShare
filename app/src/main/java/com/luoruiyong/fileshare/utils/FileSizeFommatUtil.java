package com.luoruiyong.fileshare.utils;

public class FileSizeFommatUtil {

    private static String[] UNITS = {
        "B",
        "KB",
        "MB",
        "GB",
            "TB"

    };

    public static String format(long size) {
        if (size <= 0) {
            return "未知文件大小";
        }
        double tempSize = ((double) size);
        int i = 0;
        for (; i < UNITS.length; i++) {
            if (tempSize < 1024) {
                break;
            }
            if (i + 1 < UNITS.length) {
                tempSize /= 1024;
            }
        }
        return FloatFormatUtil.saveOnePoint(tempSize) + UNITS[i < UNITS.length ? i : (i - 1)];
    }

}

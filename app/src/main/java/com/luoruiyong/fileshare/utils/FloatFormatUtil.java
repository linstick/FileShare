package com.luoruiyong.fileshare.utils;

import java.text.DecimalFormat;

public class FloatFormatUtil {
    private static DecimalFormat df2 = new DecimalFormat("0.00");
    private static DecimalFormat df1 = new DecimalFormat("0.0");

    public static String saveOnePoint(double target) {
        return df1.format(target);
    }

    public static String saveTwoPoint(double target) {
        return df2.format(target);
    }

}

package com.luoruiyong.fileshare.utils;

import java.io.Closeable;

public class SourceUtil {
    public static void close(Closeable source) {
        if (source != null) {
            try {
                source.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

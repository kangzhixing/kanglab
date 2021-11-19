package com.kang.lab.utils;

import com.kang.lab.utils.enums.OSEnum;

public class OSUtil {

    public static OSEnum currentOs;

     static {

        String osName = System.getProperty("os.name");

        if (osName.contains("Windows")) {
            currentOs = OSEnum.WINDOWS;
        } else {
            currentOs = OSEnum.LINUX;
        }
    }
}

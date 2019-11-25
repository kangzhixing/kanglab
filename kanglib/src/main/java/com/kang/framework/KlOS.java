package com.kang.framework;

public class KlOS {

    public static KlOSEnum currentOs;

     static {

        String osName = System.getProperty("os.name");

        if (osName.contains("Windows")) {
            currentOs = KlOSEnum.WINDOWS;
        } else {
            currentOs = KlOSEnum.LINUX;
        }
    }
}

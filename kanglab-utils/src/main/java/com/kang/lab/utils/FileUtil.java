package com.kang.lab.utils;

import java.io.File;

public class FileUtil {

    public static void delete(File f) {
        //获取包含file对象对应的子目录或者文件
        File[] b = f.listFiles();
        for (int i = 0; i < b.length; i++) {
            if (b[i].isFile()) {
                b[i].delete();
            } else {
                delete(b[i]);
            }
        }
        //最后删除该目录中所有文件后就删除该目录
        f.delete();
    }
}

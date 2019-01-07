package com.kang.codetool.common;

import java.util.Comparator;

public class StringCompare implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {
        return str2.compareTo(str1);
    }
}

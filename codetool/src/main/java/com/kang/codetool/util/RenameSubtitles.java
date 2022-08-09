package com.kang.codetool.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RenameSubtitles {

    private static Set<String> videoFileSuffix = new HashSet<>();

    private static Set<String> subtitleFileSuffix = new HashSet<>();

    private static final String SEASON_PATTERN = "(S)[0-9]{1,2}(E)[0-9]{1,2}";

    static {
        videoFileSuffix.add("mkv");
        videoFileSuffix.add("mp4");
        videoFileSuffix.add("avi");

        subtitleFileSuffix.add("srt");
        subtitleFileSuffix.add("ass");
    }


    public static void main(String[] args) {
        String path = RenameSubtitles.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File f = new File(path);
        readPost(f);
    }

    public static void readPost(File dic) {
        Map<String, File> subtitleMap = new HashMap<>();
        Map<String, File> videoMap = new HashMap<>();
        for (File file : dic.listFiles()) {
            String seasonTitle = getSeasonTitle(file);
            if (StringUtils.isBlank(seasonTitle)) {
                continue;
            }
            String fileLastName = getFileLastName(file);
            if (videoFileSuffix.contains(fileLastName)) {
                videoMap.put(seasonTitle, file);
            } else if (subtitleFileSuffix.contains(fileLastName)) {
                subtitleMap.put(seasonTitle, file);
            }
        }
        if (subtitleMap.isEmpty() || videoMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, File> videoEntry : videoMap.entrySet()) {
            File subtitleFile = subtitleMap.get(videoEntry.getKey());
            if (subtitleFile == null) {
                continue;
            }
            String oriName = subtitleFile.getName();
            if (subtitleFile.renameTo(new File(videoEntry.getValue().getName().substring(0, videoEntry.getValue().getName().lastIndexOf('.') + 1) + getFileLastName(subtitleFile)))) {
                System.out.println("file rename success! from " + oriName + " to " + subtitleFile.getName());
            } else {
                System.out.println("file rename failed! from " + oriName + " to " + subtitleFile.getName());
            }
        }
        System.out.println("all done!");


    }

    public static String getFileLastName(File f) {
        if (f.getName().lastIndexOf(".") == -1) {
            return "";
        }
        return f.getName().substring(f.getName().lastIndexOf("."));
    }

    private static String getSeasonTitle(File f) {
        Pattern r = Pattern.compile(SEASON_PATTERN);
        Matcher m = r.matcher(f.getName());
        System.out.println(m.group());
        return m.group();
    }
}

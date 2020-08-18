package com.kang.framework;


import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 转型工具类
 *
 * @author kangzhixing
 */
public class KlConvert {

    public static Object[] toArray(Object obj) {
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            Object[] os = new Object[length];
            for (int i = 0; i < os.length; i++) {
                os[i] = Array.get(obj, i);
            }
        }

        return null;
    }

    public static Long tryToLong(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception ex) {
            return null;
        }
    }

    public static Boolean tryToBoolean(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return Boolean.parseBoolean(obj.toString());
        } catch (Exception ex) {
            return null;
        }
    }

    public static Integer tryToInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception ex) {
            return null;
        }
    }

    public static String[] toStringArray(Object obj) {
        if (obj instanceof List) {
            List objs = (List) obj;
            String[] result = new String[objs.size()];
            for (int i = 0; i < objs.size(); i++) {
                result[i] = tryToString(objs.get(i));
            }
            return result;
        }
        return null;
    }

    public static int forceToInt(Object obj) {
        if (obj == null) {
            return 0;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception ex) {
            return 0;
        }
    }

    public static Double tryToDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception ex) {
            return null;
        }
    }

    public static Float tryToFloat(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return Float.parseFloat(obj.toString());
        } catch (Exception ex) {
            return null;
        }
    }

    public static String tryToString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static Boolean toBoolean(Object obj) {
        return Boolean.parseBoolean(obj.toString());
    }

    public static Long toLong(Object obj) {
        return Long.parseLong(obj.toString());
    }

    public static Integer toInteger(Object obj) {
        return Integer.parseInt(obj.toString());
    }

    public static Double toDouble(Object obj) {
        return Double.parseDouble(obj.toString());
    }

    public static Float toFloat(Object obj) {
        return Float.parseFloat(obj.toString());
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static LocalDateTime dateToLocalDateTime() {
        Date date = new java.util.Date();
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    public static Date localDateToDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date toDate(String date) {
        return toDate(date, "yyyy-MM-dd");
    }

    public static Date toDate(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date);
        } catch (Exception e) {
            return null;
        }
    }
}

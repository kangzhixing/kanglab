package com.kang.lab.plugins.utils.json;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.ValueFilter;
import org.apache.commons.lang3.StringUtils;

public class JsonUtil {

    public static String desensitize(Object... args) {
        return JSON.toJSONString(args, getValueFilter(), JSONWriter.Feature.IgnoreErrorGetter);
    }

    private static ValueFilter getValueFilter() {
        return (Object obj, String key, Object value) -> {
            try {
                SensitiveField annotation;
                if (value instanceof String && (annotation = obj.getClass().getDeclaredField(key).getAnnotation(SensitiveField.class)) != null) {
                    String strVal = (String) value;
                    if (StringUtils.isNotBlank(strVal)) {
                        switch (annotation.value()) {
                            case MOBILE:
                                return DesensitizedUtil.mobilePhone(strVal);
                            case ID_CARD:
                                return DesensitizedUtil.idCardNum(strVal, annotation.front() == -1 ? 3 : annotation.front(), annotation.end() == -1 ? 4 : annotation.end());
                            case BANK_CARD:
                                return DesensitizedUtil.bankCard(strVal);
                            case NAME:
                                return DesensitizedUtil.chineseName(strVal);
                            case PASSWORD:
                                return DesensitizedUtil.password(strVal);
                            case ADDRESS:
                                return DesensitizedUtil.address(strVal, annotation.end() == -1 ? 3 : annotation.end());
                            case CAR_LICENSE:
                                return DesensitizedUtil.carLicense(strVal);
                            case EMAIL:
                                return DesensitizedUtil.email(strVal);
                            case IPV4:
                                return DesensitizedUtil.ipv4(strVal);
                            case IPV6:
                                return DesensitizedUtil.ipv6(strVal);
                            case IGNORE_COUNT:
                                return "*";
                            case CUSTOM:
                                return StrUtil.hide(strVal, annotation.front() == -1 ? 0 : annotation.front(), strVal.length() - (annotation.end() == -1 ? 0 : annotation.end()));
                            default:
                                return value;
                        }
                    }
                }
            } catch (NoSuchFieldException e) {
                return value;
            }
            return value;
        };
    }
}

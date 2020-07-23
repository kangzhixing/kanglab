package com.kang.framework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Json工具类型
 *
 * @author kangzhixing
 */
@Slf4j
public class KlJson {

        /**
         *  创建Gson对象
         */
        private final static Gson STATIC_GSON = new GsonBuilder().create();

        /**
         * 私有无参构造方法
         */
        private KlJson() {
        }

        /**
         * 将对象转成json格式
         *
         * @param object 入参为对象
         * @return String 出参为转换后的字符串
         */
        public static String toJSONString(Object object) {
            try {
                return STATIC_GSON.toJson(object);
            }catch (Exception e) {
                log.error("JSON转换异常：", e);
                return "";
            }
        }

        /**
         * 将json转成特定的cls的对象
         *
         * @param jsonString json串
         * @param cls        class对象
         * @param <T>        对象类型
         * @return 特定对象
         */
        public static <T> T parseObject(String jsonString, Class<T> cls) {
            try {
                return STATIC_GSON.fromJson(jsonString, cls);
            } catch (Exception e) {
                log.error("JSON转换异常：", e);
                return null;
            }
        }
    }
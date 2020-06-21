package com.sample.order.util;

import com.google.gson.Gson;

/**
 * JSON 工具类
 *
 * @author JueYi
 */
public class JsonUtil {

    private static final Gson GSON = new Gson();

    private JsonUtil() {
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        return GSON.fromJson(jsonStr, clazz);
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

}

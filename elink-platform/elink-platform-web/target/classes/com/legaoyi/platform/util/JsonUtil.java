package com.legaoyi.platform.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T convertStr2Obj(String src, Class<T> returnClass) throws Exception {
        return objectMapper.readValue(src, returnClass);
    }

    public static <T> String covertObj2Str(T t) throws Exception {
        return objectMapper.writeValueAsString(t);
    }

    @SuppressWarnings("rawtypes")
    public static Map json2Map(String json) throws Exception {
        return objectMapper.readValue(json, Map.class);
    }

    @SuppressWarnings("rawtypes")
    public static List json2List(String json) throws Exception {
        return objectMapper.readValue(json, List.class);
    }

    public static <T> List<T> json2List(String json, Class<?> clazz) throws Exception {
        TypeFactory t = TypeFactory.defaultInstance();
        // 指定容器结构和类型（这里是ArrayList和clazz）
        return objectMapper.readValue(json, t.constructCollectionType(ArrayList.class, clazz));
    }
}

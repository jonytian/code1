package com.legaoyi.common.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Reflect2Entity {

    private static Date parseDate(String val) {
        String[] arr = val.split(" ");
        try {
            if (arr.length > 1) {
                arr = arr[1].split(":");
                if (arr.length > 2) {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(val);
                } else if (arr.length > 1) {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(val);
                } else {
                    return new SimpleDateFormat("yyyy-MM-dd HH").parse(val);
                }
            } else {
                if (arr[0].indexOf("-") != -1) {
                    return new SimpleDateFormat("yyyy-MM-dd").parse(val);
                } else if (arr[0].indexOf(":") != -1) {
                    return new SimpleDateFormat("HH:mm:ss").parse(val);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static Object reflect(Object t, Map<?, ?> map) throws Exception {
        if (map == null) {
            return t;
        }

        Field[] fields = getClassField(t);
        if (fields == null) {
            return t;
        }

        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            Object val = map.get(field.getName());
            if (val == null || (val instanceof String && "".equals(val))) {
                continue;
            }

            if (type == Long.class || "long".equals(type.getName())) {
                if (val instanceof Long) {
                    field.set(t, val);
                } else if (val instanceof String) {
                    field.set(t, Long.valueOf((String) val));
                } else {
                    field.set(t, Long.valueOf(String.valueOf(val)));
                }
            } else if (type == Integer.class || "int".equals(type.getName())) {
                if (val instanceof Integer) {
                    field.set(t, val);
                } else if (val instanceof String) {
                    field.set(t, Integer.valueOf((String) val));
                } else {
                    field.set(t, Integer.valueOf(String.valueOf(val)));
                }
            } else if (type == String.class && (val instanceof String)) {
                field.set(t, val);
            } else if (type == java.util.Date.class) {
                if (val instanceof Date) {
                    field.set(t, val);
                } else if (val instanceof String) {
                    field.set(t, parseDate((String) val));
                }
            } else if (type == Short.class || "short".equals(type.getName())) {
                if (val instanceof Short) {
                    field.set(t, val);
                } else if (val instanceof String) {
                    field.set(t, Short.valueOf((String) val));
                } else {
                    field.set(t, Short.valueOf(String.valueOf(val)));
                }
            } else if (type == Double.class || "double".equals(type.getName())) {
                if (val instanceof Double) {
                    field.set(t, val);
                } else if (val instanceof String) {
                    field.set(t, Double.valueOf((String) val));
                } else {
                    field.set(t, Double.valueOf(String.valueOf(val)));
                }
            } else if (type == Boolean.class || "boolean".equals(type.getName())) {
                if ("0".equals(String.valueOf(val)) || "false".equalsIgnoreCase(String.valueOf(val))) {
                    field.set(t, false);
                } else {
                    field.set(t, true);
                }
            } else {
                if (val instanceof Map) {
                    Object o = type.newInstance();
                    reflect(o, (Map<?, ?>) val);
                    field.set(t, o);
                }
            }
        }
        return t;
    }

    @SuppressWarnings("rawtypes")
    public static Field[] getClassField(Object t) {
        Field[] fields = t.getClass().getDeclaredFields();
        Class superclass = t.getClass().getSuperclass();
        if (superclass != null) {
            Field[] fields1 = superclass.getDeclaredFields();
            if (fields1 != null) {
                Field[] fields2 = new Field[fields.length + fields1.length];
                System.arraycopy(fields, 0, fields2, 0, fields.length);
                System.arraycopy(fields1, 0, fields2, fields.length, fields1.length);
                return fields2;
            }
        }
        return fields;
    }
}

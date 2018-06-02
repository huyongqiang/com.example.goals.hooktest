package com.example.goals.hooktest;

import java.lang.reflect.Field;

/**
 * Date: 2018/6/2.
 * Description:
 * 由于Hook需要多次对字段进行反射操作，先写一个字段工具类FieldUtil
 *
 * @author huyongqiang
 */

public class FieldUtil {
    public static Object getField(Class clazz, Object target, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    public static Field getField(Class clazz, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    public static void setField(Class clazz, Object target, String name, Object value) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
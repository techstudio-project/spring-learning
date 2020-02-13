package com.techstudio.springlearning.annotation.jvm;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author lj
 * @date 2020/1/21
 */
public class DirectMemory {
    private static final int _1m = 1024 * 1024;

    /**
     * vm args:
     * -Xmx20m
     * -XX:MaxDirectMemorySize=10m 设置本机内存
     * <p>
     * java.lang.OutOfMemoryError
     */
    public static void main(String[] args) throws IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(_1m);
        }
    }

}

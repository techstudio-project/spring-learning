package com.techstudio.springlearning.annotation.jvm;

import com.techstudio.springlearning.annotation.ConsoleUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author lj
 * @date 2020/1/26
 */
public class MethodHandleTest {

    public static void main(String[] args)
            throws Throwable {
        System.out.println("input class type");
        while (true) {
            String command = ConsoleUtils.readLine();
            Object obj;
            if ("1".equals(command)) {
                obj = System.out;
                getMethodHandle(obj).invokeExact("this is System.out.println");
                continue;
            }
            if ("2".equals(command)) {
                obj = new ClassA();
                getMethodHandle(obj).invokeExact("this is ClassA.println");
                continue;
            }
            System.out.println("input error");
        }

    }

    private static MethodHandle getMethodHandle(Object receiver)
            throws NoSuchMethodException, IllegalAccessException {
        MethodType type = MethodType.methodType(void.class, String.class);
        return MethodHandles
                .lookup()
                .findVirtual(receiver.getClass(), "println", type)
                .bindTo(receiver);
    }

    static class ClassA {
        public void println(String s) {
            System.out.println(s);
        }
    }

}

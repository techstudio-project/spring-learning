package com.techstudio.springlearning.annotation.classloader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author lj
 * @date 2020/1/16
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws ClassNotFoundException,
            MalformedURLException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {

        String classLocation1 = "H:\\classloader";
        String classLocation2 = "H:\\classloader\\v1";

        File v1ClassFile = new File(classLocation1);
        File v2ClassFile = new File(classLocation2);


        URLClassLoader v1 = new URLClassLoader(new URL[]{v1ClassFile.toURI().toURL()});
        URLClassLoader v2 = new URLClassLoader(new URL[]{v2ClassFile.toURI().toURL()});

        Class<?> v1Class = v1.loadClass("v1.Dept");
        Object obj1 = v1Class.getConstructor().newInstance();
        v1Class.getMethod("print").invoke(obj1);

        Class<?> v2Class = v1.loadClass("v2.Dept");
        Object obj2 = v2Class.getConstructor().newInstance();
        v2Class.getMethod("print").invoke(obj2);

        // 两个URLClassLoader 即使是加载的同一个class 得到的也是不一样的对象
        System.out.println(obj1.equals(obj2));

    }
}

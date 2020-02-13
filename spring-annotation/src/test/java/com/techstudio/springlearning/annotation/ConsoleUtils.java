package com.techstudio.springlearning.annotation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author lj
 * @date 2020/1/24
 */
public class ConsoleUtils {

    private ConsoleUtils() {
    }

    public static String readLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public static void booleanCommand(String content) throws IOException {
        System.out.println(content + "?");
        String command = readLine();
        while (!"yes".equals(command)) {
            System.out.println("input error");
            command = readLine();
        }
    }
}

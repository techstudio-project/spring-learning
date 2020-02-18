package com.techstudio.springlearning.annotation.http;

/**
 * @author lj
 * @date 2020/2/18
 */
public class HttpClientTest {

    public static void main(String[] args) {
        String s = HttpUtils.get("https://testapi.sesgoplus.com/soam-app/custom_routes", String.class);
        System.out.println(s);
    }

}

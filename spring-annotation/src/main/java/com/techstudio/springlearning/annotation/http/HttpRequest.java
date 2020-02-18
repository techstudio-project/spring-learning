package com.techstudio.springlearning.annotation.http;


/**
 * @author lj
 * @date 2020/2/18
 */
public interface HttpRequest {

    <T> T get(String url, Class<T> responseType) throws HttpException;

    <T> T post(String url, Object data, Class<T> responseType) throws HttpException;

}

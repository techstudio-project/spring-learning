package com.techstudio.springlearning.annotation.http;

/**
 * @author lj
 * @date 2020/2/18
 */
public class HttpUtils {

    private HttpUtils() {
    }

    private static HttpRequest getHttpRequest() {
        // return InnerApacheHttpClientWrapper.APACHE_HTTP_CLIENT_WRAPPER;
        // return InnerHttpURLConnectionWrapper.HTTP_URL_CONNECTION_WRAPPER;
        return InnerSpringRestTemplateWrapper.SPRING_REST_TEMPLATE_WRAPPER;
    }

    public static <T> T get(String url, Class<T> responseType) {
        return getHttpRequest().get(url, responseType);
    }

    public static <T> T post(String url, Object data, Class<T> responseType) {
        return getHttpRequest().post(url, data, responseType);
    }

    private static class InnerApacheHttpClientWrapper {
        private static final ApacheHttpClientWrapper APACHE_HTTP_CLIENT_WRAPPER = new ApacheHttpClientWrapper();
    }

    private static class InnerHttpURLConnectionWrapper {
        private static final HttpURLConnectionWrapper HTTP_URL_CONNECTION_WRAPPER = new HttpURLConnectionWrapper();
    }

    private static class InnerSpringRestTemplateWrapper {
        private static final SpringRestTemplateWrapper SPRING_REST_TEMPLATE_WRAPPER = new SpringRestTemplateWrapper();
    }

}

package com.techstudio.springlearning.annotation.http;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * @author lj
 * @date 2020/2/18
 */
public class SpringRestTemplateWrapper implements HttpRequest {

    private final RestTemplate restTemplate;

    public SpringRestTemplateWrapper() {
        this.restTemplate = getDefaultRestTemplate();
    }

    @Override
    public <T> T get(String url, Class<T> responseType) throws HttpException {
        return restTemplate.getForEntity(url, responseType).getBody();
    }

    @Override
    public <T> T post(String url, Object data, Class<T> responseType) throws HttpException {
        return restTemplate.postForEntity(url, data, responseType).getBody();
    }

    private RestTemplate getDefaultRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        restTemplate.setMessageConverters(
                Collections.singletonList(new StringHttpMessageConverter(StandardCharsets.UTF_8)));

        return restTemplate;
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        ApacheHttpClientWrapper apacheHttpClientWrapper = new ApacheHttpClientWrapper();
        return new HttpComponentsClientHttpRequestFactory(apacheHttpClientWrapper.getHttpClient());
    }

}

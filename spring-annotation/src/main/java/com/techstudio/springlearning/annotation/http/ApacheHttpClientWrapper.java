package com.techstudio.springlearning.annotation.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author lj
 * @date 2020/2/18
 */
public class ApacheHttpClientWrapper implements HttpRequest {

    private final CloseableHttpClient httpClient;

    public ApacheHttpClientWrapper() {
        this.httpClient = getDefaultHttpClientBuilder().build();
    }

    @Override
    public <T> T get(String url, Class<T> responseType) throws HttpException {
        HttpGet httpGet = new HttpGet(url);
        try {
            return httpClient.execute(httpGet, getDefaultResponseHandler(responseType));
        }
        catch (IOException e) {
            throw new HttpException(e);
        }
    }

    @Override
    public <T> T post(String url, Object data, Class<T> responseType) throws HttpException {
        HttpPost httpPost = new HttpPost(url);
        HttpEntity httpEntity = new StringEntity(JSON.toJSONString(data), StandardCharsets.UTF_8);
        httpPost.setEntity(httpEntity);
        try {
            return httpClient.execute(httpPost, getDefaultResponseHandler(responseType));
        }
        catch (IOException e) {
            throw new HttpException(e);
        }
    }


    private HttpClientBuilder getDefaultHttpClientBuilder() {
        return HttpClientBuilder.create()
                .setSSLContext(getSSLContext())
                .setSSLHostnameVerifier((s, p) -> true)
                .setConnectionManager(getConnectionManager());
    }

    private SSLContext getSSLContext() {
        try {
            return SSLUtils.getTrustAllSSLContext();
        }
        catch (Exception ignore) {
        }
        return SSLContexts.createDefault();
    }

    private HttpClientConnectionManager getConnectionManager() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(getDefaultRegistry());
        cm.setMaxTotal(500);
        cm.setDefaultMaxPerRoute(50);
        return cm;
    }

    private Registry<ConnectionSocketFactory> getDefaultRegistry() {
        return RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", getTrustAllSSLConnectionSocketFactory())
                .build();
    }

    private SSLConnectionSocketFactory getTrustAllSSLConnectionSocketFactory() {
        return new SSLConnectionSocketFactory(getSSLContext(), (s, sslSession) -> true);
    }

    private <T> ResponseHandler<T> getDefaultResponseHandler(Class<T> responseType) {
        return response -> {
            String str = StreamUtils.streamToString(response.getEntity().getContent());
            return JSON.parseObject(str, responseType);
        };
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }
}

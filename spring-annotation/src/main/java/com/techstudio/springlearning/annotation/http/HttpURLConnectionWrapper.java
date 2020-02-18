package com.techstudio.springlearning.annotation.http;

import com.alibaba.fastjson.JSON;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.techstudio.springlearning.annotation.http.SSLUtils.getTrustAllSSLSocketFactory;
import static com.techstudio.springlearning.annotation.http.StreamUtils.streamToString;

/**
 * @author lj
 * @date 2020/2/18
 */
public class HttpURLConnectionWrapper implements HttpRequest {

    @Override
    public <T> T get(String url, Class<T> responseType) {
        try {
            URL urlObj = new URL(url);
            String protocol = urlObj.getProtocol();
            String res;
            if ("https".equals(protocol)) {
                res = getWithSSL(urlObj);
            }
            else {
                res = get(urlObj);
            }
            return JSON.parseObject(res, responseType);
        }
        catch (Exception e) {
            throw new HttpException(e);
        }
    }

    private String get(URL url) throws IOException {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");

            conn.connect();
            InputStream is = conn.getInputStream();
            return streamToString(is);
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private String getWithSSL(URL url) throws IOException {
        HttpsURLConnection sslConn = null;
        try {
            sslConn = (HttpsURLConnection) url.openConnection();
            sslConn.setConnectTimeout(10000);
            sslConn.setReadTimeout(10000);

            sslConn.setRequestProperty("Content-Type", "application/json");
            sslConn.setRequestProperty("Connection", "Keep-Alive");
            sslConn.setRequestProperty("Charset", "UTF-8");

            SSLSocketFactory trustAllFactory = getTrustAllSSLSocketFactory();
            if (trustAllFactory != null) {
                sslConn.setSSLSocketFactory(trustAllFactory);
            }
            sslConn.setHostnameVerifier((s, sslSession) -> true);

            sslConn.connect();
            InputStream is = sslConn.getInputStream();
            return streamToString(is);
        }
        finally {
            if (sslConn != null) {
                sslConn.disconnect();
            }
        }
    }

    @Override
    public <T> T post(String url, Object data, Class<T> responseType) {
        return null;
    }
}

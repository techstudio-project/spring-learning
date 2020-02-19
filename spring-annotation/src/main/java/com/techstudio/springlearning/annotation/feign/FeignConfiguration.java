package com.techstudio.springlearning.annotation.feign;

import com.techstudio.springlearning.annotation.http.SSLUtils;
import feign.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lj
 * @date 2020/2/18
 */
@Configuration
public class FeignConfiguration {

    /**
     * 这里还可以配置其它第三方的httpClient
     *
     * @return Client
     * @see org.springframework.cloud.openfeign.FeignAutoConfiguration
     */
    @Bean
    public Client trustAllSSLHttpClient() {
        return new Client.Default(SSLUtils.getTrustAllSSLSocketFactory(),
                SSLUtils.getNoopHostnameVerifier());
    }

}

package com.hellozai.weather.checker.config.feign;

import com.hellozai.weather.checker.feign.interceptors.ApiKeyRequestParamInterceptor;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherstackClientConfig {

    @Value("${WEATHERSTACK_ACCESS_KEY_NAME:access_key}")
    private String accessKeyName;

    @Value("${WEATHERSTACK_ACCESS_KEY_VALUE}")
    private String accessKeyValue;

    @Bean
    public RequestInterceptor accessKeyRequestInterceptor() {
        return new ApiKeyRequestParamInterceptor(accessKeyName, accessKeyValue);
    }
}

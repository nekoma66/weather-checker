package com.hellozai.weather.checker.config.feign;

import com.hellozai.weather.checker.feign.interceptors.ApiKeyRequestParamInterceptor;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class OpenWeatherMapClientConfig {

    @Value("${OPENWEATHERMAP_ACCESS_KEY_NAME:appid}")
    private String accessKeyName;

    @Value("${OPENWEATHERMAP_ACCESS_KEY_VALUE}")
    private String accessKeyValue;

    @Bean
    public RequestInterceptor accessKeyRequestInterceptor() {
        return new ApiKeyRequestParamInterceptor(accessKeyName, accessKeyValue);
    }
}

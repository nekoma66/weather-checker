package com.hellozai.weather.checker.feign.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ApiKeyRequestParamInterceptor implements RequestInterceptor {

    private final String name;
    private final String key;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("Injecting key to request.");

        requestTemplate.query(name, key);
    }
}

package com.hellozai.weather.checker.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableCaching
@EnableScheduling
public class CachingConfig {

    @CacheEvict(value = "weather", allEntries = true)
    @Scheduled(fixedRateString = "${spring.caching.weather.ttl}")
    public void emptyWeatherCache() {
        log.info("Emptying weather cache");
    }
}

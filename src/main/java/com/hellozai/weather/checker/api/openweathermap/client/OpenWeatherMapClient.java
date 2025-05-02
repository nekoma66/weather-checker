package com.hellozai.weather.checker.api.openweathermap.client;

import com.hellozai.weather.checker.api.openweathermap.model.ApiResponse;
import com.hellozai.weather.checker.config.feign.OpenWeatherMapClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "openweathermap", configuration = OpenWeatherMapClientConfig.class)
public interface OpenWeatherMapClient {


    @GetMapping(path = "/weather")
    public ResponseEntity<ApiResponse> getCurrentWeather(@RequestParam String q, @RequestParam(defaultValue = "metric") String units);
}

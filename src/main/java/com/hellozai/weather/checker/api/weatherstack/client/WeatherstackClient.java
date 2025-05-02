package com.hellozai.weather.checker.api.weatherstack.client;

import com.hellozai.weather.checker.api.weatherstack.model.ApiResponse;
import com.hellozai.weather.checker.config.feign.WeatherstackClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "weatherstack", configuration = WeatherstackClientConfig.class)
public interface WeatherstackClient {

    @GetMapping(path = "/current")
    public ResponseEntity<ApiResponse> getCurrentWeather(@RequestParam String query, @RequestParam(defaultValue = "m") String units);
}

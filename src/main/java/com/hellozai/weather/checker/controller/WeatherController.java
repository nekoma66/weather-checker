package com.hellozai.weather.checker.controller;

import com.hellozai.weather.checker.constants.ApiVersion;
import com.hellozai.weather.checker.model.CityWeather;
import com.hellozai.weather.checker.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiVersion.V1)
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping(path = "/weather", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CityWeather> getCityWeather(@RequestParam String city) {
        return ok(weatherService.getCityWeather(city));
    }
}

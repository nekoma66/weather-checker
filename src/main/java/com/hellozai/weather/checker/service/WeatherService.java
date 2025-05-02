package com.hellozai.weather.checker.service;

import com.hellozai.weather.checker.api.openweathermap.client.OpenWeatherMapClient;
import com.hellozai.weather.checker.api.openweathermap.model.Main;
import com.hellozai.weather.checker.api.openweathermap.model.Wind;
import com.hellozai.weather.checker.api.weatherstack.client.WeatherstackClient;
import com.hellozai.weather.checker.api.weatherstack.model.ApiResponse;
import com.hellozai.weather.checker.api.weatherstack.model.Weather;
import com.hellozai.weather.checker.model.CityWeather;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherstackClient weatherstackClient;
    private final OpenWeatherMapClient openWeatherMapClient;

    @Cacheable("weather")
    public CityWeather getCityWeather(String city) {
        log.info("Invoking primary weather info provider.");

        var response = weatherstackClient.getCurrentWeather(city, "m");
        var apiResponse = Optional.ofNullable(response.getBody());
        var errorNode = apiResponse.map(ApiResponse::getError);

        if (errorNode.isPresent()) {
            log.warn("Error in calling primary weather info provider: {}", errorNode);
            log.warn("Switching to failover provider.");
            return failOverWeatherProvider(city);
        }

        var weather = Optional.ofNullable(response.getBody())
                .map(ApiResponse::getCurrent);

        return new CityWeather(
                weather.map(Weather::getWindSpeed)
                        .orElseThrow(() -> new RuntimeException("Invalid wind speed from api.")),
                weather.map(Weather::getTemperature)
                        .orElseThrow(() -> new RuntimeException("Invalid temperature from api.")));
    }

    private CityWeather failOverWeatherProvider(String city) {
        log.info("Invoking failover weather info provider.");

        var response = openWeatherMapClient.getCurrentWeather(city, "metric");
        var statusCode = response.getStatusCode();

        if (statusCode.is4xxClientError()) {
            log.warn("Failover weather client error: {}", statusCode);
            throw new HttpClientErrorException(statusCode);
        }

        if (statusCode.is5xxServerError()) {
            log.warn("Failover weather server error: {}", statusCode);
            throw new HttpServerErrorException(statusCode);
        }

        var weather = Optional.ofNullable(response.getBody());
        return new CityWeather(
                weather.map(com.hellozai.weather.checker.api.openweathermap.model.ApiResponse::getWind)
                        .map(Wind::getSpeed)
                        .orElseThrow(() -> new RuntimeException("Invalid wind speed from api.")),
                weather.map(com.hellozai.weather.checker.api.openweathermap.model.ApiResponse::getMain)
                        .map(Main::getTemp)
                        .orElseThrow(() -> new RuntimeException("Invalid temperature from api.")));
    }
}

package com.hellozai.weather.checker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellozai.weather.checker.api.openweathermap.client.OpenWeatherMapClient;
import com.hellozai.weather.checker.api.openweathermap.model.Main;
import com.hellozai.weather.checker.api.openweathermap.model.Wind;
import com.hellozai.weather.checker.api.weatherstack.client.WeatherstackClient;
import com.hellozai.weather.checker.api.weatherstack.model.ApiResponse;
import com.hellozai.weather.checker.api.weatherstack.model.Weather;
import com.hellozai.weather.checker.model.CityWeather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private OpenWeatherMapClient mockOpenWeatherMapClient;

    @Mock
    private WeatherstackClient mockWeatherstackClient;

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService(mockWeatherstackClient, mockOpenWeatherMapClient);
    }

    @Test
    void with_primary_weather_provider_ok_should_return_correctly_mapped_weather_data() {
        var city = "Melbourne";

        when(mockWeatherstackClient.getCurrentWeather(city, "m"))
                .thenReturn(ResponseEntity.ok(okWeatherstackResponse()));

        var weather = weatherService.getCityWeather(city);

        assertThat(weather)
                .extracting(CityWeather::windSpeed, CityWeather::temperatureDegrees)
                .containsExactly("56", "13");

        verify(mockOpenWeatherMapClient, never()).getCurrentWeather(anyString(), anyString());
    }

    @Test
    void with_primary_weather_provider_nok_should_call_failover_service_and_return_correctly_mapped_weather_data() throws JsonProcessingException {
        var city = "Melbourne";

        when(mockWeatherstackClient.getCurrentWeather(city, "m"))
                .thenReturn(ResponseEntity.ok(badWeatherstackResponse()));

        when(mockOpenWeatherMapClient.getCurrentWeather(city, "metric"))
                .thenReturn(ResponseEntity.ok(okOpenWeatherMapResponse()));

        var weather = weatherService.getCityWeather(city);

        assertThat(weather)
                .extracting(CityWeather::windSpeed, CityWeather::temperatureDegrees)
                .containsExactly("78", "123");
    }

    ApiResponse okWeatherstackResponse() {
        var weather = new Weather();
        weather.setTemperature("13");
        weather.setWindSpeed("56");

        var apiResponse = new ApiResponse();
        apiResponse.setCurrent(weather);

        return apiResponse;
    }

    ApiResponse badWeatherstackResponse() throws JsonProcessingException {
        var error = new ObjectMapper().readTree(
                """
                        {
                                "code": 104,
                                "type": "usage_limit_reached",
                                "info": "Your monthly API request volume has been reached. Please upgrade your plan."   \s
                        }"""
        );

        var apiResponse = new ApiResponse();
        apiResponse.setError(error);

        return apiResponse;
    }

    com.hellozai.weather.checker.api.openweathermap.model.ApiResponse okOpenWeatherMapResponse() {
        var wind = new Wind();
        wind.setSpeed("78");

        var weather = new Main();
        weather.setTemp("123");

        var apiResponse = new com.hellozai.weather.checker.api.openweathermap.model.ApiResponse();
        apiResponse.setMain(weather);
        apiResponse.setWind(wind);

        return apiResponse;
    }
}

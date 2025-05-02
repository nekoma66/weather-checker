package com.hellozai.weather.checker.controller;

import com.hellozai.weather.checker.ZaiWeatherCheckerApplicationTests;
import com.hellozai.weather.checker.model.CityWeather;
import com.hellozai.weather.checker.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ZaiWeatherCheckerApplicationTests
@ExtendWith(SpringExtension.class)
public class WeatherControllerTest {

    @MockitoBean
    private WeatherService mockWeatherService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void invoke_get_current_weather_api_must_return_correct_serialized_data() throws Exception {
        when(mockWeatherService.getCityWeather(anyString()))
                .thenReturn(new CityWeather("16", "45"));

        mockMvc.perform(get("/v1/weather").queryParam("city", "manila"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wind_speed").value("16"))
                .andExpect(jsonPath("$.temperature_degrees").value("45"));
    }
}

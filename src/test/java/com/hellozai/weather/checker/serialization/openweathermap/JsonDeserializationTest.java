package com.hellozai.weather.checker.serialization.openweathermap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellozai.weather.checker.api.openweathermap.model.ApiResponse;
import com.hellozai.weather.checker.api.openweathermap.model.Main;
import com.hellozai.weather.checker.api.openweathermap.model.Wind;
import com.hellozai.weather.checker.api.weatherstack.model.Weather;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonDeserializationTest {

    private static final String SAMPLE_WEATHER_API_JSON = """
            {
              "coord": {
                "lon": -3.7026,
                "lat": 40.4165
              },
              "weather": [
                {
                  "id": 803,
                  "main": "Clouds",
                  "description": "broken clouds",
                  "icon": "04d"
                }
              ],
              "base": "stations",
              "main": {
                "temp": 11.05,
                "feels_like": 10.64,
                "temp_min": 10.14,
                "temp_max": 12.59,
                "pressure": 1012,
                "humidity": 93,
                "sea_level": 1012,
                "grnd_level": 937
              },
              "visibility": 3500,
              "wind": {
                "speed": 5.36,
                "deg": 265,
                "gust": 10.28
              },
              "clouds": {
                "all": 75
              },
              "dt": 1746168071,
              "sys": {
                "type": 2,
                "id": 2084029,
                "country": "ES",
                "sunrise": 1746162758,
                "sunset": 1746213046
              },
              "timezone": 7200,
              "id": 3117735,
              "name": "Madrid",
              "cod": 200
            }""";

    private static final ObjectMapper TEST_MAPPER = new ObjectMapper();

    @Test
    void weather_api_model_relevant_fields_must_be_mapped_correctly() throws JsonProcessingException {
        var deserializedObject = TEST_MAPPER.readValue(SAMPLE_WEATHER_API_JSON, ApiResponse.class);

        assertThat(deserializedObject)
                .extracting(ApiResponse::getMain)
                .extracting(Main::getTemp)
                .isEqualTo("11.05");

        assertThat(deserializedObject)
                .extracting(ApiResponse::getWind)
                .extracting(Wind::getSpeed)
                .isEqualTo("5.36");
    }
}

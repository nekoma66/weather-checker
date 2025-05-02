package com.hellozai.weather.checker.serialization.weatherstack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellozai.weather.checker.api.weatherstack.model.ApiResponse;
import com.hellozai.weather.checker.api.weatherstack.model.Weather;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JsonDeserializationTest {

    private static final String SAMPLE_WEATHER_API_JSON = """
              {
              "request": {
                "type": "City",
                "query": "Madrid, Spain",
                "language": "en",
                "unit": "m"
              },
              "location": {
                "name": "Madrid",
                "country": "Spain",
                "region": "Madrid",
                "lat": "40.400",
                "lon": "-3.683",
                "timezone_id": "Europe/Madrid",
                "localtime": "2025-05-02 08:19",
                "localtime_epoch": 1746173940,
                "utc_offset": "2.0"
              },
              "current": {
                "observation_time": "06:19 AM",
                "temperature": 35,
                "weather_code": 356,
                "weather_icons": [
                  "https://cdn.worldweatheronline.com/images/wsymbols01_png_64/wsymbol_0010_heavy_rain_showers.png"
                ],
                "weather_descriptions": [
                  "Rain Shower, Thunderstorm In Vicinity"
                ],
                "astro": {
                  "sunrise": "07:13 AM",
                  "sunset": "09:12 PM",
                  "moonrise": "10:56 AM",
                  "moonset": "02:06 AM",
                  "moon_phase": "Waxing Crescent",
                  "moon_illumination": 24
                },
                "air_quality": {
                  "co": "262.7",
                  "no2": "36.445",
                  "o3": "51",
                  "so2": "6.845",
                  "pm2_5": "21.645",
                  "pm10": "32.745",
                  "us-epa-index": "2",
                  "gb-defra-index": "2"
                },
                "wind_speed": 13,
                "wind_degree": 95,
                "wind_dir": "E",
                "pressure": 1014,
                "precip": 0.2,
                "humidity": 94,
                "cloudcover": 75,
                "feelslike": 11,
                "uv_index": 0,
                "visibility": 4,
                "is_day": "yes"
              }
            }""";

    private static final ObjectMapper TEST_MAPPER = new ObjectMapper();

    @Test
    void weather_api_model_relevant_fields_must_be_mapped_correctly() throws JsonProcessingException {
        var deserializedObject = TEST_MAPPER.readValue(SAMPLE_WEATHER_API_JSON, ApiResponse.class);

        assertThat(deserializedObject)
                .extracting(ApiResponse::getCurrent)
                .extracting(Weather::getWindSpeed, Weather::getTemperature)
                .containsExactly("13", "35");
    }
}

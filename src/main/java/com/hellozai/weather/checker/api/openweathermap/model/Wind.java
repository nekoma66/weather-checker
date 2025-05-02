package com.hellozai.weather.checker.api.openweathermap.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Wind {

    private String speed;
    private Map<String, JsonNode> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, JsonNode> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, JsonNode value) {
        this.additionalProperties.put(name, value);
    }
}

package com.nasif.jounalApp.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherResponse{
    private Current current;

    @Getter
    @Setter
    public class Current {

        private int temperature;

        @JsonProperty("weather_descriptions")
        private List<String> weatherDescriptions;

        private int pressure;
        private double precip;
        private int humidity;
        private int feelslike;
    }
}
package com.nasif.jounalApp.service;

import com.nasif.jounalApp.api.response.WeatherResponse;
import com.nasif.jounalApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;

//    private static final String api = "http://api.weatherstack.com/current?access_key=<api_key>&query=<city>";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city) {
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if(weatherResponse != null){
            System.out.println("FROM REDIS CACHE");
            return weatherResponse;
        }
        else{
            System.out.println("from api call");
            String finalApi = appCache.cache.get("weather_api").replace("<city>", city).replace("<api_key>", apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalApi, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if(body != null){
                redisService.set("weather_of_" + city, body, 300);
            }
            return body;
        }
    }

}

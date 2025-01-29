package com.challenge.backend.service;

import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.challenge.backend.utils.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class OpenWeatherService {

    //Celsius
    private static final String UNITS = "metric";
    private static final String OW_RESPONSE_MAIN_SEC = "main";
    private static final String OW_RESPONSE_MAIN_TEMP_FIELD = "temp";

    @Value("${open.weather.url.coords}")
    private String openWeatherUrlCoords;

    @Value("${open.weather.url.city}")
    private String openWeatherUrlCity;

    @Value("${OW_APPID}")
    private String appid;

    @Autowired
    private RestTemplate restTemplate;

    public double getWeather(WeatherTrackListRequestDto request) throws JsonProcessingException {
        RequestValidator.validateTracksByWeather(request);

        if (StringUtils.isNotBlank(request.getCity())) {
            return getWeatherByCity(request.getCity());
        }

        return getWeatherByCoords(request.getLat(), request.getLon());
    }

    private double getWeatherByCoords(Double lat, Double lon) throws JsonProcessingException {

        log.info("Getting weather by coordinates lat: {} lon: {}", lat, lon);

        JsonNode body = getAllInfoWeatherByCoords(lat, lon);
        JsonNode main2 = body.get(OW_RESPONSE_MAIN_SEC).get(OW_RESPONSE_MAIN_TEMP_FIELD);
        return main2.asDouble();
    }

    private double getWeatherByCity(String cityName) throws JsonProcessingException {

        log.info("Getting weather by city name: {}", cityName);

        JsonNode body = getAllInfoWeatherByCity(cityName);
        JsonNode main2 = body.get(OW_RESPONSE_MAIN_SEC).get(OW_RESPONSE_MAIN_TEMP_FIELD);
        return main2.asDouble();
    }

    private JsonNode getAllInfoWeatherByCity(String cityName) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(openWeatherUrlCity, String.class, cityName, appid, UNITS);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.getBody());
    }

    private JsonNode getAllInfoWeatherByCoords(Double lat, Double lon) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(openWeatherUrlCoords, String.class, lat, lon, appid, UNITS);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.getBody());
    }
}

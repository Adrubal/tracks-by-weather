package com.challenge.backend.service;

import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class OpenWeatherServiceTest {

    @InjectMocks
    private OpenWeatherService openWeatherService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void getWeatherByCityTest() throws JsonProcessingException {

        ReflectionTestUtils.setField(openWeatherService, "openWeatherUrlCity", "https://api.openweathermap.org/data/2.5/weather?q={cityName}&appid={appid}&units={units}");
        ReflectionTestUtils.setField(openWeatherService, "appid", "123");

        Mockito.when(restTemplate
                        .getForEntity(ArgumentMatchers.anyString(),
                                ArgumentMatchers.any(),
                                ArgumentMatchers.anyString(),
                                ArgumentMatchers.anyString(),
                                ArgumentMatchers.anyString()))
                .thenReturn(new ResponseEntity<>(" {\"main\":{\"temp\": 15.0}}", HttpStatus.OK));

        WeatherTrackListRequestDto request = new WeatherTrackListRequestDto();
        request.setCity("Ottawa");

        double weather = openWeatherService.getWeather(request);
        Assertions.assertEquals(15, weather);
    }

    @Test
    public void getWeatherByCoordsTest() throws JsonProcessingException {

        ReflectionTestUtils.setField(openWeatherService, "openWeatherUrlCoords", "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={appid}&units={units}");
        ReflectionTestUtils.setField(openWeatherService, "appid", "123");

        Mockito.when(restTemplate
                        .getForEntity(ArgumentMatchers.anyString(),
                                ArgumentMatchers.any(),
                                ArgumentMatchers.anyDouble(),
                                ArgumentMatchers.anyDouble(),
                                ArgumentMatchers.anyString(),
                                ArgumentMatchers.anyString()))
                .thenReturn(new ResponseEntity<>(" {\"main\":{\"temp\": 15.0}}", HttpStatus.OK));

        WeatherTrackListRequestDto request = new WeatherTrackListRequestDto();
        request.setLat(20.7167);
        request.setLon(-103.4);

        double weather = openWeatherService.getWeather(request);
        Assertions.assertEquals(15, weather);
    }
}

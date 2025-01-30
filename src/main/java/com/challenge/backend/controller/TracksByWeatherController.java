package com.challenge.backend.controller;

import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.challenge.backend.model.dto.WeatherTrackListResponseDto;
import com.challenge.backend.service.TracksByWeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@EnableWebMvc
public class TracksByWeatherController {

    @Autowired
    private TracksByWeatherService tracksByWeatherService;

    @GetMapping(path = "/track-list", headers = "Accept=application/json")
    public WeatherTrackListResponseDto getTrackList(@RequestParam(required = false) String city,
                                                    @RequestParam(required = false) Double lat,
                                                    @RequestParam(required = false) Double lon) throws JsonProcessingException {
        WeatherTrackListRequestDto request = new WeatherTrackListRequestDto();
        request.setCity(city);
        request.setLat(lat);
        request.setLon(lon);
        return tracksByWeatherService.getTrackList(request);
    }
}

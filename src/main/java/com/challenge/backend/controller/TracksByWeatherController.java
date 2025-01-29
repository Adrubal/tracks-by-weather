package com.challenge.backend.controller;

import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.challenge.backend.model.dto.WeatherTrackListResponseDto;
import com.challenge.backend.service.TracksByWeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@EnableWebMvc
public class TracksByWeatherController {

    @Autowired
    private TracksByWeatherService tracksByWeatherService;

    @PostMapping(path = "/track-list", headers = "Accept=application/json")
    public WeatherTrackListResponseDto getTrackList(@RequestBody WeatherTrackListRequestDto request) throws JsonProcessingException {
        return tracksByWeatherService.getTrackList(request);
    }
}

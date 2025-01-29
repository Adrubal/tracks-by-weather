package com.challenge.backend.controller;

import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.challenge.backend.model.dto.WeatherTrackListResponseDto;
import com.challenge.backend.service.TracksByWeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@EnableWebMvc
public class TracksByWeatherController {

    @Autowired
    private TracksByWeatherService tracksByWeatherService;

    @RequestMapping(path = "/track-list", method = RequestMethod.POST,
            headers = "Accept=application/json")
    public WeatherTrackListResponseDto getTrackList(@RequestBody WeatherTrackListRequestDto request) throws JsonProcessingException {
        return tracksByWeatherService.getTrackList(request);
    }
}

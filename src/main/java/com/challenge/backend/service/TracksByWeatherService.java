package com.challenge.backend.service;

import com.challenge.backend.model.dto.TrackListResponseDto;
import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.challenge.backend.model.dto.WeatherTrackListResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TracksByWeatherService {

    @Autowired
    private OpenWeatherService openWeatherService;

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private AnalyticsService analyticsService;

    public WeatherTrackListResponseDto getTrackList(WeatherTrackListRequestDto request) throws JsonProcessingException {

        log.info("Getting track list - {}", request);
        double currentWeather = openWeatherService.getWeather(request);
        TrackListResponseDto trackListByWeather = spotifyService.getTrackListByWeather(currentWeather);
        analyticsService.saveAnalytics(request, currentWeather, trackListByWeather);

        WeatherTrackListResponseDto response = new WeatherTrackListResponseDto();
        response.setCurrentWeather(currentWeather);
        response.setGenre(trackListByWeather.getGenre());
        response.setTracks(trackListByWeather.getTrackList());

        return response;
    }
}

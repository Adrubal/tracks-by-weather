package com.challenge.backend.service;

import com.challenge.backend.model.dto.TrackListResponseDto;
import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.challenge.backend.model.entity.TracksByWeatherStatistics;
import com.challenge.backend.repository.TracksByWeatherStatisticsRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalyticsService {

    @Autowired
    private TracksByWeatherStatisticsRepository tracksByWeatherStatisticsRepository;

    public void saveAnalytics(WeatherTrackListRequestDto request, Double currentWeather, TrackListResponseDto trackListByWeather) {

        log.info("Saving analytics");

        Gson gson = new Gson();
        String trackListByWeatherJson = gson.toJson(trackListByWeather.getTrackList());

        TracksByWeatherStatistics entity = new TracksByWeatherStatistics();
        entity.setWeather(currentWeather);
        entity.setCity(request.getCity());
        entity.setLat(request.getLat());
        entity.setLon(request.getLon());
        entity.setTracks(trackListByWeatherJson);
        entity.setGenre(trackListByWeather.getGenre());

        tracksByWeatherStatisticsRepository.save(entity);
    }
}

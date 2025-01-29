package com.challenge.backend.service;

import com.challenge.backend.model.dto.TrackDto;
import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.challenge.backend.model.entity.TracksByWeatherStatistics;
import com.challenge.backend.repository.TracksByWeatherStatisticsRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AnalyticsService {

    @Autowired
    private TracksByWeatherStatisticsRepository tracksByWeatherStatisticsRepository;

    public void saveAnalytics(WeatherTrackListRequestDto request, Double currentWeather, List<TrackDto> trackListByWeather) {

        log.info("Saving analytics");

        Gson gson = new Gson();
        String trackListByWeatherJson = gson.toJson(trackListByWeather);

        TracksByWeatherStatistics entity = new TracksByWeatherStatistics();
        entity.setWeather(currentWeather);
        entity.setCity(request.getCity());
        entity.setLat(request.getLat());
        entity.setLon(request.getLon());
        entity.setTracks(trackListByWeatherJson);

        tracksByWeatherStatisticsRepository.save(entity);
    }
}

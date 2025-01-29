package com.challenge.backend.service;

import com.challenge.backend.model.dto.TrackDto;
import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.challenge.backend.model.entity.TracksByWeatherStatistics;
import com.challenge.backend.repository.TracksByWeatherStatisticsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
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

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @InjectMocks
    private AnalyticsService analyticsService;

    @Mock
    private TracksByWeatherStatisticsRepository tracksByWeatherStatisticsRepository;

    @Test
    public void getWeatherByCityTest() throws JsonProcessingException {

        Gson gson = new Gson();

        List<TrackDto> trackList = new ArrayList<>();
        TrackDto track = new TrackDto();
        track.setName("Espresso");
        track.setUrl("https://open.spotify.com/track/2HRqTpkrJO5ggZyyK6NPWz");
        trackList.add(track);

        TracksByWeatherStatistics savedEntity = new TracksByWeatherStatistics();
        savedEntity.setWeather(15D);
        savedEntity.setCity("Ottawa");
        savedEntity.setLat(20.7167);
        savedEntity.setLon(-103.4);
        savedEntity.setTracks(gson.toJson(trackList));

        Mockito.when(tracksByWeatherStatisticsRepository.save(ArgumentMatchers.any(TracksByWeatherStatistics.class)))
                .thenReturn(savedEntity);

        WeatherTrackListRequestDto request = new WeatherTrackListRequestDto();
        request.setCity("Ottawa");


        analyticsService.saveAnalytics(request, 15D, trackList);

        Mockito.verify(tracksByWeatherStatisticsRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }
}

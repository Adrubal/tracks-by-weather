package com.challenge.backend.service;

import com.challenge.backend.model.dto.TrackDto;
import com.challenge.backend.model.dto.TrackListResponseDto;
import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.challenge.backend.model.dto.WeatherTrackListResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TracksByWeatherServiceTest {

    @InjectMocks
    private TracksByWeatherService tracksByWeatherService;
    @Mock
    private OpenWeatherService openWeatherService;
    @Mock
    private SpotifyService spotifyService;
    @Mock
    private AnalyticsService analyticsService;

    @Test
    void getTrackListTest() throws JsonProcessingException {

        TrackListResponseDto trackListByWeather = new TrackListResponseDto();
        trackListByWeather.setGenre("rock");

        List<TrackDto> trackList = new ArrayList<>();
        TrackDto track = new TrackDto();
        track.setName("Espresso");
        track.setUrl("https://open.spotify.com/track/2HRqTpkrJO5ggZyyK6NPWz");
        trackList.add(track);
        trackListByWeather.setTrackList(trackList);

        BDDMockito.given(openWeatherService.getWeather(ArgumentMatchers.any(WeatherTrackListRequestDto.class))).willReturn(15D);
        BDDMockito.given(spotifyService.getTrackListByWeather(ArgumentMatchers.anyDouble())).willReturn(trackListByWeather);

        WeatherTrackListRequestDto request = new WeatherTrackListRequestDto();
        request.setCity("Ottawa");

        WeatherTrackListResponseDto response = tracksByWeatherService.getTrackList(request);

        Assertions.assertThat(response).isNotNull();
        org.junit.jupiter.api.Assertions.assertEquals(15, response.getCurrentWeather());
        org.junit.jupiter.api.Assertions.assertEquals("Espresso", response.getTracks().get(0).getName());
    }
}

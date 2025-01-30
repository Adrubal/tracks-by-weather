package com.challenge.backend.controller;

import com.challenge.backend.model.dto.TrackDto;
import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.challenge.backend.model.dto.WeatherTrackListResponseDto;
import com.challenge.backend.model.exception.MissingRequiredParametersException;
import com.challenge.backend.model.exception.ResourceNotFoundException;
import com.challenge.backend.repository.TracksByWeatherStatisticsRepository;
import com.challenge.backend.service.SpotifyService;
import com.challenge.backend.service.TracksByWeatherService;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(TracksByWeatherController.class)
class TracksByWeatherControllerTest {

    @MockitoBean
    private TracksByWeatherService tracksByWeatherService;

    @MockitoBean
    private TracksByWeatherStatisticsRepository tracksByWeatherStatisticsRepository;

    @MockitoBean
    private SpotifyService spotifyService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getTracksByWeather_withCity() throws Exception {

        WeatherTrackListResponseDto responseDto = new WeatherTrackListResponseDto();
        responseDto.setCurrentWeather(26D);
        responseDto.setGenre("pop");

        List<TrackDto> trackList = new ArrayList<>();
        TrackDto track = new TrackDto();
        track.setName("Espresso");
        track.setUrl("https://open.spotify.com/track/2HRqTpkrJO5ggZyyK6NPWz");
        trackList.add(track);
        responseDto.setTracks(trackList);

        BDDMockito.given(tracksByWeatherService
                .getTrackList(ArgumentMatchers.any(WeatherTrackListRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/track-list")
                        .param("city", "Guasave")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentWeather", Matchers.is(26D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tracks[0].name", Matchers.is("Espresso")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre", Matchers.is("pop")));
    }

    @Test
    void getTracksByWeather_withCoords() throws Exception {

        WeatherTrackListResponseDto responseDto = new WeatherTrackListResponseDto();
        responseDto.setCurrentWeather(5D);
        responseDto.setGenre("classic");

        List<TrackDto> trackList = new ArrayList<>();
        TrackDto track = new TrackDto();
        track.setName("Requiem");
        track.setUrl("https://open.spotify.com/track/2HRqTpkrJO5ggZyyK6NPWz");
        trackList.add(track);
        responseDto.setTracks(trackList);

        BDDMockito.given(tracksByWeatherService
                .getTrackList(ArgumentMatchers.any(WeatherTrackListRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/track-list")
                        .param("lat", "20.7167")
                        .param("lon", "-103.4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentWeather", Matchers.is(5D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tracks[0].name", Matchers.is("Requiem")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre", Matchers.is("classic")));
    }

    @Test
    void getTracksByWeather_allParams() throws Exception {

        WeatherTrackListResponseDto responseDto = new WeatherTrackListResponseDto();
        responseDto.setCurrentWeather(26D);
        responseDto.setGenre("pop");

        List<TrackDto> trackList = new ArrayList<>();
        TrackDto track = new TrackDto();
        track.setName("Espresso");
        track.setUrl("https://open.spotify.com/track/2HRqTpkrJO5ggZyyK6NPWz");
        trackList.add(track);
        responseDto.setTracks(trackList);

        BDDMockito.given(tracksByWeatherService
                .getTrackList(ArgumentMatchers.any(WeatherTrackListRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/track-list")
                        .param("city", "Guasave")
                        .param("lat", "20.7167")
                        .param("lon", "-103.4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentWeather", Matchers.is(26D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tracks[0].name", Matchers.is("Espresso")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre", Matchers.is("pop")));
    }

    @Test
    void getTracksByWeather_noParams_missingRequiredParametersException() throws Exception {

        MissingRequiredParametersException missingRequiredParametersException
                = new MissingRequiredParametersException("Required parameter [city or lat - lon]");

        BDDMockito.given(tracksByWeatherService
                        .getTrackList(ArgumentMatchers.any(WeatherTrackListRequestDto.class)))
                .willThrow(missingRequiredParametersException);

        mockMvc.perform(MockMvcRequestBuilders.get("/track-list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.containsString("Required parameter")));
    }

    @Test
    void getTracksByWeather_noLatitude_missingRequiredParametersException() throws Exception {

        MissingRequiredParametersException missingRequiredParametersException
                = new MissingRequiredParametersException("Required parameter [city or lat - lon]");

        BDDMockito.given(tracksByWeatherService
                        .getTrackList(ArgumentMatchers.any(WeatherTrackListRequestDto.class)))
                .willThrow(missingRequiredParametersException);

        mockMvc.perform(MockMvcRequestBuilders.get("/track-list")
                        .param("lon", "-103.4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.containsString("Required parameter")));
    }

    @Test
    void getTracksByWeather_noLongitude_missingRequiredParametersException() throws Exception {

        MissingRequiredParametersException missingRequiredParametersException
                = new MissingRequiredParametersException("Required parameter [city or lat - lon]");

        BDDMockito.given(tracksByWeatherService
                        .getTrackList(ArgumentMatchers.any(WeatherTrackListRequestDto.class)))
                .willThrow(missingRequiredParametersException);

        mockMvc.perform(MockMvcRequestBuilders.get("/track-list")
                        .param("lat", "20.7167")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.containsString("Required parameter")));
    }

    @Test
    void getTracksByWeather_wrongCity_resourceNotFoundException() throws Exception {

        ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException("Wrong City");

        BDDMockito.given(tracksByWeatherService
                        .getTrackList(ArgumentMatchers.any(WeatherTrackListRequestDto.class)))
                .willThrow(resourceNotFoundException);

        mockMvc.perform(MockMvcRequestBuilders.get("/track-list")
                        .param("city", "Guasayork")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.containsString("Wrong City")));
    }
}
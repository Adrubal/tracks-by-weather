package com.challenge.backend.service;

import com.challenge.backend.model.dto.AccessTokenDto;
import com.challenge.backend.model.dto.TrackListResponseDto;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SpotifyServiceTest {

    @InjectMocks
    private SpotifyService spotifyService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AccessTokenService accessTokenService;

    @Test
    public void getTrackListByWeatherTest() throws JsonProcessingException {

        ReflectionTestUtils.setField(spotifyService, "appName", "spotify");
        ReflectionTestUtils.setField(spotifyService, "random", new SecureRandom());
        ReflectionTestUtils.setField(spotifyService, "spotifyUrl", "https://api.spotify.com/v1/search?q=genre:{genre}&type=track&limit={limit}&offset={offset}");
        ReflectionTestUtils.setField(spotifyService, "limit", 20);

        Mockito.when(restTemplate
                        .exchange(ArgumentMatchers.anyString(),
                                ArgumentMatchers.any(HttpMethod.class),
                                ArgumentMatchers.<HttpEntity<?>>any(),
                                ArgumentMatchers.<Class<String>>any(),
                                ArgumentMatchers.anyString(),
                                ArgumentMatchers.anyInt(),
                                ArgumentMatchers.anyInt()))
                .thenReturn(new ResponseEntity<>("{\"tracks\": " +
                        "{\"items\": " +
                        "   [" +
                        "       {\"external_urls\": " +
                        "           {\"spotify\": \"https://open.spotify.com/track/1PINN6x0Riouab3wPSglpp\"" +
                        "           }," +
                        "           \"name\": \"An Idea\"}" +
                        "   ]" +
                        "  }" +
                        "}", HttpStatus.OK));

        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setToken("token");

        accessTokenDto.setCreationDate(LocalDateTime.now().minusMinutes(20));
        Mockito.when(accessTokenService.getAccessToken(ArgumentMatchers.anyString())).thenReturn(Optional.of(accessTokenDto));

        TrackListResponseDto trackListByWeather = spotifyService.getTrackListByWeather(35D);
        Assertions.assertNotNull(trackListByWeather);
    }

    @Test
    public void getTrackListByWeatherTest_No_access_token() throws JsonProcessingException {

        ReflectionTestUtils.setField(spotifyService, "appName", "spotify");
        ReflectionTestUtils.setField(spotifyService, "random", new SecureRandom());
        ReflectionTestUtils.setField(spotifyService, "spotifyUrl", "https://api.spotify.com/v1/search?q=genre:{genre}&type=track&limit={limit}&offset={offset}");
        ReflectionTestUtils.setField(spotifyService, "limit", 20);
        ReflectionTestUtils.setField(spotifyService, "spotifyRequestAccessTokenUrl", "https://accounts.spotify.com/api/token");

        Mockito.when(restTemplate
                        .exchange(ArgumentMatchers.anyString(),
                                ArgumentMatchers.any(HttpMethod.class),
                                ArgumentMatchers.<HttpEntity<?>>any(),
                                ArgumentMatchers.<Class<String>>any()))
                .thenReturn(new ResponseEntity<>("{" +
                        "    \"access_token\": \"token\"," +
                        "    \"token_type\": \"Bearer\"," +
                        "    \"expires_in\": 3600" +
                        "}", HttpStatus.OK));

        Mockito.when(restTemplate
                        .exchange(ArgumentMatchers.anyString(),
                                ArgumentMatchers.any(HttpMethod.class),
                                ArgumentMatchers.<HttpEntity<?>>any(),
                                ArgumentMatchers.<Class<String>>any(),
                                ArgumentMatchers.anyString(),
                                ArgumentMatchers.anyInt(),
                                ArgumentMatchers.anyInt()))
                .thenReturn(new ResponseEntity<>("{\"tracks\": " +
                        "{\"items\": " +
                        "   [" +
                        "       {\"external_urls\": " +
                        "           {\"spotify\": \"https://open.spotify.com/track/1PINN6x0Riouab3wPSglpp\"" +
                        "           }," +
                        "           \"name\": \"An Idea\"}" +
                        "   ]" +
                        "  }" +
                        "}", HttpStatus.OK));

        Mockito.when(accessTokenService.getAccessToken(ArgumentMatchers.anyString())).thenReturn(Optional.empty());

        TrackListResponseDto trackListByWeather = spotifyService.getTrackListByWeather(35D);
        Assertions.assertNotNull(trackListByWeather);
    }
}

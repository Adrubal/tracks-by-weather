package com.challenge.backend.service;

import com.challenge.backend.model.dto.AccessTokenDto;
import com.challenge.backend.model.dto.TrackDto;
import com.challenge.backend.model.dto.TrackListResponseDto;
import com.challenge.backend.model.enums.TracksByWeatherEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SpotifyService {

    private static final String SPOTIFY_RESPONSE_MAIN_SEC = "tracks";
    private static final String SPOTIFY_RESPONSE_ITEMS_LIST = "items";
    private static final String SPOTIFY_RESPONSE_TRACK_NAME = "name";
    private static final String SPOTIFY_RESPONSE_EXTERNAL_URL_NODE = "external_urls";
    private static final String SPOTIFY_RESPONSE_EXTERNAL_URL = "spotify";
    private static final String ACCESS_TOKEN_STRING = "access_token";

    @Value("${spotify.url}")
    private String spotifyUrl;

    @Value("${spotify.request.accesstoken.url}")
    private String spotifyRequestAccessTokenUrl;

    @Value("${spotify.limit}")
    private Integer limit;

    @Value("${spotify.app.name}")
    private String appName;

    @Value("${SPO_CLIENT_ID}")
    private String spoClientId;

    @Value("${SPO_CLIENT_SECRET}")
    private String spoClientSecret;

    private SecureRandom random;

    private String accessToken;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccessTokenService accessTokenService;

    @PostConstruct
    private void postConstruct() {
        random = new SecureRandom();
    }

    public TrackListResponseDto getTrackListByWeather(Double currentWeather) throws JsonProcessingException {

        log.info("Getting spotify track list for current weather of {}", currentWeather);

        TrackListResponseDto response = new TrackListResponseDto();

        TracksByWeatherEnum genre = TracksByWeatherEnum.getGenreByWeather(currentWeather);
        response.setGenre(genre.toString().toLowerCase());

        Optional<AccessTokenDto> accessTokenOptional = getAccessToken();

        if (accessTokenOptional.isEmpty()) {
            accessToken = requestNewAccessToken();
        } else {
            AccessTokenDto accessTokenDto = accessTokenOptional.get();
            if (Boolean.FALSE.equals(isAccessTokenValid(accessTokenDto))) {
                accessToken = requestNewAccessToken();
            } else {
                accessToken = accessTokenDto.getToken();
            }
        }

        JsonNode spotifyResponse = getAllInfoTrackList(genre);
        JsonNode trackListNode = spotifyResponse.get(SPOTIFY_RESPONSE_MAIN_SEC).get(SPOTIFY_RESPONSE_ITEMS_LIST);

        response.setTrackList(itemListToDtoList(trackListNode));

        return response;
    }

    public boolean isAccessTokenValid(AccessTokenDto accessToken) {

        //Spotify access token is valid for 60 minutes, generating a new one just before it expires
        long diff = ChronoUnit.MINUTES.between(accessToken.getCreationDate(), LocalDateTime.now());
        return diff <= 55;
    }

    public String requestNewAccessToken() throws JsonProcessingException {

        log.info("Requesting a new Spotify access token");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");
        requestBody.add("client_id", spoClientId);
        requestBody.add("client_secret", spoClientSecret);
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, createFormUrlEncodedHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                spotifyRequestAccessTokenUrl,
                HttpMethod.POST,
                formEntity,
                String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.getBody());

        accessTokenService.saveAccessToken(appName, jsonNode.get(ACCESS_TOKEN_STRING).asText());
        return jsonNode.get(ACCESS_TOKEN_STRING).asText();
    }

    private JsonNode getAllInfoTrackList(TracksByWeatherEnum genre) throws JsonProcessingException {

        //Generating a random number for offset parameter to get a different track list on every request
        int offset = random.nextInt(100);

        ResponseEntity<String> response = restTemplate.exchange(
                spotifyUrl,
                HttpMethod.GET,
                new HttpEntity<>(createAuthorizationHeaders()),
                String.class,
                genre.getValue(),
                limit,
                offset);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.getBody());
    }

    private Optional<AccessTokenDto> getAccessToken() {
        return accessTokenService.getAccessToken(appName);
    }

    private HttpHeaders createAuthorizationHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        return headers;
    }

    private HttpHeaders createFormUrlEncodedHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
        return headers;
    }

    private List<TrackDto> itemListToDtoList(JsonNode jsonNode) {

        List<TrackDto> trackList = new ArrayList<>();

        for (JsonNode node : jsonNode) {
            TrackDto newTrack = new TrackDto();
            newTrack.setName(node.get(SPOTIFY_RESPONSE_TRACK_NAME).asText());
            newTrack.setUrl(node.get(SPOTIFY_RESPONSE_EXTERNAL_URL_NODE).get(SPOTIFY_RESPONSE_EXTERNAL_URL).asText());

            trackList.add(newTrack);
        }

        return trackList;
    }
}

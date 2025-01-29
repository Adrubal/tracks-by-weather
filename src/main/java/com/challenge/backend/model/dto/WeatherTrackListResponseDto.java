package com.challenge.backend.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherTrackListResponseDto {

    private Double currentWeather;
    private String genre;
    private List<TrackDto> tracks;
}

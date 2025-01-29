package com.challenge.backend.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WeatherTrackListRequestDto {

    private String city;
    private Double lat;
    private Double lon;
}

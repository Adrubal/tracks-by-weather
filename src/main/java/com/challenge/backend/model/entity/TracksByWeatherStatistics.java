package com.challenge.backend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TracksByWeatherStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tracks_by_weather_statistics_id_seq")
    @SequenceGenerator(name = "tracks_by_weather_statistics_id_seq",
            sequenceName = "tracks_by_weather_statistics_id_seq",
            allocationSize = 1)
    private Long id;
    private Double weather;
    private String city;
    private Double lat;
    private Double lon;
    private String tracks;

}

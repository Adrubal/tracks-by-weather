package com.challenge.backend.model.enums;

public enum TracksByWeatherEnum {

    CUMBIA("cumbia"),
    POP("pop"),
    ROCK("rock"),
    CLASSIC("clasica");

    private final String genre;

    TracksByWeatherEnum(String genre) {
        this.genre = genre;
    }

    public String getValue() {
        return this.genre;
    }

    public static TracksByWeatherEnum getGenreByWeather(Double weather) {

        if (weather > 30) {
            return TracksByWeatherEnum.CUMBIA;
        } else if (weather >= 15) {
            return TracksByWeatherEnum.POP;
        } else if (weather >= 10) {
            return TracksByWeatherEnum.ROCK;
        } else {
            return TracksByWeatherEnum.CLASSIC;
        }
    }
}

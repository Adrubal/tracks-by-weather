package com.challenge.backend;

import com.challenge.backend.configuration.RestTemplateResponseErrorHandler;
import com.challenge.backend.configuration.RestTemplateWithErrorHandlerConfig;
import com.challenge.backend.controller.TracksByWeatherController;
import com.challenge.backend.model.dto.AccessTokenDto;
import com.challenge.backend.model.dto.ExceptionResponse;
import com.challenge.backend.model.dto.TrackDto;
import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.challenge.backend.model.entity.TracksByWeatherStatistics;
import com.challenge.backend.model.exception.BadRequestException;
import com.challenge.backend.model.exception.MissingRequiredParametersException;
import com.challenge.backend.model.exception.ResourceNotFoundException;
import com.challenge.backend.service.*;
import com.challenge.backend.utils.RequestValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
// We use direct @Import instead of @ComponentScan to speed up cold starts
// @ComponentScan(basePackages = "com.challenge.backend.controller")
@Import({TracksByWeatherController.class,
        RestTemplateResponseErrorHandler.class,
        RestTemplateWithErrorHandlerConfig.class,
        AccessTokenDto.class,
        ExceptionResponse.class,
        TrackDto.class,
        WeatherTrackListRequestDto.class,
        TracksByWeatherStatistics.class,
        BadRequestException.class,
        MissingRequiredParametersException.class,
        ResourceNotFoundException.class,
        AccessTokenService.class,
        AnalyticsService.class,
        OpenWeatherService.class,
        SpotifyService.class,
        TracksByWeatherService.class,
        RequestValidator.class
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
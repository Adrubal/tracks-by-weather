package com.challenge.backend.utils;

import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.challenge.backend.model.exception.MissingRequiredParametersException;
import org.apache.commons.lang3.StringUtils;

public class RequestValidator {

    public static void validateTracksByWeather(WeatherTrackListRequestDto request) {
        if (StringUtils.isBlank(request.getCity())) {
            if (request.getLon() == null || request.getLat() == null) {
                throw new MissingRequiredParametersException("Required parameter [city or lat - lon]");
            }
        }
    }
}

package com.challenge.backend.model.exception;

import com.challenge.backend.model.dto.WeatherTrackListRequestDto;
import com.challenge.backend.service.OpenWeatherService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MissingRequiredParametersExceptionTest {

    @InjectMocks
    private OpenWeatherService openWeatherService;

    @Test
    void whenExceptionThrown_thenAssertionSucceeds() {
        WeatherTrackListRequestDto request = new WeatherTrackListRequestDto();
        RuntimeException exception = Assertions.assertThrows(MissingRequiredParametersException.class, () -> {
            openWeatherService.getWeather(request);
        });

        String expectedMessage = "Required parameter [city or lat - lon]";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}

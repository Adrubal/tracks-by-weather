package com.challenge.backend.model.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BadRequestExceptionTest {

    @Test
    void whenExceptionThrown_thenAssertionSucceeds() {
        RuntimeException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException("city not found");
        });

        String expectedMessage = "city not found";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}

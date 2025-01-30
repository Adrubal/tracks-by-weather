package com.challenge.backend.model.exception;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InitializationExceptionTest {

    @Test
    void whenExceptionThrown_thenAssertionSucceeds() {
        RuntimeException exception = Assertions.assertThrows(InitializationException.class, () -> {
            throw new InitializationException("Could not initialize Spring Boot application");
        });

        String expectedMessage = "Could not initialize Spring Boot application";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenExceptionThrown_thenAssertionSucceeds_Throwable() {
        NullPointerException npe = new NullPointerException();
        ContainerInitializationException e = new ContainerInitializationException("NullPointerException", npe);
        RuntimeException exception = Assertions.assertThrows(InitializationException.class, () -> {
            throw new InitializationException("NullPointerException", e);
        });

        Assertions.assertTrue(exception.getMessage().contains("NullPointerException"));
    }

    @Test
    void whenExceptionThrown_thenAssertionSucceeds_no_message() {
        RuntimeException exception = Assertions.assertThrows(InitializationException.class, () -> {
            throw new InitializationException();
        });

        Assertions.assertNull(exception.getMessage());
    }
}

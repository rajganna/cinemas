package com.gic.utils;

import com.gic.models.Cinema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RegexUtilsTest {

    private Logger mockLogger;

    @BeforeEach
    void setUp() {
        mockLogger = mock(Logger.class); // Mock the logger
    }

    @Test
    void testProcessInitialParams_ValidInput() {
        // Given
        String input = "CinemaTitle 10 20";  // Example of valid input

        // When
        Optional<Cinema> result = RegexUtils.processInitialParams(input);

        // Then
        assertTrue(result.isPresent(), "The result should be present for valid input");
        assertEquals("CinemaTitle", result.get().title(), "The cinema title should be 'CinemaTitle'");
        assertEquals(10, result.get().rows(), "The cinema should have 10 rows");
        assertEquals(20, result.get().seatsPerRow(), "The cinema should have 20 seats per row");
    }

    @Test
    void testProcessInitialParams_InvalidInput_FormatMismatch() {
        // Given
        String input = "CinemaTitle 10";

        // When
        Optional<Cinema> result = RegexUtils.processInitialParams(input);

        // Then
        assertFalse(result.isPresent(), "The result should be empty for invalid input");
    }

    @Test
    void testProcessInitialParams_InvalidInput_InvalidValues() {
        // Given
        String input = "CinemaTitle -10 -20";

        // When
        Optional<Cinema> result = RegexUtils.processInitialParams(input);

        // Then
        assertFalse(result.isPresent());
    }

}

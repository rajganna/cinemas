package com.gic.utils;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DisplayUtilsTest {
    @Test
    void testDisplaySeatingMap() {
        char[][] seatingMap = {
                {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}
        };

        String currentBookingId = "ABC123";
        Map<String, List<String>> bookingHistory = new HashMap<>();
        bookingHistory.put("XYZ789", Arrays.asList("B3", "B4", "B5"));

        Map<String, List<String>> reservationMap = new HashMap<>();
        reservationMap.put("ABC123", Arrays.asList("C5", "C6"));

        // Capture output using ByteArrayOutputStream
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        DisplayUtils.displaySeatingMap(seatingMap, currentBookingId, bookingHistory, reservationMap);

        String output = outContent.toString();

        assertTrue(output.contains("B  . . . # # # . . . . "));
        assertTrue(output.contains("C  . . . . . o o . . . "));
        assertTrue(output.contains("       🎥 S C R E E N 🎥"));
    }
}

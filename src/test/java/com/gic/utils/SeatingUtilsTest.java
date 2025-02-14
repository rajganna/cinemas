package com.gic.utils;

import com.gic.models.Cinema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeatingUtilsTest {

    private Cinema cinema;

    @BeforeEach
    void setUp() {
        cinema = new Cinema("Cinema1", 5, 10);
    }

    @Test
    void testParseCinemaInput_ValidInput() {
        // Given
        String input = "Cinema1 5 10";

        // When
        Cinema result = SeatingUtils.parseCinemaInput(input);

        // Then
        assertNotNull(result, "Cinema object should not be null");
        assertEquals("Cinema1", result.title(), "Cinema title should be 'Cinema1'");
        assertEquals(5, result.rows(), "Cinema should have 5 rows");
        assertEquals(10, result.seatsPerRow(), "Cinema should have 10 seats per row");
    }

    @Test
    void testAllocateDefaultSeats_Success() {
        // Given
        int numSeats = 10;

        // When
        List<int[]> allocatedSeats = SeatingUtils.allocateDefaultSeats(cinema, numSeats);

        // Then
        assertNotNull(allocatedSeats, "Allocated seats list should not be null");
        for (int[] seat : allocatedSeats) {
            assertTrue(seat[1] >= cinema.seatsPerRow() / 2, "Seats should be allocated to the right half");
        }
    }

    @Test
    void testAllocateCustomSeats_Success() {
        // Given
        int numTickets = 3;
        int startRow = 1;
        int startCol = 2;

        // When
        List<int[]> allocatedSeats = SeatingUtils.allocateCustomSeats(cinema, numTickets, startRow, startCol);

        // Then
        assertNotNull(allocatedSeats, "Allocated seats list should not be null");
        for (int[] seat : allocatedSeats) {
            assertEquals(startRow, seat[0], "All seats should be in the same row as specified");
            assertTrue(seat[1] >= startCol, "Seats should be allocated starting from the specified column");
        }
    }

    @Test
    void testAllocateDefaultSeats_NoSeatsAvailable() {
        // Given
        int numSeats = 100;  // Requesting more seats than available

        // When
        List<int[]> allocatedSeats = SeatingUtils.allocateDefaultSeats(cinema, numSeats);

        // Then
        assertTrue(allocatedSeats.isEmpty(), "No seats should be allocated if the number exceeds availability");
    }
}

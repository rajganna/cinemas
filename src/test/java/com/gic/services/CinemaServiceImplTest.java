package com.gic.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CinemaServiceImplTest {

    private CinemaServiceImpl cinemaService;

    @BeforeEach
    void setUp() {
        cinemaService = new CinemaServiceImpl();
        String input = "Inception 8 10";
        cinemaService.initializeCinema(input);
    }

    @Test
    void testBookTickets_DefaultSeats() {
        int numSeats = 3;
        cinemaService.bookTickets(numSeats);

        assertTrue(cinemaService.getSeatsAvailable() <= 97);
    }

    @Test
    void testBookTickets_CustomSeats() {
        int numSeats = 3;
        int startRow = 2;
        int startCol = 3;

        cinemaService.bookTickets(numSeats, startRow, startCol);

        assertTrue(cinemaService.getSeatsAvailable() <= 97);
    }

    @Test
    void testReserveSeats() {
        String bookingId = "BOOK-123";
        int numSeats = 5;

        cinemaService.reserveSeats(numSeats, bookingId);

        assertTrue(cinemaService.getSeatsAvailable() <= 95);
    }

    @Test
    void testConfirmSeats() {
        String bookingId = "BOOK-123";
        int numSeats = 3;

        cinemaService.reserveSeats(numSeats, bookingId);
        cinemaService.confirmSeats(bookingId);

        assertEquals(77, cinemaService.getSeatsAvailable());
    }
}

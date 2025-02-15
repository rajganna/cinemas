package com.gic.services;

import com.gic.models.Cinema;

import java.util.Optional;

public interface CinemaService {
    Optional<Cinema> initializeCinema(String input);
    int getSeatsAvailable();
    void bookTickets(int numTickets, int startRow, int startCol);
    void bookTickets(int numTickets);
    void reserveSeats(int numSeats, String bookingId);
    void displayMenu();
    String getTitle();
    void displaySeatMap(String bookingId);
    void confirmSeats(String bookingId);
    void checkBooking(String bookingId);
    void confirmSeatsWithStarting(String bookingId, String startSeat, int numSeats);
    boolean cancelReservation(String bookingId);
}

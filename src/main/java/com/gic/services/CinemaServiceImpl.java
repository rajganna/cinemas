package com.gic.services;

import com.gic.models.Cinema;
import com.gic.utils.DisplayUtils;
import com.gic.utils.SeatingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.gic.utils.RegexUtils.processInitialParams;

@Service
public class CinemaServiceImpl implements CinemaService {

    private static final Logger logger = LoggerFactory.getLogger(CinemaServiceImpl.class);
    private Cinema cinema;
    private final Map<String, List<String>> bookingHistory = new HashMap<>();


    @Override
    public Optional<Cinema> initializeCinema(String input) {
        Optional<Cinema> cinemaOptional = processInitialParams(input);
        cinemaOptional.ifPresent(c -> {
            this.cinema = c;
        });
        return cinemaOptional;
    }

    @Override
    public void bookTickets(int numSeats) {
        if (numSeats <= 0 || numSeats > cinema.getSeatsAvailable()) {
            logger.warn("Sorry, only {} seats are available.", cinema.getSeatsAvailable());
            return;
        }
        List<int[]> allocatedSeats = SeatingUtils.allocateDefaultSeats(cinema, numSeats);
        printBookingDetails(allocatedSeats);
    }

    @Override
    public void bookTickets(int numSeats, int startRow, int startCol) {
        List<int[]> allocatedSeats = SeatingUtils.allocateCustomSeats(cinema, numSeats, startRow, startCol);
        printBookingDetails(allocatedSeats);
    }

    private void printBookingDetails(List<int[]> allocatedSeats) {
        String bookingId = "BOOK-" + System.currentTimeMillis();
        logger.info("Booking successful! Booking ID: {}", bookingId);
        logger.info("Seats allocated: {}", allocatedSeats);
        SeatingUtils.displaySeatingMap(cinema, allocatedSeats);
    }

    @Override
    public int getSeatsAvailable() {
        return cinema.getSeatsAvailable();
    }

    @Override
    public void reserveSeats(int numSeats, String bookingId) {
        List<String> allocatedSeats = reserveSeat(numSeats, bookingId);
        bookingHistory.put(bookingId, allocatedSeats);
    }

    @Override
    public void displayMenu() {
        DisplayUtils.displayMenu(cinema.title(), cinema.getSeatsAvailable());
    }

    @Override
    public String getTitle() {
        return cinema.title();
    }

    @Override
    public void displaySeatMap(String bookingId) {
        DisplayUtils.displaySeatingMap(cinema.seatingMap(), bookingId, bookingHistory, cinema.reservationMap());
    }

    @Override
    public void confirmSeats(String bookingId) {
        confirmReservation(bookingId);
        cinema.reservationMap().remove(bookingId);
    }

    @Override
    public void checkBooking(String bookingId) {
        if (bookingHistory.containsKey(bookingId)) {
            DisplayUtils.displaySeatingMap(cinema.seatingMap(), bookingId, bookingHistory, cinema.reservationMap());
        }   else {
             logger.warn("Invalid booking id");
        }
    }

    @Override
    public void confirmSeatsWithStarting(String bookingId, String startSeat, int numSeats) {
        List<String> allocatedSeats = confirmReservationStarting(startSeat, numSeats);
        if (!allocatedSeats.isEmpty()) {
            cinema.reservationMap().put(bookingId, allocatedSeats);
            bookingHistory.put(bookingId, allocatedSeats);
            confirmSeats(bookingId);
        }
    }

    @Override
    public boolean cancelReservation(String bookingId) {
        if (!cinema.reservationMap().containsKey(bookingId)) {
            return false;
        }

        List<String> reservedSeats = cinema.reservationMap().get(bookingId);
        for (String seat : reservedSeats) {
            int row = seat.charAt(0) - 'A';
            int col = Integer.parseInt(seat.substring(1)) - 1;
            if (cinema.seatingMap()[row][col] == 'o') {
                cinema.seatingMap()[row][col] = '.';
            }
        }

        cinema.reservationMap().remove(bookingId);
        return true;
    }

    private List<String> reserveSeat(int numSeats, String bookingId)  {
        List<String> allocatedSeats = new ArrayList<>();

        if (numSeats > getSeatsAvailable()) {
            return allocatedSeats;
        }

        for (int i = 0; i < cinema.rows() && allocatedSeats.size() < numSeats; i++) {
            List<Integer> availableColumns = new ArrayList<>();

            for (int j = 0; j < cinema.seatsPerRow(); j++) {
                if (cinema.seatingMap()[i][j] == '.') {
                    availableColumns.add(j);
                }
            }

            if (availableColumns.isEmpty()) continue; // Skip fully booked rows

            int mid = (cinema.seatsPerRow() - 1) / 2;

            availableColumns.sort(Comparator.comparingInt((Integer col) -> Math.abs(col - mid))
                    .thenComparingInt(col -> col));

            List<Integer> orderedSeats = new ArrayList<>();

            // Allocate seats starting from the middle, expanding outward
            int left = mid, right = mid + 1;
            while (orderedSeats.size() < numSeats && (left >= 0 || right < cinema.seatsPerRow())) {
                if (left >= 0 && availableColumns.contains(left)) {
                    orderedSeats.add(left);
                }
                if (right < cinema.seatsPerRow() && availableColumns.contains(right)) {
                    orderedSeats.add(right);
                }
                left--;
                right++;
            }

            for (int col : orderedSeats) {
                if (allocatedSeats.size() < numSeats) {
                    cinema.seatingMap()[i][col] = 'o';
                    allocatedSeats.add((char) ('A' + i) + String.valueOf(col));
                } else {
                    break;
                }
            }
        }

        if (!allocatedSeats.isEmpty()) {
            cinema.reservationMap().put(bookingId, allocatedSeats);
        }

        return allocatedSeats;
    }

    private void confirmReservation(String bookingId) {
        if (!cinema.reservationMap().containsKey(bookingId)) {
            return;
        }

        List<String> reservedSeats = cinema.reservationMap().get(bookingId);
        for (String seat : reservedSeats) {
            int row = seat.charAt(0) - 'A';
            int col = Integer.parseInt(seat.substring(1));
            if (cinema.seatingMap()[row][col] == 'o') {
                cinema.seatingMap()[row][col] = '#';
            }
        }
    }

    private List<String> confirmReservationStarting(String startSeat, int numSeats) {
        List<String> allocatedSeats = new ArrayList<>();

        if (startSeat.length() < 2) {
            logger.warn("❌ Invalid seat format. Please enter a valid seat (e.g., C5).");
            return List.of();
        }

        int startRow = startSeat.charAt(0) - 'A';
        int startCol;

        try {
            startCol = Integer.parseInt(startSeat.substring(1), 10) - 1;  // Ensure leading zeros don't affect parsing
            logger.info("Parsed seat: " + startSeat + " → Row: " + startRow + " Col: " + startCol);
        } catch (NumberFormatException e) {
            logger.warn("❌ Invalid seat number. Please enter a valid seat (e.g., C5).");
            return List.of();
        }

        if (startRow < 0 || startRow >= cinema.rows() || startCol < 0 || startCol >= cinema.seatsPerRow()) {
            logger.warn("❌ Invalid seat selection: Out of bounds.");
            return List.of();
        }

        int seatsFilled = 0;
        for (int row = startRow; row < cinema.rows() && seatsFilled < numSeats; row++) {
            for (int col = (row == startRow ? startCol : 0); col < cinema.seatsPerRow() && seatsFilled < numSeats; col++) {
                if (cinema.seatingMap()[row][col] == '.') {
                    cinema.seatingMap()[row][col] = 'o';
                    String seatLabel = (char) ('A' + row) + Integer.toString(col);
                    logger.info("Allocating seat: " + seatLabel);
                    allocatedSeats.add(seatLabel);
                    seatsFilled++;
                }
            }
        }

        return allocatedSeats;
    }
}

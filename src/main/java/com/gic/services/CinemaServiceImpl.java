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
    public void initializeCinema(String input) {
        Optional<Cinema> cinemaOptional = processInitialParams(input);
        cinemaOptional.ifPresent(c -> {
            this.cinema = c;
        });
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
        List<String> allocatedSeats = confirmReservationStarting(bookingId, startSeat, numSeats);
        if (!allocatedSeats.isEmpty())
            bookingHistory.put(bookingId, allocatedSeats);
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

            int mid = cinema.seatsPerRow() / 2;
            availableColumns.sort(Comparator.comparingInt(col -> Math.abs(col - mid +  1)));

            for (int col : availableColumns) {
                if (allocatedSeats.size() < numSeats) {
                    cinema.seatingMap()[i][col] = 'o';
                    allocatedSeats.add((char) ('A' + i) + String.valueOf(col + 1));
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

        cinema.reservationMap().remove(bookingId);
    }

    private List<String> confirmReservationStarting(String bookingId, String startSeat, int numSeats) {
        List<String> allocatedSeats = new ArrayList<>();
        if (startSeat.length() < 2) {
            System.out.println("❌ Invalid seat format. Please enter a valid seat (e.g., C5).");
            return List.of();
        }

        int startRow = startSeat.charAt(0) - 'A';
        int startCol;

        try {
            startCol = Integer.parseInt(startSeat.substring(1)) - 1;
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid seat number. Please enter a valid seat (e.g., C5).");
            return List.of();
        }

        if (startRow < 0 || startRow >= cinema.rows() || startCol < 0 || startCol >= cinema.seatsPerRow()) {
            System.out.println("❌ Invalid seat selection: Out of bounds.");
            return List.of();
        }

        int seatsFilled = 0;
        for (int col = startCol; col < cinema.seatsPerRow() && seatsFilled < numSeats; col++) {
            if (cinema.seatingMap()[startRow][col] == '.') {
                cinema.seatingMap()[startRow][col] = 'o';
                allocatedSeats.add((char) ('A' + startRow) + String.valueOf(col + 1));
                seatsFilled++;
            }
        }
        return allocatedSeats;
    }
}

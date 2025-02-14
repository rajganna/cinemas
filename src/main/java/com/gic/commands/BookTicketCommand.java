package com.gic.commands;

import com.gic.services.CinemaService;
import com.gic.utils.DisplayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.UUID;

@Component
public class BookTicketCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(BookTicketCommand.class);
    private final CinemaService cinemaService;
    private final Scanner scanner = new Scanner(System.in);

    public BookTicketCommand(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @Override
    public void execute() {
        while (true) {
            logger.info("Enter number of tickets to book, or enter blank to go back to main menu:");
            DisplayUtils.display();
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                logger.info("Returning to main menu...");
                return;
            }

            try {
                int ticketsRequested = Integer.parseInt(input);
                int availableSeats = cinemaService.getSeatsAvailable();

                if (ticketsRequested > availableSeats) {
                    logger.warn("Sorry, only {} seats are available.", availableSeats);
                    continue;
                }

                String bookingId = "GIC" + String.format("%04d", UUID.randomUUID().hashCode() & 0x7FFFFFFF);
                cinemaService.reserveSeats(ticketsRequested, bookingId);

                logger.info("Successfully reserved {} {} tickets.", ticketsRequested, cinemaService.getTitle());
                logger.info("Booking ID: {}", bookingId);

                cinemaService.displaySeatMap(bookingId);
                logger.info("Enter blank to accept seat selection, or enter new seating position:");
                DisplayUtils.display();

                String seatSelection = scanner.nextLine().trim();
                if (seatSelection.length() == 0) {
                    cinemaService.confirmSeats(bookingId);
                } else {
                    cinemaService.confirmSeatsWithStarting(bookingId, seatSelection, ticketsRequested);
                }
                cinemaService.displaySeatMap(bookingId);
                break;

            } catch (NumberFormatException e) {
                logger.error("Invalid input. Please enter a valid number.");
            }
        }
    }
}

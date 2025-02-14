package com.gic.commands;

import com.gic.services.CinemaService;
import com.gic.utils.DisplayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CheckBookingsCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(BookTicketCommand.class);
    private final CinemaService cinemaService;
    private final Scanner scanner = new Scanner(System.in);
    public CheckBookingsCommand(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @Override
    public void execute() {


        while (true) {
            logger.info("Enter booking id, or enter blank to go back to main menu:");
            DisplayUtils.display();
            String input = scanner.nextLine().trim();

            if (input.length() == 0) {
                break;
            }
            cinemaService.checkBooking(input);
        }

    }
}

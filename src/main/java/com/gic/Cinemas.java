package com.gic;

import com.gic.commands.Command;
import com.gic.commands.CommandFactory;
import com.gic.models.Cinema;
import com.gic.services.CinemaService;
import com.gic.utils.DisplayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class Cinemas implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Cinemas.class);
    private final CinemaService cinemaService;
    private final CommandFactory commandFactory;

    public Cinemas(CinemaService cinemaService, CommandFactory commandFactory) {
        this.cinemaService = cinemaService;
        this.commandFactory = commandFactory;
    }

    public static void main(String[] args) {
        SpringApplication.run(Cinemas.class, args);

    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        Optional<Cinema> cinema = Optional.empty();
        while (cinema.isEmpty()) {
            logger.info("Please define movie title and seating map in [Title] [Row] [SeatsPerRow] Format: ");
            DisplayUtils.display();
            String input = scanner.nextLine();
            cinema  = cinemaService.initializeCinema(input);
        }
        while (true) {
            displayMenu();
            DisplayUtils.display();
            int choice = scanner.nextInt();
            Command command = commandFactory.getCommand(choice);
            command.execute();
        }
    }

    private void displayMenu() {
        cinemaService.displayMenu();

    }
}

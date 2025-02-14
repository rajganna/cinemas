package com.gic.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class DisplayUtils {


    private static final Logger logger = LoggerFactory.getLogger(DisplayUtils.class);
    public static void display() {
        System.out.print("\u001B[36m > \u001B[0m");
    }

    public static void displaySeatingMap(char[][] seatingMap,
                                         String currentBookingId,
                                         Map<String, List<String>> bookingHistory,
                                         Map<String, List<String>> reservationMap) {

            System.out.println("\n       🎥 S C R E E N 🎥       ");
            System.out.println("   " + "-".repeat(seatingMap[0].length * 2));

        for (int row = seatingMap.length - 1; row >= 0; row--) {
            char rowLabel = (char) ('A' + row);
            System.out.print(rowLabel + "  ");

            for (int col = 0; col < seatingMap[0].length; col++) {
                String seat = rowLabel + String.valueOf(col);

                boolean isCurrentUserSeat = reservationMap.containsKey(currentBookingId) &&
                        reservationMap.get(currentBookingId).contains(seat);

                boolean isBooked = bookingHistory.values().stream().anyMatch(seats -> seats.contains(seat));

                if (isCurrentUserSeat) {
                    System.out.print("o ");
                } else if (isBooked) {
                    System.out.print("# ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }

        System.out.print("   ");
        for (int col = 0; col < seatingMap[0].length; col++) {
            System.out.print((col + 1) + " ");
        }
        System.out.println();
    }


    public static void displayMenu(String title, int availableSeats) {
        logger.info("🎬 Welcome to GIC Cinemas 🎬");
        logger.info("[1] Book tickets for {} ({} seats available)", title,
                availableSeats);
        logger.info("[2] Check bookings");
        logger.info("[3] Exit");
        logger.info("Please enter your selection:");
    }
}

package com.gic.utils;

import com.gic.models.Cinema;

import java.util.ArrayList;
import java.util.List;

public class SeatingUtils {

    public static Cinema parseCinemaInput(String input) {
        String[] parts = input.split(" ");
        String title = parts[0];
        int rows = Integer.parseInt(parts[1]);
        int seatsPerRow = Integer.parseInt(parts[2]);
        return new Cinema(title, rows, seatsPerRow);
    }

    public static List<int[]> allocateDefaultSeats(Cinema cinema, int numSeats) {
        List<int[]> allocatedSeats = new ArrayList<>();
        char[][] seatingMap = cinema.seatingMap();
        int rows = seatingMap.length;
        int cols = seatingMap[0].length;

        for (int r = rows - 1; r >= 0; r--) {
            for (int c = cols / 2; c < cols; c++) {
                if (seatingMap[r][c] == 0 && allocatedSeats.size() < numSeats) {
                    seatingMap[r][c] = 1;
                    allocatedSeats.add(new int[]{r, c});
                }
            }
            if (allocatedSeats.size() == numSeats) break;
        }

        return allocatedSeats;
    }

    public static List<int[]> allocateCustomSeats(Cinema cinema, int numTickets, int startRow, int startCol) {
        List<int[]> allocatedSeats = new ArrayList<>();
        char[][] seatingMap = cinema.seatingMap();
        int rows = seatingMap.length;
        int cols = seatingMap[0].length;

        for (int c = startCol; c < cols; c++) {
            if (seatingMap[startRow][c] == 0 && allocatedSeats.size() < numTickets) {
                seatingMap[startRow][c] = 1;
                allocatedSeats.add(new int[]{startRow, c});
            }
        }

        if (allocatedSeats.size() < numTickets) {
            allocatedSeats.addAll(allocateDefaultSeats(cinema, numTickets - allocatedSeats.size()));
        }

        return allocatedSeats;
    }

    public static void displaySeatingMap(Cinema cinema, List<int[]> newBookings) {
        System.out.println("\n        🎥 S C R E E N 🎥");
        System.out.println("  --------------------------------");

        // Print column numbers (1-based index)
        System.out.print("    ");
        for (int j = 0; j < cinema.seatsPerRow(); j++) {
            System.out.print((j + 1) + " ");
        }
        System.out.println();

        // Print each row
        for (int i = 0; i < cinema.rows(); i++) {
            System.out.print((char) ('A' + i) + "   ");  // Print row letter
            for (int j = 0; j < cinema.seatsPerRow(); j++) {
                switch (cinema.seatingMap()[i][j]) {
                    case 0 -> System.out.print(". ");  // Available
                    case 1 -> System.out.print("# ");  // Previously reserved
                    case 2 -> System.out.print("o ");  // Newly booked
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}

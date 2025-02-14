package com.gic.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.With;

import java.util.*;

@With
public record Cinema(@NotBlank(message = "Movie title cannot be empty.") String title,
                     @Min(value = 1, message = "Rows must be at least 1.")
                     @Max(value = 26, message = "Rows cannot exceed 26.") int rows,
                     @Min(value = 1, message = "Seats per row must be at least 1.")
                     @Max(value = 50, message = "Seats per row cannot exceed 50.") int seatsPerRow,
                     char[][] seatingMap,
                     Map<String, List<String>> reservationMap) {

    public Cinema(String title, int rows, int seatsPerRow) {
        this(title, rows, seatsPerRow, initializeSeating(rows, seatsPerRow), new HashMap<>());
    }

    private static char[][] initializeSeating(int rows, int seatsPerRow) {
        char[][] map = new char[rows][seatsPerRow];
        for (char[] row : map) {
            Arrays.fill(row, '.');
        }
        return map;
    }

    public int getSeatsAvailable() {
        return (int) Arrays.stream(seatingMap)
                .flatMapToInt(row -> new String(row).chars())
                .filter(seat -> seat == '.')
                .count();
    }
}

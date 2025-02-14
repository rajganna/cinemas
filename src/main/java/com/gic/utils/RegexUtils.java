package com.gic.utils;

import com.gic.models.Cinema;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gic.utils.Constants.INITIAL_INPUT;

public class RegexUtils {
    private static final Logger logger = LoggerFactory.getLogger(RegexUtils.class);
    public static Optional<Cinema> processInitialParams(String input) {
        Pattern pattern = Pattern.compile(INITIAL_INPUT);
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            String title = matcher.group(1);
            int row = Integer.parseInt(matcher.group(2));
            int seatsPerRow = Integer.parseInt(matcher.group(3));

            if (row < 0 || seatsPerRow < 0) {
                logger.warn("Invalid input format! Please use the correct format: [Title] [Row] [SeatsPerRow]");
                return Optional.empty();
            }

            var cinema = new Cinema(title, row, seatsPerRow);
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Cinema>> violations = validator.validate(cinema);
            if (!violations.isEmpty()) {
                for (ConstraintViolation<Cinema> violation : violations) {
                   logger.warn(violation.getMessage());
                }
                return Optional.empty();
            }
            return Optional.of(cinema);

        } else {
            logger.warn("Invalid input format! Please use the correct format: [Title] [Row] [SeatsPerRow]");
            return Optional.empty();
        }
    }
}

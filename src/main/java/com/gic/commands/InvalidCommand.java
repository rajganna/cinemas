package com.gic.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InvalidCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(InvalidCommand.class);
    @Override
    public void execute() {
        logger.warn("❌ Invalid command. Please enter a valid option.");
    }
}

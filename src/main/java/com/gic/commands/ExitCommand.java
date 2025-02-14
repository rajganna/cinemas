package com.gic.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExitCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(ExitCommand.class);
    @Override
    public void execute() {
        logger.info("Exiting the system...");
        System.exit(0);
    }
}

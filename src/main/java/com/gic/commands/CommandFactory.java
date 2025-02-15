package com.gic.commands;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CommandFactory {
    private final Map<Integer, Class<? extends Command>> commandMap;
    private final ApplicationContext context;

    public CommandFactory(ApplicationContext context) {
        this.context = context;
        this.commandMap = Map.of(
                1, BookTicketCommand.class,
                2, CheckBookingsCommand.class,
                3, ExitCommand.class
        );
    }

    public Command getCommand(int choice) {
        Class<? extends Command> commandClass = commandMap.get(choice);

        if (commandClass == null) {
            return context.getBean(InvalidCommand.class);
        }

        return context.getBean(commandClass);
    }
}

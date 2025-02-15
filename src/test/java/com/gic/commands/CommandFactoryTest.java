package com.gic.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandFactoryTest {

    @Mock
    private ApplicationContext context;

    @Mock
    private BookTicketCommand bookTicketCommand;

    @Mock
    private CheckBookingsCommand checkBookingsCommand;

    @Mock
    private ExitCommand exitCommand;

    @Mock
    private InvalidCommand invalidCommand;

    @InjectMocks
    private CommandFactory commandFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(context.getBean(BookTicketCommand.class)).thenReturn(bookTicketCommand);
        when(context.getBean(CheckBookingsCommand.class)).thenReturn(checkBookingsCommand);
        when(context.getBean(ExitCommand.class)).thenReturn(exitCommand);
        when(context.getBean(InvalidCommand.class)).thenReturn(invalidCommand);
    }

    @Test
    void testGetCommand_ValidChoice_BookTicketCommand() {
        Command command = commandFactory.getCommand(1);

        assertSame(bookTicketCommand, command);
        verify(context).getBean(BookTicketCommand.class);
    }

    @Test
    void testGetCommand_ValidChoice_CheckBookingsCommand() {
        Command command = commandFactory.getCommand(2);

        assertSame(checkBookingsCommand, command);
        verify(context).getBean(CheckBookingsCommand.class);
    }

    @Test
    void testGetCommand_ValidChoice_ExitCommand() {
        // Simulate user choosing option 3
        Command command = commandFactory.getCommand(3);

        assertSame(exitCommand, command);
        verify(context).getBean(ExitCommand.class);
    }

    @Test
    void testGetCommand_InvalidChoice() {
        Command command = commandFactory.getCommand(999);

        assertSame(invalidCommand, command);
        verify(context).getBean(InvalidCommand.class);
    }
}



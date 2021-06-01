package org.eurostates.mosecommands.context;

import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.jetbrains.annotations.NotNull;

public class ErrorContext {

    private final @NotNull ArgumentCommand command;
    private final int argumentFailedAt;
    private final @NotNull CommandArgument<?> argument;
    private final @NotNull String error;

    public ErrorContext(@NotNull ArgumentCommand command, int argumentFailedAt, @NotNull CommandArgument<?> argument, @NotNull String error) {
        this.command = command;
        this.argumentFailedAt = argumentFailedAt;
        this.argument = argument;
        this.error = error;
    }

    public @NotNull ArgumentCommand getCommand() {
        return command;
    }

    public int getArgumentFailedAt() {
        return argumentFailedAt;
    }

    public @NotNull CommandArgument<?> getArgument() {
        return argument;
    }

    public @NotNull String getError() {
        return error;
    }
}

package org.eurostates.mosecommands.context;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.ParseCommandArgument;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CommandArgumentContext<T> {

    private final @NotNull ParseCommandArgument<T> argument;
    private final boolean asSuggestion;
    private int firstArgument;
    private String[] command;

    public CommandArgumentContext(@NotNull ParseCommandArgument<T> argument, int firstArgument, String... command) {
        this(argument, firstArgument, false, command);
    }

    public CommandArgumentContext(@NotNull ParseCommandArgument<T> argument, int firstArgument, CommandArgumentContext<?> context){
        this(argument, firstArgument, context.isAsSuggestion(), context.command);
    }

    public CommandArgumentContext(@NotNull ParseCommandArgument<T> argument, CommandArgumentContext<?> context) {
        this(argument, context.getFirstArgument(), context.isAsSuggestion(), context.command);
    }

    public CommandArgumentContext(@NotNull ParseCommandArgument<T> argument, int firstArgument, boolean asSuggestion, String... command) {
        this.argument = argument;
        this.firstArgument = firstArgument;
        this.command = command;
        this.asSuggestion = asSuggestion;
    }

    public boolean isAsSuggestion() {
        return this.asSuggestion;
    }

    public @NotNull ParseCommandArgument<T> getArgument() {
        return this.argument;
    }

    public @NotNull String[] getRemainingArguments() {
        int last = this.command.length;
        return Arrays.copyOfRange(this.command, this.firstArgument, last);
    }

    public int getFirstArgument() {
        return this.firstArgument;
    }

    public void setCommand(String... args) {
        this.command = args;
    }

    public void setStartArgument(int start) {
        this.firstArgument = start;
    }
}

package org.eurostates.mosecommands.context;

import org.bukkit.command.CommandSender;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.operation.OptionalArgument;
import org.eurostates.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class CommandContext {

    private final @NotNull String[] command;
    private final @NotNull CommandSender sender;
    private final @NotNull Set<ArgumentCommand> potentialCommands = new HashSet<>();

    public CommandContext(@NotNull CommandSender source, @NotNull Collection<ArgumentCommand> commands, String... command) {
        this.command = command;
        this.potentialCommands.addAll(commands);
        this.sender = source;
    }

    public @NotNull String[] getCommand() {
        return this.command;
    }

    public @NotNull CommandSender getSource() {
        return this.sender;
    }

    public @NotNull List<String> getSuggestions(@NotNull ArgumentCommand command) {
        CommandArgument<?>[] arguments = command.getArguments();
        int commandArgument = 0;
        List<OptionalArgument<?>> optionalArguments = new ArrayList<>();
        for (CommandArgument<?> arg : arguments) {
            if (this.command.length == commandArgument) {
                if (arg instanceof OptionalArgument) {
                    optionalArguments.add((OptionalArgument<?>) arg);
                    continue;
                }
                return this.suggest(arg, commandArgument);
            }
            if (this.command.length < commandArgument) {
                throw new IllegalArgumentException("Not enough provided arguments for value of that argument");
            }
            try {
                Map.Entry<?, Integer> entry = this.parse(arg, commandArgument, true);
                if (commandArgument == entry.getValue() && arg instanceof OptionalArgument) {
                    optionalArguments.add((OptionalArgument<?>) arg);
                } else {
                    optionalArguments.clear();
                }
                commandArgument = entry.getValue();
            } catch (IOException e) {
                return this.suggest(arg, commandArgument);
            }
        }
        if (optionalArguments.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> ret = new ArrayList<>();
        for (OptionalArgument<?> argument : optionalArguments) {
            ret.addAll(suggest(argument, commandArgument));
        }
        return ret;
    }

    public <T> @NotNull T getArgument(@NotNull ArgumentCommand command, @NotNull CommandArgument<T> id) {
        return this.getArgument(command, id.getId());
    }

    public <T> @NotNull T getArgument(@NotNull ArgumentCommand command, String id){
        return getArgument(command, id, false);
    }

    public <T> @NotNull T getArgument(@NotNull ArgumentCommand command, @NotNull String id, boolean asSuggestion) {
        CommandArgument<?>[] arguments = command.getArguments();
        if (Stream.of(arguments).noneMatch(a -> a.getId().equals(id))) {
            throw new IllegalArgumentException("Argument ID not found within command");
        }
        int commandArgument = 0;
        for (CommandArgument<?> arg : arguments) {
            if (this.command.length == commandArgument && arg instanceof OptionalArgument) {
                if (arg.getId().equals(id)) {
                    try {
                        return (T) this.parse(arg, commandArgument, asSuggestion).getKey();
                    } catch (IOException ignored) {
                    }
                }
                continue;
            }
            if (this.command.length < commandArgument) {
                throw new IllegalArgumentException("Not enough provided arguments for value of that argument");
            }
            try {
                Map.Entry<?, Integer> entry = this.parse(arg, commandArgument, asSuggestion);
                commandArgument = entry.getValue();
                if (arg.getId().equals(id)) {
                    return (T) entry.getKey();
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        throw new IllegalArgumentException("Argument ID of '" + id + "' not found within command");
    }

    public Set<ErrorContext> getErrors() {
        Set<ErrorContext> map = new HashSet<>();
        for (ArgumentCommand command : this.potentialCommands) {
            CommandArgument<?>[] arguments = command.getArguments();
            int commandArgument = 0;
            for (CommandArgument<?> arg : arguments) {
                if (this.command.length == commandArgument && arg instanceof OptionalArgument) {
                    continue;
                }
                if (this.command.length <= commandArgument) {
                    ErrorContext context = new ErrorContext(command, commandArgument, arg, "Not enough arguments");
                    map.add(context);
                    break;
                }
                try {
                    Map.Entry<?, Integer> entry = this.parse(arg, commandArgument, false);
                    commandArgument = entry.getValue();
                } catch (IOException e) {
                    ErrorContext context = new ErrorContext(command, commandArgument, arg, e.getMessage());
                    map.add(context);
                    break;
                }
            }

        }
        return Utils.getBest(map, (BiFunction<ErrorContext, ErrorContext, Utils.Compare>) (e1, e2) -> {
            int at1 = e1.getArgumentFailedAt();
            int at2 = e2.getArgumentFailedAt();
            if (at1 == at2) {
                return Utils.Compare.EQUAL;
            }
            if (at1 > at2) {
                return Utils.Compare.WORSE;
            }
            return Utils.Compare.BETTER;
        });
    }

    public Optional<ArgumentCommand> getCompleteCommand(boolean asSuggestion) {
        return this.potentialCommands.stream().filter(command -> {
            CommandArgument<?>[] arguments = command.getArguments();
            int commandArgument = 0;
            for (CommandArgument<?> arg : arguments) {
                if (this.command.length == commandArgument && arg instanceof OptionalArgument) {
                    continue;
                }
                if (this.command.length <= commandArgument) {
                    return false;
                }
                try {
                    Map.Entry<?, Integer> entry = this.parse(arg, commandArgument, asSuggestion);
                    commandArgument = entry.getValue();
                } catch (IOException e) {
                    return false;
                }
            }
            return this.command.length == commandArgument;
        }).findAny();

    }

    public Set<ArgumentCommand> getPotentialCommands(boolean asSuggestion) {
        Map<ArgumentCommand, Integer> map = new HashMap<>();
        this.potentialCommands.forEach(c -> {
            CommandArgument<?>[] arguments = c.getArguments();
            int commandArgument = 0;
            int completeArguments = 0;
            for (CommandArgument<?> arg : arguments) {
                if (this.command.length == commandArgument && arg instanceof OptionalArgument) {
                    continue;
                }
                if (this.command.length <= commandArgument) {
                    map.put(c, completeArguments);
                    return;
                }
                try {
                    Map.Entry<?, Integer> entry = this.parse(arg, commandArgument, asSuggestion);
                    if (commandArgument != entry.getValue()) {
                        commandArgument = entry.getValue();
                        completeArguments++;
                    }
                } catch (IOException e) {
                    map.put(c, completeArguments);
                    return;
                }
            }
            map.put(c, completeArguments);
        });

        Set<ArgumentCommand> set = new HashSet<>();
        int current = 0;
        for (Map.Entry<ArgumentCommand, Integer> entry : map.entrySet()) {
            if (entry.getValue() > current) {
                current = entry.getValue();
                set.clear();
            }
            if (entry.getValue() == current) {
                set.add(entry.getKey());
            }
        }
        return set;
    }

    private <T> Map.Entry<T, Integer> parse(@NotNull CommandArgument<T> arg, int commandArgument, boolean asSuggestion) throws IOException {
        CommandArgumentContext<T> argContext = new CommandArgumentContext<>(arg, commandArgument, asSuggestion, this.command);
        return arg.parse(this, argContext);
    }

    private <T> List<String> suggest(@NotNull CommandArgument<T> arg, int commandArgument) {
        if (this.command.length <= commandArgument) {
            return Collections.emptyList();
        }
        return arg.suggest(this, new CommandArgumentContext<>(arg, commandArgument, true, this.command));
    }
}

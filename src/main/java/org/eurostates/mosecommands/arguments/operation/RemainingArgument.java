package org.eurostates.mosecommands.arguments.operation;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class RemainingArgument<T> implements CommandArgument<List<T>> {

    private final @NotNull String id;
    private final @NotNull List<CommandArgument<T>> argument;

    @Deprecated
    public RemainingArgument(@NotNull String id) {
        throw new RuntimeException("Remaining arguments require arguments");
    }

    @SafeVarargs
    public RemainingArgument(@NotNull String id, CommandArgument<T>... argument) {
        this(id, Arrays.asList(argument));
    }

    public RemainingArgument(@NotNull String id, @NotNull Collection<CommandArgument<T>> argument) {
        if (argument.isEmpty()) {
            throw new IllegalArgumentException("Remaining Argument cannot have a argument of empty");
        }
        this.id = id;
        this.argument = new ArrayList<>(argument);
    }

    private @NotNull Map.Entry<T, Integer> parseAny(@NotNull CommandContext context, CommandArgumentContext<?> caContext) throws IOException {
        IOException e1 = null;
        for (int A = 0; A < this.argument.size(); A++) {
            try {
                CommandArgumentContext<T> argumentContext = new CommandArgumentContext<>(this.argument.get(A), caContext);
                return this.argument.get(A).parse(context, argumentContext);
            } catch (IOException e) {
                if (A == 0) {
                    e1 = e;
                }
            }
        }
        if (e1 == null) {
            //shouldnt be possible
            throw new IOException("Unknown error occurred");
        }
        throw e1;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull Map.Entry<List<T>, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<List<T>> argument) throws IOException {
        int A = argument.getFirstArgument();
        List<T> list = new ArrayList<>();
        while (A < context.getCommand().length) {
            Map.Entry<T, Integer> entry = parseAny(context, argument);
            A = entry.getValue();
            list.add(entry.getKey());
        }
        return new AbstractMap.SimpleImmutableEntry<>(list, A);
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext context, @NotNull CommandArgumentContext<List<T>> argument) {
        int A = argument.getFirstArgument();
        while (A < context.getCommand().length) {
            Map.Entry<T, Integer> entry;
            try {
                entry = parseAny(context, argument);
            } catch (IOException e) {
                List<String> list = new ArrayList<>();
                for (CommandArgument<T> arg : this.argument) {
                    CommandArgumentContext<T> argumentContext = new CommandArgumentContext<>(arg, A, true, context.getCommand());
                    list.addAll(arg.suggest(context, argumentContext));
                }
                return list;
            }
            A = entry.getValue();
        }
        return Collections.emptyList();
    }
}
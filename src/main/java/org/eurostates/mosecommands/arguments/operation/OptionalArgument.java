package org.eurostates.mosecommands.arguments.operation;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.ParseCommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class OptionalArgument<T> implements CommandArgument<T> {

    public static class WrappedParser<T> implements ParseCommandArgument<T> {

        private final @NotNull T value;

        public WrappedParser(@NotNull T value) {
            this.value = value;
        }

        @Override
        public @NotNull Map.Entry<T, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) {
            return new AbstractMap.SimpleImmutableEntry<>(this.value, 0);
        }
    }

    private final @NotNull CommandArgument<T> arg;
    private final @NotNull ParseCommandArgument<T> value;

    public OptionalArgument(@NotNull CommandArgument<T> arg, @NotNull T value) {
        this(arg, new WrappedParser<>(value));
    }

    public OptionalArgument(@NotNull CommandArgument<T> arg, @NotNull ParseCommandArgument<T> value) {
        this.arg = arg;
        this.value = value;
    }

    public @NotNull CommandArgument<T> getOriginalArgument() {
        return this.arg;
    }

    @Override
    public @NotNull String getId() {
        return this.arg.getId();
    }

    @Override
    public @NotNull Map.Entry<T, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) throws IOException {
        if (context.getCommand().length == argument.getFirstArgument()) {
            return new AbstractMap.SimpleImmutableEntry<>(this.value.parse(context, argument).getKey(), argument.getFirstArgument());
        }
        try {
            return this.arg.parse(context, argument);
        } catch (IOException e) {
            return new AbstractMap.SimpleImmutableEntry<>(this.value.parse(context, argument).getKey(), argument.getFirstArgument());
        }
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<T> argument) {
        return this.arg.suggest(commandContext, argument);
    }

    @Override
    public @NotNull String getUsage() {
        String original = this.getOriginalArgument().getUsage();
        return "[" + original.substring(1, original.length() - 1) + "]";
    }
}

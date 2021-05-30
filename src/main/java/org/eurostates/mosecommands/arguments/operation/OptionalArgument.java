package org.eurostates.mosecommands.arguments.operation;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.ParseCommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class OptionalArgument<T> implements CommandArgument<T> {

    public static class WrappedParser<T> implements ParseCommandArgument<T> {

        private final T value;

        public WrappedParser(T value) {
            this.value = value;
        }

        @Override
        public Map.Entry<T, Integer> parse(CommandContext context, CommandArgumentContext<T> argument) {
            return new AbstractMap.SimpleImmutableEntry<>(this.value, 0);
        }
    }

    private final CommandArgument<T> arg;
    private final ParseCommandArgument<T> value;

    public OptionalArgument(CommandArgument<T> arg, T value) {
        this(arg, new WrappedParser<>(value));
    }

    public OptionalArgument(CommandArgument<T> arg, ParseCommandArgument<T> value) {
        this.arg = arg;
        this.value = value;
    }

    public CommandArgument<T> getOriginalArgument() {
        return this.arg;
    }

    @Override
    public String getId() {
        return this.arg.getId();
    }

    @Override
    public Map.Entry<T, Integer> parse(CommandContext context, CommandArgumentContext<T> argument) throws IOException {
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
    public List<String> suggest(CommandContext commandContext, CommandArgumentContext<T> argument) {
        return this.arg.suggest(commandContext, argument);
    }

    @Override
    public String getUsage() {
        String original = this.getOriginalArgument().getUsage();
        return "[" + original.substring(1, original.length() - 1) + "]";
    }
}

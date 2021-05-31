package org.eurostates.mosecommands.arguments.operation;

import org.eurostates.lamda.throwable.single.ThrowableFunction;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class MappedArgument<T, J> implements CommandArgument<T> {

    private final CommandArgument<J> commandArgument;
    private final ThrowableFunction<J, T, IOException> convert;

    public MappedArgument(CommandArgument<J> commandArgument, ThrowableFunction<J, T, IOException> convert){
        this.commandArgument = commandArgument;
        this.convert = convert;
    }

    @Override
    public String getId() {
        return this.commandArgument.getId();
    }

    @Override
    public Map.Entry<T, Integer> parse(CommandContext context, CommandArgumentContext<T> argument) throws IOException {
        CommandArgumentContext<J> argContext = new CommandArgumentContext<>(this.commandArgument, argument.getFirstArgument(), context.getCommand());
        Map.Entry<J, Integer> entry = this.commandArgument.parse(context, argContext);
        return new AbstractMap.SimpleImmutableEntry<>(this.convert.apply(entry.getKey()), entry.getValue());
    }

    @Override
    public List<String> suggest(CommandContext context, CommandArgumentContext<T> argument) {
        CommandArgumentContext<J> argContext = new CommandArgumentContext<>(this.commandArgument, argument.getFirstArgument(), context.getCommand());
        return this.commandArgument.suggest(context, argContext);
    }
}
package org.eurostates.mosecommands.arguments.operation;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectedRemainingArgument<T, R> implements CommandArgument<R> {

    private Collector<T, ?, R> collectors;
    private RemainingArgument<T> argument;

    public CollectedRemainingArgument(Collector<T, ?, R> collector, String id, CommandArgument<T>... arguments) {
        this(collector, new RemainingArgument<>(id, arguments));
    }

    public CollectedRemainingArgument(Collector<T, ?, R> collector, RemainingArgument<T> argument) {
        this.collectors = collector;
        this.argument = argument;
    }

    @Override
    public String getId() {
        return this.argument.getId();
    }

    @Override
    public Map.Entry<R, Integer> parse(CommandContext context, CommandArgumentContext<R> argument) throws IOException {
        Map.Entry<List<T>, Integer> parsed = this.argument.parse(context, new CommandArgumentContext<>(this.argument, argument.getFirstArgument(), context.getCommand()));
        R collected = parsed.getKey().stream().collect(this.collectors);
        return new AbstractMap.SimpleImmutableEntry<>(collected, parsed.getValue());
    }

    @Override
    public List<String> suggest(CommandContext commandContext, CommandArgumentContext<R> argument) {
        return this.argument.suggest(commandContext, new CommandArgumentContext<>(this.argument, argument.getFirstArgument(), commandContext.getCommand()));
    }

    public static CollectedRemainingArgument<CharSequence, String> collectStrings(String id, CharSequence joining, CommandArgument<CharSequence>... arguments) {
        return new CollectedRemainingArgument<>(Collectors.joining(joining), id, arguments);
    }
}

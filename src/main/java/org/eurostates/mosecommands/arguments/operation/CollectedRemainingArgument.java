package org.eurostates.mosecommands.arguments.operation;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectedRemainingArgument<T, R> implements CommandArgument<R> {

    private Collector<T, ?, R> collectors;
    private RemainingArgument<T> argument;

    @Deprecated
    public CollectedRemainingArgument(@NotNull Collector<T, ?, R> collector, @NotNull String id) {
        throw new RuntimeException("Collected Remaining Arguments requires arguments");
    }

    public CollectedRemainingArgument(@NotNull Collector<T, ?, R> collector, @NotNull String id, CommandArgument<T>... arguments) {
        this(collector, new RemainingArgument<>(id, arguments));
    }

    public CollectedRemainingArgument(@NotNull Collector<T, ?, R> collector, @NotNull String id, Collection<CommandArgument<T>> arguments) {
        this(collector, new RemainingArgument<>(id, arguments));
    }

    public CollectedRemainingArgument(@NotNull Collector<T, ?, R> collector, @NotNull RemainingArgument<T> argument) {
        this.collectors = collector;
        this.argument = argument;
    }

    @Override
    public @NotNull String getId() {
        return this.argument.getId();
    }

    @Override
    public @NotNull Map.Entry<R, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<R> argument) throws IOException {
        Map.Entry<List<T>, Integer> parsed = this.argument.parse(context, new CommandArgumentContext<>(this.argument, argument));
        R collected = parsed.getKey().stream().collect(this.collectors);
        return new AbstractMap.SimpleImmutableEntry<>(collected, parsed.getValue());
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<R> argument) {
        return this.argument.suggest(commandContext, new CommandArgumentContext<>(this.argument, argument.getFirstArgument(), true, commandContext.getCommand()));
    }

    public static @NotNull CollectedRemainingArgument<CharSequence, String> collectString(@NotNull String id, @NotNull CharSequence joining, CommandArgument<String>... arguments) {
        return collectString(id, joining, Arrays.asList(arguments));
    }

    public static @NotNull CollectedRemainingArgument<CharSequence, String> collectString(@NotNull String id, @NotNull CharSequence joining, Collection<CommandArgument<String>> arguments) {
        Set<CommandArgument<CharSequence>> mapped = arguments
                .parallelStream()
                .map(arg -> new MappedArgument<>(arg, charS -> (CharSequence) charS))
                .collect(Collectors.toSet());
        return collectCharSequence(id, joining, mapped);
    }

    public static @NotNull CollectedRemainingArgument<CharSequence, String> collectCharSequence(@NotNull String id, @NotNull CharSequence joining, CommandArgument<CharSequence>... arguments) {
        return collectCharSequence(id, joining, Arrays.asList(arguments));
    }

    public static @NotNull CollectedRemainingArgument<CharSequence, String> collectCharSequence(@NotNull String id, @NotNull CharSequence joining, Collection<CommandArgument<CharSequence>> arguments) {
        return new CollectedRemainingArgument<>(Collectors.joining(joining), id, arguments);
    }
}

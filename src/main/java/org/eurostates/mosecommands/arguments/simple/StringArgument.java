package org.eurostates.mosecommands.arguments.simple;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StringArgument implements CommandArgument<String> {

    private final @NotNull String id;

    public StringArgument(@NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull Map.Entry<String, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<String> argument) throws IOException {
        String text = context.getCommand()[argument.getFirstArgument()];
        return new AbstractMap.SimpleImmutableEntry<>(text, argument.getFirstArgument() + 1);
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<String> argument) {
        return Collections.emptyList();
    }
}

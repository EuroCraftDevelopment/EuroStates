package org.eurostates.mosecommands.arguments;

import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

public interface ParseCommandArgument<T> {

    @NotNull Map.Entry<T, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) throws IOException;

}

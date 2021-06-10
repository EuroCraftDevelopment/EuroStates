package org.eurostates.mosecommands.arguments.operation;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.ParseCommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PreArgument<T> implements CommandArgument<T> {

    private final CommandArgument<T> wrapper;
    private final ParseCommandArgument<Boolean> success;

    public PreArgument(CommandArgument<T> wrapper, ParseCommandArgument<Boolean> success) {
        this.wrapper = wrapper;
        this.success = success;
    }

    @Override
    public @NotNull String getId() {
        return this.wrapper.getId();
    }

    @Override
    public Map.@NotNull Entry<T, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) throws IOException {
        if (this.success.parse(context, new CommandArgumentContext<>(this.success, argument)).getKey()) {
            return this.wrapper.parse(context, argument);
        }
        throw new IOException("Unknown Error (Developer didn't specify");
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<T> argument) {
        return this.wrapper.suggest(commandContext, argument);
    }
}

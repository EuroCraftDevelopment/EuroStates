package org.eurostates.mosecommands.arguments.operation;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.SuggestCommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SuggestionArgument<T> implements CommandArgument<T> {

    private final @NotNull CommandArgument<T> argument;
    private final @NotNull SuggestCommandArgument<T> extraArguments;
    private final boolean overrideOriginal;

    public SuggestionArgument(@NotNull CommandArgument<T> argument, @NotNull SuggestCommandArgument<T> extraArguments, boolean overrideOriginal) {
        this.argument = argument;
        this.extraArguments = extraArguments;
        this.overrideOriginal = overrideOriginal;

    }

    @Override
    public @NotNull String getId() {
        return this.argument.getId();
    }

    @Override
    public @NotNull Map.Entry<T, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) throws IOException {
        return this.argument.parse(context, argument);
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<T> argument) {
        List<String> suggestions = new ArrayList<>();
        if (!overrideOriginal) {
            suggestions.addAll(this.argument.suggest(commandContext, argument));
        }
        suggestions.addAll(this.extraArguments.suggest(commandContext, argument));
        return suggestions;
    }
}

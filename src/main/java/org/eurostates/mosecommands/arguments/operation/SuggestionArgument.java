package org.eurostates.mosecommands.arguments.operation;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SuggestionArgument<T> implements CommandArgument<T> {

    private final CommandArgument<T> argument;
    private final SuggestionArgument<T> extraArguments;
    private final boolean overrideOriginal;

    public SuggestionArgument(CommandArgument<T> argument, SuggestionArgument<T> extraArguments, boolean overrideOriginal) {
        this.argument = argument;
        this.extraArguments = extraArguments;
        this.overrideOriginal = overrideOriginal;

    }

    @Override
    public String getId() {
        return this.argument.getId();
    }

    @Override
    public Map.Entry<T, Integer> parse(CommandContext context, CommandArgumentContext<T> argument) throws IOException {
        return this.argument.parse(context, argument);
    }

    @Override
    public List<String> suggest(CommandContext commandContext, CommandArgumentContext<T> argument) {
        List<String> suggestions = new ArrayList<>();
        if (!overrideOriginal) {
            suggestions.addAll(this.argument.suggest(commandContext, argument));
        }
        suggestions.addAll(this.extraArguments.suggest(commandContext, argument));
        return suggestions;
    }
}

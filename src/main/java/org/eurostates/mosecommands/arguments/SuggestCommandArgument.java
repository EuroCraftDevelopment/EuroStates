package org.eurostates.mosecommands.arguments;

import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SuggestCommandArgument<T> {

    @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<T> argument);


}

package org.eurostates.mosecommands.arguments;

import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;

import java.util.List;

public interface SuggestCommandArgument<T> {

    List<String> suggest(CommandContext commandContext, CommandArgumentContext<T> argument);


}

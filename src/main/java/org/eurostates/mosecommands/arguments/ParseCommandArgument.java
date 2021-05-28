package org.eurostates.mosecommands.arguments;

import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;

import java.io.IOException;
import java.util.Map;

public interface ParseCommandArgument<T> {

    Map.Entry<T, Integer> parse(CommandContext context, CommandArgumentContext<T> argument) throws IOException;

}

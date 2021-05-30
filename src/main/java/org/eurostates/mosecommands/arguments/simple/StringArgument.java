package org.eurostates.mosecommands.arguments.simple;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StringArgument implements CommandArgument<String> {
    private final String id;

    public StringArgument(String id){
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Map.Entry<String, Integer> parse(CommandContext context, CommandArgumentContext<String> argument) throws IOException {
        String text = context.getCommand()[argument.getFirstArgument()];
        return new AbstractMap.SimpleImmutableEntry<>(text, argument.getFirstArgument() + 1);
    }

    @Override
    public List<String> suggest(CommandContext commandContext, CommandArgumentContext<String> argument) {
        return Collections.emptyList();
    }
}

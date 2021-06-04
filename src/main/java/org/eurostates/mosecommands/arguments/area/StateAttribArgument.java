package org.eurostates.mosecommands.arguments.area;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

@Deprecated
public class StateAttribArgument implements CommandArgument<String> {

    private final @NotNull String id;

    List<String> attribs = new ArrayList<>(Arrays.asList(
            "userprefix"
    ));

    public StateAttribArgument(@NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull Map.Entry<String, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<String> argument) throws IOException {
        String text = context.getCommand()[argument.getFirstArgument()];
        if (!attribs.contains(text)) throw new IOException("No such attribute exists for a state.");
        return new AbstractMap.SimpleImmutableEntry<>(text, argument.getFirstArgument() + 1);
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<String> argument) {
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
        List<String> list = new ArrayList<>();

        attribs.forEach(attrib -> {
            if (attrib.startsWith(peek.toLowerCase())) list.add(attrib);
        });

        return list;
    }
}

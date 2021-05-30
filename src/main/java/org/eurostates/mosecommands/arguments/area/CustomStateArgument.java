package org.eurostates.mosecommands.arguments.area;

import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.States;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CustomStateArgument implements CommandArgument<CustomState> {

    private final String id;

    public CustomStateArgument(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Map.Entry<CustomState, Integer> parse(CommandContext context, CommandArgumentContext<CustomState> argument) throws IOException {
        String arg = context.getCommand()[argument.getFirstArgument()];
        CustomState customState = States
                .CUSTOM_STATES
                .stream()
                .filter(state -> {
                    if (state.getName().equalsIgnoreCase(arg)) {
                        return true;
                    }
                    return state.getTag().equalsIgnoreCase(arg);
                })
                .findAny()
                .orElseThrow(() -> new IOException("Unknown state"));
        return new AbstractMap.SimpleImmutableEntry<>(customState, argument.getFirstArgument() + 1);
    }

    @Override
    public List<String> suggest(CommandContext commandContext, CommandArgumentContext<CustomState> argument) {
        List<String> list = new ArrayList<>();
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
        States.CUSTOM_STATES.forEach(customState -> {
            list.add(customState.getTag());
            list.add(customState.getName());
        });
        list.sort(Comparator.naturalOrder());
        return list
                .stream()
                .filter(str -> str.toLowerCase().startsWith(peek.toLowerCase()))
                .collect(Collectors.toList());
    }
}

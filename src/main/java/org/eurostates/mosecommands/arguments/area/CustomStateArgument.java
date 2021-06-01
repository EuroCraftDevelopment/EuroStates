package org.eurostates.mosecommands.arguments.area;

import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.States;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CustomStateArgument implements CommandArgument<CustomState> {

    private final @NotNull String id;

    public CustomStateArgument(@NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull Map.Entry<CustomState, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<CustomState> argument) throws IOException {
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
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<CustomState> argument) {
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

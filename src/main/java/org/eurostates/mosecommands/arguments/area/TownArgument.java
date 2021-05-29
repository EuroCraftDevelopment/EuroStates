package org.eurostates.mosecommands.arguments.area;

import org.eurostates.area.state.States;
import org.eurostates.area.town.Town;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TownArgument implements CommandArgument<Town> {

    private final String id;

    public TownArgument(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Map.Entry<Town, Integer> parse(CommandContext context, CommandArgumentContext<Town> argument) throws IOException {
        String arg = context.getCommand()[argument.getFirstArgument()];
        Town townResult = States
                .CUSTOM_STATES
                .stream()
                .flatMap(state -> state.getTowns().parallelStream())
                .filter(town -> {
                    if (town.getName().equalsIgnoreCase(arg)) {
                        return true;
                    }
                    return town.getTag().equalsIgnoreCase(arg);
                })
                .findAny()
                .orElseThrow(() -> new IOException("Unknown town"));
        return new AbstractMap.SimpleImmutableEntry<>(townResult, argument.getFirstArgument() + 1);
    }

    @Override
    public List<String> suggest(CommandContext commandContext, CommandArgumentContext<Town> argument) {
        List<String> list = new ArrayList<>();
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
        States
                .CUSTOM_STATES
                .stream()
                .flatMap(state -> state
                        .getTowns()
                        .parallelStream())
                .forEach(customState -> {
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

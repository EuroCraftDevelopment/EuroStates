package org.eurostates.mosecommands.arguments.area;

import org.eurostates.area.technology.Technologies;
import org.eurostates.area.technology.Technology;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TechnologyArgument implements CommandArgument<Technology> {

    private final @NotNull String id;

    public TechnologyArgument(@NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull Map.Entry<Technology, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<Technology> argument) throws IOException {
        String arg = context.getCommand()[argument.getFirstArgument()];
        Technology technology = Technologies
                .TECHNOLOGIES
                .stream()
                .filter(tech -> {
                    if (tech.getName().equalsIgnoreCase(arg)) {
                        return true;
                    }
                    return tech.getIdentifier().equalsIgnoreCase(arg);
                })
                .findAny()
                .orElseThrow(() -> new IOException("Unknown technology"));
        return new AbstractMap.SimpleImmutableEntry<>(technology, argument.getFirstArgument() + 1);
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<Technology> argument) {
        List<String> list = new ArrayList<>();
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
        Technologies.TECHNOLOGIES.forEach(technology -> {
            list.add(technology.getIdentifier());
        });
        list.sort(Comparator.naturalOrder());
        return list
                .stream()
                .filter(str -> str.toLowerCase().startsWith(peek.toLowerCase()))
                .collect(Collectors.toList());
    }
}

package org.eurostates.mosecommands.arguments.area;

import org.bukkit.command.CommandSender;
import org.eurostates.area.state.States;
import org.eurostates.area.town.Town;
import org.eurostates.lamda.throwable.bi.ThrowableBiConsumer;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.eurostates.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TownArgument implements CommandArgument<Town> {

    private final @NotNull String id;
    private final @NotNull ThrowableBiConsumer<CommandSender, Town, IOException> finalCheck;

    public TownArgument(@NotNull String id) {
        this(id, (sender, town) -> {
        });
    }

    public TownArgument(@NotNull String id, @NotNull ThrowableBiConsumer<CommandSender, Town, IOException> function) {
        this.id = id;
        this.finalCheck = function;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull Map.Entry<Town, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<Town> argument) throws IOException {
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
        this.finalCheck.consume(context.getSource(), townResult);
        return new AbstractMap.SimpleImmutableEntry<>(townResult, argument.getFirstArgument() + 1);
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<Town> argument) {
        List<String> list = new ArrayList<>();
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
        States
                .CUSTOM_STATES
                .stream()
                .flatMap(state -> state
                        .getTowns()
                        .parallelStream())
                .filter(customTown -> Utils.throwOr(IOException.class, () -> {
                    this.finalCheck.consume(commandContext.getSource(), customTown);
                    return true;
                }, false))
                .forEach(customState -> {
                    list.add(customState.getTag());
                    list.add(customState.getName());
                });
        list.sort(Comparator.naturalOrder());
        return list
                .parallelStream()
                .filter(str -> str.toLowerCase().startsWith(peek.toLowerCase()))
                .collect(Collectors.toList());
    }
}

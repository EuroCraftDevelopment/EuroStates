package org.eurostates.mosecommands.arguments.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LocationArgument implements CommandArgument<Location> {

    private final String id;
    private final CommandArgument<Double> xArg;
    private final CommandArgument<Double> yArg;
    private final CommandArgument<Double> zArg;
    private final CommandArgument<World> worldArg;

    public LocationArgument(String id, CommandArgument<Double> x, CommandArgument<Double> y, CommandArgument<Double> z, CommandArgument<World> world) {
        this.id = id;
        this.xArg = x;
        this.yArg = y;
        this.zArg = z;
        this.worldArg = world;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public Map.@NotNull Entry<Location, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<Location> argument) throws IOException {
        Map.Entry<Double, Integer> xResult = this.xArg.parse(context, new CommandArgumentContext<>(this.xArg, argument));
        Map.Entry<Double, Integer> yResult = this.yArg.parse(context, new CommandArgumentContext<>(this.yArg, xResult.getValue(), argument.isAsSuggestion(), context.getCommand()));
        Map.Entry<Double, Integer> zResult = this.zArg.parse(context, new CommandArgumentContext<>(this.zArg, yResult.getValue(), argument.isAsSuggestion(), context.getCommand()));
        Map.Entry<World, Integer> worldResult = this.worldArg.parse(context, new CommandArgumentContext<>(this.worldArg, zResult.getValue(), argument.isAsSuggestion(), context.getCommand()));
        Location location = new Location(worldResult.getKey(), xResult.getKey(), yResult.getKey(), zResult.getKey());
        return new AbstractMap.SimpleImmutableEntry<>(location, worldResult.getValue());
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<Location> argument) {
        int arg = argument.getFirstArgument();
        String[] args = commandContext.getCommand();
        for (int A = 0; A < 4; A++) {
            int argInt = arg + A;
            if (args.length == (argInt + 1)) {
                CommandArgument<?> commandArg = new CommandArgument[]{this.xArg, this.yArg, this.zArg, this.worldArg}[A];
                return suggest(commandArg, commandContext, argInt, argument.isAsSuggestion());
            }
        }
        return Collections.emptyList();
    }

    private <T> List<String> suggest(CommandArgument<T> argument, CommandContext commandContext, int A, boolean isSuggestion) {
        return argument.suggest(commandContext, new CommandArgumentContext<>(argument, A, isSuggestion, commandContext.getCommand()));
    }
}

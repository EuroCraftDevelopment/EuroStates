package org.eurostates.mosecommands.arguments.location;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BlockLocationArgument implements CommandArgument<Block> {

    private final String id;
    private final CommandArgument<Integer> xArg;
    private final CommandArgument<Integer> yArg;
    private final CommandArgument<Integer> zArg;
    private final CommandArgument<World> worldArg;

    public BlockLocationArgument(String id, CommandArgument<Integer> x, CommandArgument<Integer> y, CommandArgument<Integer> z, CommandArgument<World> world) {
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
    public @NotNull Map.Entry<Block, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<Block> argument) throws IOException {
        Map.Entry<Integer, Integer> xResult = this.xArg.parse(context, new CommandArgumentContext<>(this.xArg, argument.getFirstArgument(), context.getCommand()));
        Map.Entry<Integer, Integer> yResult = this.yArg.parse(context, new CommandArgumentContext<>(this.yArg, xResult.getValue(), context.getCommand()));
        Map.Entry<Integer, Integer> zResult = this.zArg.parse(context, new CommandArgumentContext<>(this.zArg, yResult.getValue(), context.getCommand()));
        Map.Entry<World, Integer> worldResult = this.worldArg.parse(context, new CommandArgumentContext<>(this.worldArg, zResult.getValue(), context.getCommand()));
        Block block = worldResult.getKey().getBlockAt(xResult.getKey(), yResult.getKey(), zResult.getKey());
        return new AbstractMap.SimpleImmutableEntry<>(block, worldResult.getValue());
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<Block> argument) {
        int arg = argument.getFirstArgument();
        String[] args = commandContext.getCommand();
        for (int A = 0; A < 4; A++) {
            int argInt = arg + A;
            if (args.length == (argInt + 1)) {
                CommandArgument<?> commandArg = new CommandArgument[]{this.xArg, this.yArg, this.zArg, this.worldArg}[A];
                return suggest(commandArg, commandContext, argInt);
            }
        }
        return Collections.emptyList();
    }

    private <T> List<String> suggest(CommandArgument<T> argument, CommandContext commandContext, int A) {
        return argument.suggest(commandContext, new CommandArgumentContext<>(argument, A, commandContext.getCommand()));
    }
}

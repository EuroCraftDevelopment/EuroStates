package org.eurostates.mosecommands.arguments.location;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorldArgument implements CommandArgument<World> {

    private final String id;

    public WorldArgument(String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull Map.Entry<World, Integer> parse(CommandContext context, CommandArgumentContext<World> argument) throws IOException {
        String worldName = context.getCommand()[argument.getFirstArgument()];
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            return new AbstractMap.SimpleImmutableEntry<>(world, argument.getFirstArgument() + 1);
        }
        if (!argument.isAsSuggestion() && context.getSource() instanceof Player) {
            Player player = (Player) context.getSource();
            return new AbstractMap.SimpleImmutableEntry<>(player.getWorld(), argument.getFirstArgument());
        }
        if (!argument.isAsSuggestion() && context.getSource() instanceof CommandBlock) {
            CommandBlock block = (CommandBlock) context.getSource();
            return new AbstractMap.SimpleImmutableEntry<>(block.getWorld(), argument.getFirstArgument());
        }
        throw new IOException("Unknown world name of '" + worldName + "'");
    }

    @Override
    public @NotNull List<String> suggest(CommandContext commandContext, CommandArgumentContext<World> argument) {
        String worldPeek = commandContext.getCommand()[argument.getFirstArgument()];
        return Bukkit
                .getWorlds()
                .stream()
                .map(World::getName)
                .filter(n -> n.toLowerCase().startsWith(worldPeek))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }
}

package org.eurostates.mosecommands.commands.town.admin;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.town.Town;
import org.eurostates.dynmap.MarkerSetManager;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.ParseCommandArgument;
import org.eurostates.mosecommands.arguments.SuggestCommandArgument;
import org.eurostates.mosecommands.arguments.area.CustomStateArgument;
import org.eurostates.mosecommands.arguments.location.BlockLocationArgument;
import org.eurostates.mosecommands.arguments.location.WorldArgument;
import org.eurostates.mosecommands.arguments.operation.CollectionArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.operation.OptionalArgument;
import org.eurostates.mosecommands.arguments.operation.SuggestionArgument;
import org.eurostates.mosecommands.arguments.simple.NumberArgument;
import org.eurostates.mosecommands.arguments.simple.StringArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.eurostates.parser.Parsers;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class TownCreateCommand implements ArgumentCommand {
    public static final ExactArgument ADMIN_ARGUMENT = new ExactArgument("admin");
    public static final ExactArgument CREATE_ARGUMENT = new ExactArgument("create");
    public static final CustomStateArgument STATE_ARGUMENT = new CustomStateArgument("state");
    public static final StringArgument TOWNNAME_ARGUMENT = new StringArgument("townname");
    public static final CollectionArgument<OfflinePlayer> MAYOR_ARGUMENT = CollectionArgument.getAsOfflinePlayer("mayor");
    public static final OptionalArgument<Block> BLOCK_LOCATION_ARGUMENT = new OptionalArgument<>(new BlockLocationArgument(
            "centre",
            createBlockArg(NumberArgument.asInt("x"), Location::getBlockX),
            createBlockArg(NumberArgument.asInt("y"), Location::getBlockY),
            createBlockArg(NumberArgument.asInt("z"), Location::getBlockZ),
            new OptionalArgument<World>(new WorldArgument("world"), (context, argument) -> {
                if (context.getSource() instanceof Player) {
                    World world = ((Player) context.getSource()).getLocation().getWorld();
                    return new AbstractMap.SimpleImmutableEntry<>(world, argument.getFirstArgument());
                }
                if (context.getSource() instanceof CommandBlock) {
                    World world = ((Player) context.getSource()).getLocation().getWorld();
                    return new AbstractMap.SimpleImmutableEntry<>(world, argument.getFirstArgument());
                }
                throw new IOException("Cannot get your location, please provide one");
            })
    ), new ParseCommandArgument<Block>() {
        @Override
        public @NotNull Map.Entry<Block, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<Block> argument) throws IOException {
            if (context.getSource() instanceof Player) {
                Block block = ((Player) context.getSource()).getLocation().getBlock();
                return new AbstractMap.SimpleImmutableEntry<>(block, argument.getFirstArgument());
            }
            if (context.getSource() instanceof CommandBlock) {
                Block block = ((Player) context.getSource()).getLocation().getBlock();
                return new AbstractMap.SimpleImmutableEntry<>(block, argument.getFirstArgument());
            }
            throw new IOException("Cannot get your location, please provide one");
        }
    });

    private static <T> CommandArgument<T> createBlockArg(CommandArgument<T> base, Function<Location, T> function) {
        SuggestCommandArgument<T> suggestions = (commandContext, argument) -> {
            @NotNull CommandSender source = commandContext.getSource();
            if (source instanceof Player) {
                return Collections.singletonList(function.apply(((Player) source).getLocation()).toString());
            }
            if (source instanceof CommandBlock) {
                return Collections.singletonList(function.apply(((CommandBlock) source).getLocation()).toString());
            }
            return Collections.emptyList();
        };
        ParseCommandArgument<T> parse = (context, argument) -> {
            @NotNull CommandSender source = context.getSource();
            if (source instanceof Player) {
                T position = function.apply(((Player) source).getLocation());
                return new AbstractMap.SimpleImmutableEntry<>(position, argument.getFirstArgument());
            }
            if (source instanceof CommandBlock) {
                T position = function.apply(((CommandBlock) source).getLocation());
                return new AbstractMap.SimpleImmutableEntry<>(position, argument.getFirstArgument());
            }
            throw new IOException("Cannot get your location, please provide one");
        };
        return new OptionalArgument<>(new SuggestionArgument<>(base, suggestions, true), parse);
    }

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                ADMIN_ARGUMENT,
                CREATE_ARGUMENT,
                STATE_ARGUMENT,
                TOWNNAME_ARGUMENT,
                MAYOR_ARGUMENT,
                BLOCK_LOCATION_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.of("eurostates.admin");
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        String townName = context.getArgument(this, TOWNNAME_ARGUMENT);
        OfflinePlayer mayor = context.getArgument(this, MAYOR_ARGUMENT);
        CustomState state = context.getArgument(this, STATE_ARGUMENT);
        Block block = context.getArgument(this, BLOCK_LOCATION_ARGUMENT);

        if (!(mayor.hasPlayedBefore())) { // Check if leader joined server before
            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                    ChatColor.RED + "The player has not played on the server before.");
            return true;
        }

        UUID id = UUID.randomUUID();
        String townTag = townName.substring(0, 4).toUpperCase();
        ESUser mayorUser = Parsers.GETTER_USER.fromId(mayor.getUniqueId());

        Town newTown = new Town(id, townTag, townName, mayorUser.getOwnerId(), state, block);

        try {
            newTown.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        state.getTowns().add(newTown);

        try {
            state.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MarkerSetManager.addTownMarker(newTown);

        Bukkit.broadcastMessage(
                ChatColor.BLUE + "[EuroStates] " + ChatColor.WHITE +
                        townName + " has been formed with the mayor as " + mayor.getName() + "!"
        );

        return true;
    }
}

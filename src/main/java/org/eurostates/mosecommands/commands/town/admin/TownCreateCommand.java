package org.eurostates.mosecommands.commands.town.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.town.CustomTown;
import org.eurostates.area.town.Town;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.area.CustomStateArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.simple.StringArgument;
import org.eurostates.mosecommands.arguments.source.OfflinePlayerArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.eurostates.parser.Parsers;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class TownCreateCommand implements ArgumentCommand {
    public static final ExactArgument ADMIN_ARGUMENT = new ExactArgument("admin");
    public static final ExactArgument CREATE_ARGUMENT = new ExactArgument("create");
    public static final CustomStateArgument STATE_ARGUMENT = new CustomStateArgument("state");
    public static final StringArgument TOWNNAME_ARGUMENT = new StringArgument("townname");
    public static final OfflinePlayerArgument MAYOR_ARGUMENT = new OfflinePlayerArgument("mayor");

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                ADMIN_ARGUMENT,
                CREATE_ARGUMENT,
                STATE_ARGUMENT,
                TOWNNAME_ARGUMENT,
                MAYOR_ARGUMENT
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

        if (!(context.getSource() instanceof Player)) {
            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                    ChatColor.RED + "This cannot be executed in the console.");
            return true;
        }

        if (!(mayor.hasPlayedBefore())) { // Check if leader joined server before
            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                    ChatColor.RED + "The player has not played on the server before.");
            return true;
        }

        UUID id = UUID.randomUUID();
        String townTag = townName.substring(0, 4).toUpperCase();
        ESUser mayorUser = Parsers.GETTER_USER.fromId(mayor.getUniqueId());

        Player sourcePlayer = (Player) context.getSource();
        Block temporaryCenter = sourcePlayer.getLocation().getBlock();
        CustomTown newTown = new CustomTown(id, townTag, townName, mayorUser.getOwnerId(), state, temporaryCenter);

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


        Bukkit.broadcastMessage(
                ChatColor.BLUE + "[EuroStates] " + ChatColor.WHITE +
                        townName + " has been formed with the mayor as " + mayor.getName() + "!"
        );

        return true;
    }
}

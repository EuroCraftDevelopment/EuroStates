package org.eurostates.mosecommands.commands.town.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.town.CustomTown;
import org.eurostates.area.town.Town;
import org.eurostates.dynmap.MarkerSetManager;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.area.TownArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class TownDeleteCommand implements ArgumentCommand {
    public static final ExactArgument ADMIN_ARGUMENT = new ExactArgument("admin");
    public static final ExactArgument DELETE_ARGUMENT = new ExactArgument("delete");
    public static final TownArgument TOWN_ARGUMENT = new TownArgument("town");

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                ADMIN_ARGUMENT,
                DELETE_ARGUMENT,
                TOWN_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.of("eurostates.admin");
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        Town town = context.getArgument(this, TOWN_ARGUMENT);
        CustomState state = town.getState();

        state.getTowns().remove(town);

        File file = town.getOwnerUser().getFile();
        boolean didDelete = file.delete();

        if(!didDelete) Bukkit.getLogger().warning("Could not delete state file: " + file.toString());

        try {
            state.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MarkerSetManager.removeTownMarker((CustomTown) town);

        Bukkit.broadcastMessage(ChatColor.BLUE+"[EuroStates] "+
                ChatColor.WHITE+"Town "+town.getName()+" was forcibly disbanded.");

        return true;
    }
}

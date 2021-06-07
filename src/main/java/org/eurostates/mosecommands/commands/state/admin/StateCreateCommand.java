package org.eurostates.mosecommands.commands.state.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.States;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.simple.StringArgument;
import org.eurostates.mosecommands.arguments.source.OfflinePlayerArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.eurostates.parser.Parsers;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class StateCreateCommand implements ArgumentCommand {
    public static final ExactArgument ADMIN_ARGUMENT = new ExactArgument("admin");
    public static final ExactArgument CREATE_ARGUMENT = new ExactArgument("create");
    public static final StringArgument STATENAME_ARGUMENT = new StringArgument("statename");
    public static final OfflinePlayerArgument LEADER_ARGUMENT = new OfflinePlayerArgument("leader");

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                ADMIN_ARGUMENT,
                CREATE_ARGUMENT,
                STATENAME_ARGUMENT,
                LEADER_ARGUMENT,
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.of("eurostates.admin");
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        String stateName = context.getArgument(this, STATENAME_ARGUMENT);
        OfflinePlayer leader = context.getArgument(this, LEADER_ARGUMENT);

        if (!(leader.hasPlayedBefore())) { // Check if leader joined server before
            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                    ChatColor.RED + "The player has not played on the server before.");
            return true;
        }
        UUID id = UUID.randomUUID();
        String stateTag = stateName.substring(0, 3).toUpperCase();
        ESUser leaderUser = Parsers.GETTER_USER.fromId(leader.getUniqueId());

        CustomState newState = new CustomState(id, stateTag, stateName, "" , 'r', leaderUser, stateName);
        States.CUSTOM_STATES.add(newState);

        try {
            newState.save();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Bukkit.broadcastMessage(
                ChatColor.BLUE + "[EuroStates] " + ChatColor.WHITE +
                        stateName + " has been formed with the owner as " + leader.getName() + "!"
        );

        return true;
    }
}

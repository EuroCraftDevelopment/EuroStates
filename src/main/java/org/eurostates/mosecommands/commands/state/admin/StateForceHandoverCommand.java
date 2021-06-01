package org.eurostates.mosecommands.commands.state.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.eurostates.area.state.CustomState;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.area.CustomStateArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.source.OfflinePlayerArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

public class StateForceHandoverCommand implements ArgumentCommand {
    public static final ExactArgument ADMIN_ARGUMENT = new ExactArgument("admin");
    public static final ExactArgument FORCE_HANDOVER_ARGUMENT = new ExactArgument("forcehandover");
    public static final CustomStateArgument STATE_ARGUMENT = new CustomStateArgument("state");
    public static final OfflinePlayerArgument PLAYER_ARGUMENT = new OfflinePlayerArgument("player");

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
            ADMIN_ARGUMENT,
            FORCE_HANDOVER_ARGUMENT,
            STATE_ARGUMENT,
            PLAYER_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.of("eurostates.admin");
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        CustomState customState = context.getArgument(this, STATE_ARGUMENT);
        OfflinePlayer newLeader = context.getArgument(this, PLAYER_ARGUMENT);

        if(!newLeader.hasPlayedBefore()){
            context.getSource().sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.RED+
                    "That player has not been online."); return true;
        }

        customState.setOwner(newLeader);
        try {
            customState.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.broadcastMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.WHITE+
                "The leader of "+customState.getName()+" ("+ customState.getTag() +") has been forcibly changed to "
        + newLeader.getName()+ ".");

        return true;
    }
}

package org.eurostates.mosecommands.commands.state.global;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.NomadState;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.eurostates.parser.Parsers;
import org.eurostates.util.invites.InviteHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

public class StateInviteAcceptCommand implements ArgumentCommand {
    public static final ExactArgument ACCEPT_ARGUMENT = new ExactArgument("accept");

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{ACCEPT_ARGUMENT};
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.empty();
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        if(!(context.getSource() instanceof Player)){
            context.getSource().sendMessage("Can only execute as a player."); return true;
        }

        Player player = (Player) context.getSource();
        ESUser user = Parsers.GETTER_USER.fromId(player.getUniqueId());

        if(!(user.getState() instanceof NomadState)){
            player.sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.RED+
                    "You are already in a state."); return true;
        }

        CustomState state = InviteHandler.INVITES.getStateFromInvited(user);

        if(state==null){
            player.sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.RED+
                    "You do not have any pending invitations."); return true;
        }

        state.getCitizens().add(player);

        Bukkit.broadcastMessage(ChatColor.BLUE + "[EuroStates] "+ChatColor.RESET+
                player.getDisplayName()+" has joined "+state.getName()+"!");

        try {
            state.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}

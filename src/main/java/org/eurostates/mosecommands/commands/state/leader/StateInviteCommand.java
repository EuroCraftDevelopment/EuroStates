package org.eurostates.mosecommands.commands.state.leader;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.NomadState;
import org.eurostates.area.state.State;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.source.OfflinePlayerArgument;
import org.eurostates.mosecommands.arguments.source.PlayerArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.eurostates.parser.Parsers;
import org.eurostates.util.invites.InviteHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class StateInviteCommand implements ArgumentCommand {
    public static final ExactArgument INVITE_ARGUMENT = new ExactArgument("invite");
    public static final PlayerArgument PLAYER_ARGUMENT = new PlayerArgument("player");

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                INVITE_ARGUMENT,
                PLAYER_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.empty();
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        Player invitedPlayer = context.getArgument(this, PLAYER_ARGUMENT);
        ESUser invited = Parsers.GETTER_USER.fromId(invitedPlayer.getUniqueId());

        if(!(context.getSource() instanceof Player)) {
            context.getSource().sendMessage("This can only be run as a player!");
            return true;
        }

        Player inviterPlayer = (Player) context.getSource();
        ESUser inviter = Parsers.GETTER_USER.fromId(inviterPlayer.getUniqueId());

        State invitedState = inviter.getState();

        if(invitedState instanceof NomadState) {
            inviterPlayer.sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.RED+
                    "You are not part of a state."); return true;
        }

        CustomState customInvitedState = (CustomState) invitedState;

        if(!(customInvitedState.getOwnerId().toString().equals(inviterPlayer.getUniqueId().toString()))) {
            inviterPlayer.sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.RED+
                    "You are not the leader of state."); return true;
        }

        CustomState testState = InviteHandler.INVITES.getStateFromInvited(invited);
        if(testState!=null) {
            inviterPlayer.sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.RED+
                    "The player has already a pending invite. Please wait."); return true;
        }

        if(!(invited.getState() instanceof NomadState)) {
            inviterPlayer.sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.RED+
                    "The player already has a state."); return true;
        }

        InviteHandler.INVITES.addInvite(inviter, invited);

        inviterPlayer.sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.RESET+
                "Invite sent.");

        invitedPlayer.sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.RESET+
                inviterPlayer.getDisplayName() + " has invited you to "+customInvitedState.getName()+".");
        invitedPlayer.sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.RESET+
                "Do /states accept to accept the invitation. The invitation will time out in 3 minutes.");
        return true;
    }
}

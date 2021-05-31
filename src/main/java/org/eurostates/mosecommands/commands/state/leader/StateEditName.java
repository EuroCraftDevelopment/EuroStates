package org.eurostates.mosecommands.commands.state.leader;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eurostates.EuroStates;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.NomadState;
import org.eurostates.area.state.State;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.simple.StringArgument;
import org.eurostates.mosecommands.context.CommandContext;

import java.io.IOException;
import java.util.Optional;

public class StateEditName implements ArgumentCommand {

    public static final ExactArgument EDIT_ARGUMENT = new ExactArgument("edit");
    public static final StringArgument NAME_ARGUMENT = new StringArgument("name");

    @Override
    public CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{EDIT_ARGUMENT, NAME_ARGUMENT};
    }

    @Override
    public boolean canRun(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        return EuroStates.getPlugin().getTowns().parallelStream().anyMatch(t -> t.getOwnerId().equals(player.getUniqueId()));
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.empty();
    }

    @Override // this is the funniest shit i have ever seen
    public boolean run(CommandContext context, String[] arg) {
        String newName = context.getArgument(this, NAME_ARGUMENT);
        if (!(context.getSource() instanceof Player)) {
            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " + ChatColor.RED + "Cannot be executed from console.");
            return true;
        }
        Player player = (Player) context.getSource();

        ESUser user = EuroStates.getPlugin().getUser(player.getUniqueId()).orElseGet(() -> new ESUser(player.getUniqueId()));
        State state = user.getState();

        if (state instanceof NomadState) {
            player.sendMessage(ChatColor.BLUE + "[EuroStates] " + ChatColor.RED + "You're not part of a state.");
            return true;
        }

        if (!(state instanceof CustomState)) {
            player.sendMessage(ChatColor.BLUE + "[EuroStates] " + ChatColor.RED + "Unknown state. Tell the admin.");
            return true;
        }

        CustomState customState = (CustomState) state;

        if (customState.getOwner().getUniqueId() != user.getOwnerId()) {
            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " + ChatColor.RED + "You're not the leader of this state.");
            return true;
        }

        String oldName = customState.getName();
        customState.setName(newName);

        context.getSource().sendMessage(
                ChatColor.BLUE + "[EuroStates] " + ChatColor.WHITE + oldName + " (" + customState.getTag() +
                        ") has been renamed to " + newName + ".");
        try {
            customState.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}

package org.eurostates.mosecommands.commands.state.leader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.eurostates.EuroStates;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.NomadState;
import org.eurostates.area.state.State;
import org.eurostates.area.state.States;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.simple.StringArgument;
import org.eurostates.mosecommands.context.CommandContext;

import java.util.Optional;

public class StateEditName implements ArgumentCommand {
    public static final String EDIT_ARGUMENT = "edit";
    public static final String NAME_ARGUMENT = "name";

    @Override
    public CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{new ExactArgument(EDIT_ARGUMENT), new StringArgument(NAME_ARGUMENT)};
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

        Optional<ESUser> user = EuroStates.getPlugin().getUser(((Player) context.getSource()).getUniqueId());

        State state = user.get().getState();

        if (state instanceof NomadState){
            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " + ChatColor.RED + "You're not part of a state.");
            return true;
        }

        if (state.getOwner().getUniqueId() != user.get().getOwnerId()){
            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " + ChatColor.RED + "You're not the leader of this state.");
            return true;
        }

        CustomState customState = (CustomState) state;

        String oldName = customState.getName();
        customState.setName(newName);

        context.getSource().sendMessage(
            ChatColor.BLUE + "[EuroStates] " + ChatColor.WHITE + oldName + " (" + customState.getTag() +
            ") has been renamed to " + newName + ".");

        // how save???????????? burh moment
        customState.save(customState.getFile());

        return true;
    }
}

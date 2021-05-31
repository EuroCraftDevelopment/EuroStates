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
import org.eurostates.mosecommands.arguments.area.StateAttribArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.operation.RemainingArgument;
import org.eurostates.mosecommands.arguments.simple.StringArgument;
import org.eurostates.mosecommands.context.CommandContext;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StateEdit implements ArgumentCommand {
    public static final String EDIT_ARGUMENT = "edit";
    public static final String ATTRIB_ARGUMENT = "attribute";
    public static final String NAME_ARGUMENT = "name";
    public static final String REM_ARGUMENT = "remaining";


    @Override
    public CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                new ExactArgument(EDIT_ARGUMENT),
                new StateAttribArgument(ATTRIB_ARGUMENT),
                new StringArgument(NAME_ARGUMENT),
                new RemainingArgument<>(REM_ARGUMENT, new StringArgument("temp"))};
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.empty();
    }

    @Override
    public boolean run(CommandContext context, String[] arg) {
        // get arguments
        String attrib = context.getArgument(this, ATTRIB_ARGUMENT);
        String newAttrib = context.getArgument(this, NAME_ARGUMENT);
        List<String> remainingArguments = context.getArgument(this, REM_ARGUMENT);

        // check if player is running command
        if (!(context.getSource() instanceof Player)) {
            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " + ChatColor.RED + "Cannot be executed from console.");
            return true;
        }

        Optional<ESUser> user = EuroStates.getPlugin().getUser(((Player) context.getSource()).getUniqueId());

        State state = user.get().getState();

        // check if player is Nomad
        if (!(state instanceof CustomState)){
            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " + ChatColor.RED + "You're not part of a state.");
            return true;
        }

        CustomState customState = (CustomState) state;

        // check if player is leader of nation
        if (customState.getOwnerId() != user.get().getOwnerId()){
            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " + ChatColor.RED + "You're not the leader of this state.");
            return true;
        }


        String stateName = customState.getName();
        String stateTag = customState.getTag();

        // Actual editing part lol

        if(attrib.equalsIgnoreCase("name")){
            if(newAttrib.length()>21 || newAttrib.length()<3){
    context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
            ChatColor.RED + "State name cannot be shorter than 3 or longer than 21."); }

            customState.setName(newAttrib);
        }

        else if(attrib.equalsIgnoreCase("tag")){
            if(newAttrib.length()!=3){
    context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
            ChatColor.RED + "State name cannot be shorter than 3 or longer than 21."); }

            customState.setTag(newAttrib);
        }

        else if(attrib.equalsIgnoreCase("color")){
            if(newAttrib.length()!=1){
                context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                ChatColor.RED + "State color must be a single letter."); }

            char color = newAttrib.charAt(0);
            customState.setChatColour(color);
        }

        else if(attrib.equalsIgnoreCase("userprefix")){
            UUID userID = Bukkit.getOfflinePlayer(newAttrib).getUniqueId();


        }


        // TODO Fix save
        try {
            customState.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}

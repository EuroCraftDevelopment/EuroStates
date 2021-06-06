package org.eurostates.mosecommands.commands.state.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.area.CustomStateArgument;
import org.eurostates.mosecommands.arguments.area.StateAttribArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.operation.RemainingArgument;
import org.eurostates.mosecommands.arguments.simple.StringArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.eurostates.parser.Parsers;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class StateForceEditCommand implements ArgumentCommand {
    public static final ExactArgument ADMIN_ARGUMENT = new ExactArgument("admin");
    public static final ExactArgument FORCE_EDIT_ARGUMENT = new ExactArgument("forceedit");
    public static final StateAttribArgument STATE_ATTRIB_ARGUMENT = new StateAttribArgument("stateattrib");
    public static final CustomStateArgument STATE_ARGUMENT = new CustomStateArgument("state");
    public static final StringArgument ATTRIB_ARGUMENT = new StringArgument("attrib");
    public static final RemainingArgument<String> REM_ARGUMENT = new RemainingArgument<>("remaining", new StringArgument("temp"));


    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                ADMIN_ARGUMENT,
                FORCE_EDIT_ARGUMENT,
                STATE_ATTRIB_ARGUMENT,
                STATE_ARGUMENT,
                ATTRIB_ARGUMENT,
                REM_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.of("eurostates.admin");
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        String attrib = context.getArgument(this, STATE_ATTRIB_ARGUMENT);
        CustomState customState = context.getArgument(this, STATE_ARGUMENT);
        String newAttrib = context.getArgument(this, ATTRIB_ARGUMENT);
        List<String> remainingArguments = context.getArgument(this, REM_ARGUMENT);

        String stateName = customState.getName();
        String stateTag = customState.getTag();

        if (attrib.equalsIgnoreCase("name")) {
            if (newAttrib.length() > 21 || newAttrib.length() < 3) {
                context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                        ChatColor.RED + "State name cannot be shorter than 3 or longer than 21."); return true;
            }

            customState.setName(newAttrib);
        }
        else if (attrib.equalsIgnoreCase("tag")) {
            if (newAttrib.length() != 3) {
                context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                        ChatColor.RED + "Tags must be 3 letters."); return true;
            }

            customState.setTag(newAttrib.toUpperCase());
        }
        else if (attrib.equalsIgnoreCase("color")) {
            if (newAttrib.length() != 1) {
                context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                        ChatColor.RED + "State color must be a single letter."); return true;
            }

            char color = newAttrib.charAt(0);
            customState.setChatColour(color);
        }
        else if (attrib.equalsIgnoreCase("userprefix")) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(newAttrib);

            if(!(offlinePlayer.hasPlayedBefore())){
                context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                        ChatColor.RED + "User has not played on this server before."); return true;
            }

            ESUser prefixUser = Parsers.GETTER_USER.fromId(offlinePlayer.getUniqueId());

            if(!(prefixUser.getState().getTag().equals(stateTag))){
                context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                        ChatColor.RED + "That user is not a member of specified state."); return true;
            }

            String newPrefix = remainingArguments.get(0);
            if (newPrefix.length() > 21 || newPrefix.length() < 3) {
                context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                        ChatColor.RED + "Prefix rank cannot be shorter than 3 or longer than 21."); return true;
            }

            prefixUser.setRank(newPrefix);
            try {
                prefixUser.save();
            } catch (IOException e) {
                e.printStackTrace();
            }

            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                    ChatColor.WHITE + "Changed "+offlinePlayer.getName()+"'s prefix rank to "+ newPrefix+".");

            // return early because its an exception
            return true;

        }


        try {
            customState.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.broadcastMessage(ChatColor.BLUE + "[EuroStates] " +
                ChatColor.WHITE + stateName + " (" + stateTag + ")'s " +
                attrib + " was forcibly changed to " + newAttrib + "."
        );

        return true;
    }
}

package org.eurostates.mosecommands.commands.state.global;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.town.Town;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.area.CustomStateArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.eurostates.util.Utils;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class StateViewCommand implements ArgumentCommand {

    public static final CustomStateArgument STATE_ARGUMENT = new CustomStateArgument("state");

    @Override
    public CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{STATE_ARGUMENT};
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.empty();
    }

    @Override
    public boolean run(CommandContext context, String[] arg) {
        CustomState state = context.getArgument(this, STATE_ARGUMENT);

        // get info to display
        String state_name = state.getName();
        String state_tag = state.getTag();
        String state_leader = Bukkit.getOfflinePlayer(state.getOwnerId()).getName();

        // get list of towns
        Collection<Town> towns = state.getTowns();
        String towns_str = towns.parallelStream().map(Town::getName).collect(Collectors.joining(", ")); //fucking lambda, hell yeah

        // get list of members
        Collection<ESUser> users = state.getEuroStatesCitizens();
        String users_str = users
                .parallelStream()
                .map(esUser -> {
                    return esUser.getRank() + " " + Utils.canCast(
                            Bukkit.getOfflinePlayer(esUser.getOwnerId()),
                            Player.class,
                            Player::getDisplayName,
                            OfflinePlayer::getName);
                }).collect(Collectors.joining(", "));

        CommandSender source = context.getSource();

        source.sendMessage(ChatColor.RED + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat(" ", 64));
        source.sendMessage(ChatColor.BLUE + "EuroStates " + ChatColor.WHITE + "State Info");
        source.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));

        source.sendMessage(ChatColor.WHITE + "State Name: " + ChatColor.GRAY + state_name);
        source.sendMessage(ChatColor.WHITE + "State Tag: " + ChatColor.GRAY + state_tag);
        source.sendMessage(ChatColor.WHITE + "Leader: " + ChatColor.GRAY + state_leader);
        source.sendMessage(ChatColor.WHITE + "Towns:" + ChatColor.DARK_GRAY + towns_str);
        source.sendMessage(ChatColor.WHITE + "Members:" + ChatColor.DARK_GRAY + users_str);

        source.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));

        return true;
    }
}

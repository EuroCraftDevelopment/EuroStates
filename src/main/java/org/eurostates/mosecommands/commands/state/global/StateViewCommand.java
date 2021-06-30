package org.eurostates.mosecommands.commands.state.global;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.State;
import org.eurostates.area.town.Town;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.area.CustomStateArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.eurostates.parser.Parsers;
import org.eurostates.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class StateViewCommand implements ArgumentCommand {

    public static final CustomStateArgument STATE_ARGUMENT = new CustomStateArgument("state");

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{STATE_ARGUMENT};
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.empty();
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        CustomState state = context.getArgument(this, STATE_ARGUMENT);

        // get info to display
        String stateName = state.getName();
        String stateTag = state.getTag();
        String stateLeader = Bukkit.getOfflinePlayer(state.getOwnerId()).getName();

        // get list of towns
        Collection<Town> towns = state.getTowns();
        String townNames = towns.parallelStream().map(Town::getName).collect(Collectors.joining(", ")); //fucking lambda, hell yeah

        // get list of members
        Collection<ESUser> users = state.getEuroStatesCitizens();
        String userNames = users
                .parallelStream()
                .map(esUser -> esUser.getRank() + " " + Utils.canCast(
                        Bukkit.getOfflinePlayer(esUser.getOwnerId()),
                        Player.class,
                        Player::getDisplayName,
                        OfflinePlayer::getName)
                ).collect(Collectors.joining(", "));

        ESUser user = null;
        if(context.getSource() instanceof Player) {
            user = Parsers.GETTER_USER.fromId(((Player) context.getSource()).getUniqueId());
        }

        CommandSender source = context.getSource();

        source.sendMessage(ChatColor.RED + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat(" ", 64));
        source.sendMessage(ChatColor.BLUE + "EuroStates " + ChatColor.WHITE + "State Info");
        source.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));

        source.sendMessage(ChatColor.WHITE + "State Name: " + ChatColor.GRAY + stateName);
        source.sendMessage(ChatColor.WHITE + "State Tag: " + ChatColor.GRAY + stateTag);
        source.sendMessage(ChatColor.WHITE + "Leader: " + ChatColor.GRAY + stateLeader);
        source.sendMessage(ChatColor.WHITE + "Towns:" + ChatColor.DARK_GRAY + townNames);
        source.sendMessage(ChatColor.WHITE + "Members:" + ChatColor.DARK_GRAY + userNames);

        if(user!=null && user.getState() instanceof CustomState) {
            source.sendMessage(ChatColor.WHITE + "Relationship: " + ChatColor.GRAY + ((CustomState) user.getState()).getRelationship(state).getStatus());
        }

        source.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));
        return true;
    }
}

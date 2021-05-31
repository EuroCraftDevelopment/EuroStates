package org.eurostates.mosecommands.commands.town.global;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eurostates.area.town.Town;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.area.TownArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.eurostates.util.Utils;

import java.util.Optional;

public class TownViewCommand implements ArgumentCommand {

    public static final String TOWN_ARGUMENT = "town";

    @Override
    public CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{new TownArgument(TOWN_ARGUMENT)};
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.empty();
    }

    @Override
    public boolean run(CommandContext context, String[] arg) {
        Town town = context.getArgument(this, TOWN_ARGUMENT);

        String townName = town.getName();
        String townTag = town.getTag();
        String stateName = town.getState().getName();
        String mayorName = Utils.canCast(town.getOwner(), Player.class, Player::getDisplayName, OfflinePlayer::getName);
        Block centre = town.getCentre();

        CommandSender source = context.getSource();

        source.sendMessage(ChatColor.RED + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat(" ", 64));
        source.sendMessage(ChatColor.BLUE + "EuroStates " + ChatColor.WHITE + "Town Info");
        source.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));

        source.sendMessage(ChatColor.WHITE + "Town Name: " + ChatColor.GRAY + townName);
        source.sendMessage(ChatColor.WHITE + "Town Tag: " + ChatColor.GRAY + townTag);
        source.sendMessage(ChatColor.WHITE + "State: " + ChatColor.GRAY + stateName);
        source.sendMessage(ChatColor.WHITE + "Mayor: " + ChatColor.GRAY + mayorName);
        source.sendMessage(ChatColor.WHITE + "Location: " + ChatColor.GRAY + centre.getX() + " " + centre.getY() + " " + centre.getZ());

        source.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));
        return true;
    }
}

package org.eurostates.functions.sendinfo;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.eurostates.area.town.LegacyTown;

import java.io.IOException;

public class SendTownInfo {
    public static void sendLegacyTownInfo(String town_tag, Player player) throws IOException {
        LegacyTown town = new LegacyTown(town_tag);

        try {
            town = LegacyTown.getFromFile(town_tag);
        } catch (IOException e) {
            player.sendMessage(ChatColor.BLUE + "[EuroStates] " + ChatColor.RED + "This is not a valid town!");
            return;
        }

        String town_name = town.getName();
        String state = town.getState();
        OfflinePlayer mayor_off = town.getMayorPlayer();

        String mayor;
        if (!mayor_off.isOnline()) {
            mayor = mayor_off.getName();
        } else {
            mayor = mayor_off.getPlayer().getDisplayName();
        }

        Location center = town.getCenter();

        player.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));
        player.sendMessage(ChatColor.BLUE + "EuroStates " + ChatColor.WHITE + "LegacyTown Info");
        player.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));

        player.sendMessage(ChatColor.WHITE + "LegacyTown Name: " + ChatColor.GRAY + town_name);
        player.sendMessage(ChatColor.WHITE + "LegacyTown Tag: " + ChatColor.GRAY + town_tag);
        player.sendMessage(ChatColor.WHITE + "State: " + ChatColor.GRAY + state);
        player.sendMessage(ChatColor.WHITE + "Mayor: " + ChatColor.GRAY + mayor);
        player.sendMessage(ChatColor.WHITE + "Location: " + ChatColor.GRAY + center.getBlockX() + " " + center.getBlockY() + " " + center.getBlockZ());

        player.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));
    }
}

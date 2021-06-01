package org.eurostates.commands.states.admin;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.eurostates.area.town.LegacyTown;
import org.eurostates.commands.CommandInterface;
import org.eurostates.player.ESPlayer;
import org.eurostates.state.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

@Deprecated
public class CreateState implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, ArrayList<String> args) throws IOException {
        if (args.size() != 3) {
            sender.sendMessage(ChatColor.BLUE + "[EuroStates] " + ChatColor.RED +
                    "Correct Usage: /state create [CapitalTownTag] [Name] [StateColor]");
            return false;
        }

        if (!sender.hasPermission("eurostates.state.create")) {
            sender.sendMessage(ChatColor.BLUE + "[EuroStates] "
                    + ChatColor.RED + "You lack the permission: eurostates.state.create");
            return false;
        }

        if (args.get(2).length() != 1) {
            sender.sendMessage(ChatColor.BLUE + "[EuroStates] " +
                    ChatColor.RED + "Chat Color can only be 1 character long.");
            return false;
        }

        String town_tag = args.get(0);
        String state_name = args.get(1);

        char state_color = args.get(2).charAt(0);

        String state_tag = state_name.substring(0, 3).toUpperCase();

        LegacyTown town;
        try {
            town = LegacyTown.getFromFile(town_tag);
        } catch (IOException e) {
            sender.sendMessage(ChatColor.BLUE + "[EuroStates] " +
                    ChatColor.RED + "No town with the tag " + town_tag + " exists.");
            return false;
        }

        OfflinePlayer mayor = town.getMayorPlayer();


        State state = new State(state_tag, state_name, mayor.getUniqueId(), state_color, new HashSet<>(), new HashSet<>(), new HashSet<>());
        state.addTown(town.getTag());

        state.saveToFile(state.getTag());

        ESPlayer esplayer = ESPlayer.getFromFile(mayor.getUniqueId().toString());

        esplayer.setStateTag(state_tag);
        esplayer.setRank("King");

        esplayer.saveToFile(ESPlayer.getFile(esplayer.getId().toString()));

        sender.getServer().broadcastMessage(ChatColor.BLUE + "[EuroStates] " + ChatColor.WHITE +
                "New state with the name of " + state.getName() + " (" + state.getTag() + ") with the leader as " + mayor.getName() + " and capital as " + town.getName() + " was created.");


        return true;
    }
}

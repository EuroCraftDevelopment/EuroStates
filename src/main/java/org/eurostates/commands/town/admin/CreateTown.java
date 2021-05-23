package org.eurostates.commands.town.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eurostates.commands.CommandInterface;
import org.eurostates.town.Town;

import java.io.IOException;
import java.util.ArrayList;

public class CreateTown implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, ArrayList<String> args) throws IOException {
        if(!sender.hasPermission("eurostates.createtown")){sender.sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.RED+"You lack the permission: eurostates.createtown");}

        if(args.size()!=2){sender.sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.RED+"Correct Usage: /town create [TownName] [TownMayor]"); return false;}

        String town_name = args.get(0);
        Player mayor = Bukkit.getPlayer(args.get(1));

        if(mayor==null){sender.sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.RED+"No online player with name "+args.get(1)+"found."); return false;}

        Town town = new Town(town_name.substring(0, 4).toUpperCase(), town_name, mayor.getUniqueId(), "None", new Location(null, 0, 0, 0));
        town.saveToFile(town.getTag());

        sender.getServer().broadcastMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.WHITE+
"New town with the name of "+town.getName()+" ("+town.getTag()+") with the mayor as "+mayor.getDisplayName()+" was created.");

        return false;
    }
}

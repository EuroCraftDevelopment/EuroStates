package org.eurostates.commands.town;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eurostates.commands.CommandInterface;
import org.eurostates.functions.sendinfo.SendPlayerInfo;
import org.eurostates.functions.sendinfo.SendTownInfo;

import java.io.IOException;
import java.util.ArrayList;

public class TownBase implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, ArrayList<String> args) throws IOException {
        sender.sendMessage(ChatColor.BLUE+"[EuroStates] "+ChatColor.WHITE+"Please do /town view to view a town's information.");
        return false;
    }
}

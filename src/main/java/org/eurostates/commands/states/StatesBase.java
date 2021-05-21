package org.eurostates.commands.states;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.eurostates.commands.CommandInterface;
import org.eurostates.functions.SendPlayerInfo;

import java.io.IOException;

public class StatesBase implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) throws IOException {
        SendPlayerInfo.sendPlayerInfo((Player) sender);
        return false;
    }
}


package org.eurostates.commands.states;

import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.eurostates.commands.CommandInterface;
import org.eurostates.functions.sendinfo.SendPlayerInfo;

import java.io.IOException;
import java.util.ArrayList;

@Deprecated
public class StatesBase implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, ArrayList<String> args) throws IOException {
        SendPlayerInfo.sendPlayerInfo((Player) sender);
        return false;
    }
}


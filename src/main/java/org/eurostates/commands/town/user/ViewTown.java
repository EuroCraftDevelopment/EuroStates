package org.eurostates.commands.town.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eurostates.commands.CommandInterface;
import org.eurostates.functions.sendinfo.SendTownInfo;

import java.io.IOException;
import java.util.ArrayList;

@Deprecated
public class ViewTown implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, ArrayList<String> args) throws IOException {
        SendTownInfo.sendLegacyTownInfo(args.get(0), (Player) sender);
        return false;
    }
}

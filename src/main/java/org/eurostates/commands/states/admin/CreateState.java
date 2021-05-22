package org.eurostates.commands.states.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.eurostates.commands.CommandInterface;

import java.util.ArrayList;

public class CreateState implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, ArrayList<String> args) {
        sender.sendMessage(args.get(0));
        return true;
    }
}

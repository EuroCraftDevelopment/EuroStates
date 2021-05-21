package org.eurostates.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;

import java.io.IOException;

public interface CommandInterface {

    //Every time I make a command, I will use this same method.
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) throws IOException;

}
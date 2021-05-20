package org.eurostates.commands;

import org.bukkit.plugin.Plugin;
import org.eurostates.commands.testcommands.HelloWorld;

import static org.bukkit.Bukkit.getServer;

public class CommandHandler {
    public static void launchCommands(Plugin plugin){
        getServer().getPluginCommand("helloworld").setExecutor(new HelloWorld());
    }
}

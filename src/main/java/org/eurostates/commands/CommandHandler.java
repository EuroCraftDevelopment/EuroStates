package org.eurostates.commands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.eurostates.EuroStates;
import org.eurostates.commands.states.States;

public class CommandHandler {
    public static void commandLauncher(){
        Plugin plugin = EuroStates.getPlugin();

        // States
        Bukkit.getPluginCommand("states").setExecutor(new States());
    }
}

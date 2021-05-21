package org.eurostates.commands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.eurostates.EuroStates;
import org.eurostates.commands.states.StatesBase;
import org.eurostates.commands.states.StatesCmdHandler;

public class CommandHandler {
    public static void commandLauncher(){
        Plugin plugin = EuroStates.getPlugin();

        // States command reg
        StatesCmdHandler handler = new StatesCmdHandler();
        handler.register("states", new StatesBase());

        // States
        Bukkit.getPluginCommand("states").setExecutor(new StatesCmdHandler());

    }
}

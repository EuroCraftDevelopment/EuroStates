package org.eurostates;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.eurostates.commands.CommandHandler;
import org.eurostates.events.EventHandler;
import org.eurostates.events.Listeners;

public final class EuroStates extends JavaPlugin {

    static EuroStates plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this; // For further plugin obj access
        CommandHandler.launchCommands(this);
        EventHandler.registerEvents();
    }

    public static EuroStates getPlugin() {
        return plugin;
    }
}

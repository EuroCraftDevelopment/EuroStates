package org.eurostates;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.eurostates.commands.CommandHandler;
import org.eurostates.events.Listeners;

public final class EuroStates extends JavaPlugin {

    static EuroStates plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this; // For further plugin obj access
        CommandHandler.launchCommands(this);
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
    }

    public static EuroStates getPlugin() {
        return plugin;
    }
}

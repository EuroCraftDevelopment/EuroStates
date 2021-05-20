package org.eurostates;


import org.bukkit.plugin.java.JavaPlugin;
import org.eurostates.commands.CommandHandler;
import org.eurostates.commands.states.States;
import org.eurostates.events.EventHandler;

public final class EuroStates extends JavaPlugin {

    static EuroStates plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this; // For further plugin obj access
        CommandHandler.commandLauncher();
        EventHandler.registerEvents();
    }

    public static EuroStates getPlugin() {
        return plugin;
    }
}

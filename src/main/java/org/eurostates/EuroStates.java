package org.eurostates;


import org.bukkit.plugin.java.JavaPlugin;
import org.eurostates.commands.CommandHandler;

public final class EuroStates extends JavaPlugin {

    static EuroStates plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this; // For further plugin obj access
        CommandHandler.launchCommands(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static EuroStates retPlugin(){return plugin;}
}

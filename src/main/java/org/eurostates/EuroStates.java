package org.eurostates;


import org.bukkit.plugin.java.JavaPlugin;
import org.eurostates.commands.CommandHandler;

public final class EuroStates extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        CommandHandler.launchCommands(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

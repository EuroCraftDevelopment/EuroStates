package org.eurostates;


import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.eurostates.commands.CommandHandler;
import org.eurostates.events.EventHandler;

public final class EuroStates extends JavaPlugin {

    static EuroStates plugin;
    static LuckPerms api;

    @Override
    public void onEnable() {
        // Plugin startup logic
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) { api = provider.getProvider(); }


        plugin = this; // For further plugin obj access
        CommandHandler.commandLauncher();
        EventHandler.registerEvents();


    }

    public static EuroStates getPlugin() {
        return plugin;
    }
    public static LuckPerms getApi() { return api; }
}

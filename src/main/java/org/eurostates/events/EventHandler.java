package org.eurostates.events;

import org.bukkit.Bukkit;
import org.eurostates.EuroStates;

public class EventHandler {
    public static void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new Listeners(), EuroStates.getPlugin());
    }
}

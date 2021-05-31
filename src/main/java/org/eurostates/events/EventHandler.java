package org.eurostates.events;

import org.bukkit.Bukkit;
import org.eurostates.EuroStates;

//you created a class for 1 line .... that is only used one within the whole plugin

@Deprecated
public class EventHandler {
    public static void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new Listeners(), EuroStates.getPlugin());
    }
}

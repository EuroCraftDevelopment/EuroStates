package org.eurostates.dynmap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.MarkerSet;

public class DAPIProvider {
    public static DynmapCommonAPI dapi = null;

    public static DynmapCommonAPI registerDynmap(JavaPlugin p) {
        Bukkit.getLogger().info("Loading Dynmap...");
        dapi = (DynmapCommonAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");
        if (dapi == null) {
            Bukkit.getLogger().warning("Failed loading Dynmap.");
            Bukkit.getServer().getPluginManager().disablePlugin(p);
        }
        Bukkit.getLogger().info("Loaded Dynmap.");
        return dapi;
    }
}

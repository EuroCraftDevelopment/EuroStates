package org.eurostates.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Listeners implements Listener {

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event){
        //you will need to test if its get message or format ... documentation sucks
        ChatColor prefix_color;
        String prefix;
        // TODO: Retrieve both player's nation tag and color



        // event.setFormat(prefix_color + prefix + " %s : %s");
    }
}

package org.eurostates.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.eurostates.player.ESPlayer;
import org.eurostates.state.State;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Listeners implements Listener {

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) throws IOException {
        //you will need to test if its get message or format ... documentation sucks
        State state = null;
        ChatColor prefix_color = null;

        String p_uuid = event.getPlayer().getUniqueId().toString();

        ESPlayer player = ESPlayer.getFromFile(ESPlayer.getFile(p_uuid));
        String stag = player.getStateTag();

        // If state tag is NOMAD, it means they are stateless, so keep as is.
        if(!stag.equals("NOMAD")){state = State.getFromFile(State.getFile(stag));}

        String prefix = "["+stag+"]";

        if(state==null){prefix_color=ChatColor.GRAY;}
        else{prefix_color = state.getBukkitColor();}

        event.setFormat(prefix_color + prefix + " %s : %s");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        UUID p_uuid = event.getPlayer().getUniqueId();
        File file = ESPlayer.getFile(p_uuid.toString());

        // if file does not exist, create it.
        if(!file.exists()){
            ESPlayer player = new ESPlayer(p_uuid);
            player.saveToFile(file);
        }
    }
}

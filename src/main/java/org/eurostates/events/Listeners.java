package org.eurostates.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.eurostates.EuroStates;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.State;

import java.util.UUID;

public class Listeners implements Listener {

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        ESUser user = EuroStates.getPlugin().getUser(playerId).orElseGet(() -> new ESUser(playerId));
        State state = user.getState();
        event.setFormat(state.getLegacyChatColour() + "[" + state.getTag() + "]" + ChatColor.RESET + " %s : %s");

        //these two do the same thing. Look how much smaller this is :)


        //you will need to test if its get message or format ... documentation sucks
        /*State state = null;
        ChatColor prefix_color = null;

        String p_uuid = event.getPlayer().getUniqueId().toString();

        ESPlayer player = ESPlayer.getFromFile(p_uuid);
        String stag = player.getStateTag();

        // If state tag is NOMAD, it means they are stateless, so keep as is.
        if (!stag.equals("NOMAD")) {
            state = State.getFromFile(stag);
        }

        String prefix = "[" + stag + "]";

        if (state == null) {
            prefix_color = ChatColor.GRAY;
        } else {
            prefix_color = state.getBukkitColor();
        }

        event.setFormat(prefix_color + prefix + ChatColor.RESET + " %s : %s");*/
    }

    /*
     *     this event isn't needed anymore as the plugin.getUser(UUID) handles it
     */


    /*@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        UUID p_uuid = event.getPlayer().getUniqueId();
        File file = ESPlayer.getFile(p_uuid.toString());

        // if file does not exist, create it.
        if (!file.exists()) {
            ESPlayer player = new ESPlayer(p_uuid);
            player.saveToFile(file);
        }
    }*/
}

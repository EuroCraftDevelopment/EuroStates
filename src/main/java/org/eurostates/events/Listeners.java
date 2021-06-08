package org.eurostates.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.State;
import org.eurostates.parser.Parsers;

import java.util.UUID;

public class Listeners implements Listener {

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        ESUser user = Parsers.GETTER_USER.fromId(playerId);
        State state = user.getState();
        event.setFormat(state.getLegacyChatColour() + "[" + state.getTag() + "]" + ChatColor.RESET + " %s : %s");
    }
}

package org.eurostates.functions;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.eurostates.player.ESPlayer;
import org.eurostates.state.State;

import java.io.IOException;

public class SendPlayerInfo {
    public static void sendPlayerInfo(Player player) throws IOException {


        String username = player.getDisplayName();
        String nation_name;
        String nation_rank;

        // Player Info
        ESPlayer esplayer = ESPlayer.getFromFile(ESPlayer.getFile(player.getUniqueId().toString()));
        if(esplayer.getStateTag().equals("NOMAD")){nation_name="Nomad";}
        else{State state = State.getFromFile(State.getFile(esplayer.getStateTag())); nation_name=state.getName();}
        nation_rank = esplayer.getRank();


        // Send Player Info
        player.sendMessage(ChatColor.BLUE+"EuroStates "+ChatColor.WHITE+"Player Info");
        player.sendMessage(ChatColor.RED +""+ ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));
        player.sendMessage(ChatColor.WHITE+"Name: "+ChatColor.GRAY+username);
        player.sendMessage(ChatColor.WHITE+"Nation: "+ChatColor.GRAY+nation_name);
        player.sendMessage(ChatColor.WHITE+"Custom Rank: "+ChatColor.GRAY+nation_rank);
        player.sendMessage(ChatColor.RED +""+ ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));
    }
}

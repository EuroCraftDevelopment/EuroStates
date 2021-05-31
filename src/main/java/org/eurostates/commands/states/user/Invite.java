package org.eurostates.commands.states.user;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eurostates.commands.CommandInterface;
import org.eurostates.player.ESPlayer;
import org.eurostates.state.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Deprecated
public class Invite implements CommandInterface {
    public static HashMap<String, Long> invitedPlayers = new HashMap<String, Long>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, ArrayList<String> args) throws IOException {
        if(args.size()!=1 ||args.size()!=2){sender.sendMessage(ChatColor.BLUE+"[EuroStates] "
                    +ChatColor.RED+ "Correct Usage: /states invite accept or /states invite state [user]"); return false;}

        if(args.get(0).equalsIgnoreCase("accept")){

        }

        else if(args.get(0).equalsIgnoreCase("state")){
            if(args.size()!=2){sender.sendMessage(ChatColor.BLUE+"[EuroStates] "
                    +ChatColor.RED+ "Correct Usage: /states invite accept or /states invite state [user]"); return false;}

            if(!(sender instanceof Player)){sender.sendMessage(ChatColor.BLUE+"[EuroStates] "
                    +ChatColor.RED+ "This command cannot be run from the console."); return false;}

            Player source_p = (Player) sender;
            Player target_p = sender.getServer().getPlayer(args.get(1));

            if(target_p==null){sender.sendMessage(ChatColor.BLUE+"[EuroStates] "
                    +ChatColor.RED+args.get(1) +" could not be found."); return false;}

            ESPlayer es_source_p = ESPlayer.getFromFile(source_p.getUniqueId().toString());

            String state_tag = es_source_p.getStateTag();

            if(state_tag.equals("NOMAD")){sender.sendMessage(ChatColor.BLUE+"[EuroStates] "+
                    ChatColor.RED+"You are not part of any state."); return false;}

            State t_state = State.getFromFile(state_tag);

            if(t_state.getOwner().toString().equals(es_source_p.getId().toString())){sender.sendMessage(ChatColor.BLUE+"[EuroStates] "+
                    ChatColor.RED+"You are not the owner of "+t_state.getName()+" ."); return false;}



        }



        return false;
    }
}

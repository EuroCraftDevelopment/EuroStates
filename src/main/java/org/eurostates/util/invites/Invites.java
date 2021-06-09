package org.eurostates.util.invites;

import org.bukkit.Bukkit;
import org.eurostates.EuroStates;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.parser.Parsers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Invites {
    private HashMap<UUID, UUID> invites = new HashMap<UUID, UUID>();


    public void addInvite(ESUser inviter, ESUser invited){
        this.invites.put(invited.getOwnerId(), inviter.getOwnerId());
        Bukkit.getScheduler().runTaskLater(
                EuroStates.getPlugin(),
                () -> this.invites.remove(invited.getOwnerId()),
                3600
        );
    }

    public CustomState getStateFromInvited(ESUser invited){
        if(invites.containsKey(invited.getOwnerId())){
            return (CustomState) Parsers.GETTER_USER.fromId(invites.get(invited.getOwnerId())).getState();
        }
        return null;
    }
}

package org.eurostates.relationship.war;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.eurostates.area.ESUser;
import org.eurostates.area.town.Town;
import org.eurostates.parser.Parsers;
import org.eurostates.relationship.WarRelationship;

import java.util.Optional;

public class WarListener implements Listener {

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player killed = (Player) event.getEntity();
        Player killer = killed.getKiller();
        if (killer == null) {
            return;
        }
        ESUser killedUser = Parsers.GETTER_USER.fromId(killed.getUniqueId());
        ESUser killerUser = Parsers.GETTER_USER.fromId(killer.getUniqueId());

        Optional<Town> opKilledTown = killedUser.getTown();
        Optional<Town> opKillerTown = killerUser.getTown();

        if (!opKilledTown.isPresent() || !opKillerTown.isPresent()) {
            return;
        }

        Town killedTown = opKilledTown.get();
        Town killerTown = opKillerTown.get();

        Optional<WarRelationship> opRelationship = killerTown.getWarWith(killedTown);
        if (!opRelationship.isPresent()) {
            return;
        }
        WarRelationship relationship = opRelationship.get();
        Optional<WarSide> opWarSide = relationship.getWarSide(killerTown);
        if (!opWarSide.isPresent()) {
            return;
        }
        WarSide side = opWarSide.get();
        //TODO get config

    }
}

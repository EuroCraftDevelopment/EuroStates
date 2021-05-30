package org.eurostates.area.state;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.eurostates.area.Area;
import org.eurostates.area.ESUser;
import org.eurostates.area.Rank;
import org.eurostates.area.town.Town;
import org.eurostates.parser.Parsers;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public interface State extends Area {

    Set<Town> getTowns();

    Set<String> getPermissions();

    Set<Rank> getRanks();

    Set<ESUser> getEuroStatesCitizens();

    default Set<UUID> getCitizenIds() {
        return Parsers.collectOrFilter(this.getEuroStatesCitizens().parallelStream(), citizen -> {
            try {
                return Parsers.GETTER_USER.toId(citizen);
            } catch (IOException e) {
                return null;
            }
        }, Collectors.toSet());
    }

    default Set<OfflinePlayer> getCitizens() {
        return this
                .getCitizenIds()
                .stream()
                .map(Bukkit::getOfflinePlayer)
                .collect(Collectors.toSet());
    }

    default Set<Player> getOnlineCitizens() {
        return this.getCitizenIds()
                .stream()
                .filter(uuid -> Bukkit.getPlayer(uuid) != null)
                .map(Bukkit::getPlayer)
                .collect(Collectors.toSet());
    }
}

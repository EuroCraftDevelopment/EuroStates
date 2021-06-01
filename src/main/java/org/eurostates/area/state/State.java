package org.eurostates.area.state;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.eurostates.area.Area;
import org.eurostates.area.ESUser;
import org.eurostates.area.town.Town;
import org.eurostates.parser.Parsers;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public interface State extends Area {

    @NotNull Set<Town> getTowns();

    @NotNull Set<String> getPermissions();

    @NotNull Set<ESUser> getEuroStatesCitizens();

    default @NotNull Set<String> getRanks() {
        return getEuroStatesCitizens().parallelStream().map(ESUser::getRank).collect(Collectors.toSet());
    }


    default @NotNull Set<UUID> getCitizenIds() {
        return Parsers.collectOrFilter(this.getEuroStatesCitizens().parallelStream(), citizen -> {
            try {
                return Parsers.GETTER_USER.toId(citizen);
            } catch (IOException e) {
                return null;
            }
        }, Collectors.toSet());
    }

    default @NotNull Set<OfflinePlayer> getCitizens() {
        return this
                .getCitizenIds()
                .stream()
                .map(Bukkit::getOfflinePlayer)
                .collect(Collectors.toSet());
    }

    default @NotNull Set<Player> getOnlineCitizens() {
        return this.getCitizenIds()
                .stream()
                .filter(uuid -> Bukkit.getPlayer(uuid) != null)
                .map(Bukkit::getPlayer)
                .collect(Collectors.toSet());
    }
}

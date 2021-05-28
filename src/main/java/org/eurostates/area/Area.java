package org.eurostates.area;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.eurostates.parser.Parsers;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public interface Area {

    String getTag();

    String getName();

    char getLegacyChatColourCharacter();

    Set<ESUser> getEuroStatesCitizens();

    Set<Rank> getRanks();

    default ChatColor getLegacyChatColour() {
        return ChatColor.getByChar(this.getLegacyChatColourCharacter());
    }

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

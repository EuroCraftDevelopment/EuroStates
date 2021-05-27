package org.eurostates.area;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public interface Area {

    String getTag();

    String getName();

    char getLegacyChatColourCharacter();

    Set<UUID> getCitizenIds();

    Set<Rank> getRanks();

    default ChatColor getLegacyChatColour() {
        return ChatColor.getByChar(this.getLegacyChatColourCharacter());
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

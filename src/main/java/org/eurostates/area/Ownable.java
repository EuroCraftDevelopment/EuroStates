package org.eurostates.area;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface Ownable {

    UUID getOwnerId();

    default OfflinePlayer getOwner() {
        OfflinePlayer player = Bukkit.getOfflinePlayer(this.getOwnerId());
        if (player.getPlayer() != null) {
            return player.getPlayer();
        }
        return player;
    }
}

package org.eurostates.ownable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public interface PlayerOwnable extends Ownable {

    default OfflinePlayer getOwner() {
        OfflinePlayer player = Bukkit.getOfflinePlayer(this.getOwnerId());
        if (player.getPlayer() != null) {
            return player.getPlayer();
        }
        return player;
    }
}

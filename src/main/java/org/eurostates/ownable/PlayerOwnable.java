package org.eurostates.ownable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.eurostates.area.ESUser;
import org.eurostates.parser.Parsers;
import org.jetbrains.annotations.NotNull;

public interface PlayerOwnable extends Ownable {

    default @NotNull OfflinePlayer getOwner() {
        OfflinePlayer player = Bukkit.getOfflinePlayer(this.getOwnerId());
        if (player.getPlayer() != null) {
            return player.getPlayer();
        }
        return player;
    }

    default @NotNull ESUser getOwnerUser() {
        return Parsers.GETTER_USER.fromId(this.getOwnerId());
    }
}

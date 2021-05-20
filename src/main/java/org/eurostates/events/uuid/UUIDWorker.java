package org.eurostates.events.uuid;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.eurostates.EuroStates;
import org.eurostates.events.uuid.UUIDManager;

import java.io.IOException;
import java.util.UUID;

public class UUIDWorker implements Listener {

    public void onJoin(PlayerJoinEvent event) throws IOException {
        UUID pUUID = event.getPlayer().getUniqueId();
        String str_UUID = pUUID.toString();

        UUIDManager.saveUUID(str_UUID, event.getPlayer().getName());
    }
}

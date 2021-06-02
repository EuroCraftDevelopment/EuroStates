package org.eurostates.mosecommands.bukkit;

import org.eurostates.mosecommands.ArgumentCommands;

public interface BukkitCommands {

    BukkitCommand TOWN_COMMAND = new BukkitCommand(
            ArgumentCommands.TOWN_VIEW
    );
    BukkitCommand STATE_COMMAND = new BukkitCommand(
            ArgumentCommands.STATE_CREATE,
            ArgumentCommands.STATE_DELETE,
            ArgumentCommands.STATE_EDIT,
            ArgumentCommands.STATE_FORCE_EDIT,
            ArgumentCommands.STATE_FORCE_HANDOVER
    );
}

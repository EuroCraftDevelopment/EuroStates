package org.eurostates.mosecommands.bukkit;

import org.eurostates.mosecommands.commands.state.global.StateViewCommand;
import org.eurostates.mosecommands.commands.town.global.TownViewCommand;

public interface BukkitCommands {

    BukkitCommand TOWN_COMMAND = new BukkitCommand(new TownViewCommand());
    BukkitCommand STATE_COMMAND = new BukkitCommand(new StateViewCommand());
}

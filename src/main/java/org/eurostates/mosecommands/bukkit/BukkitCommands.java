package org.eurostates.mosecommands.bukkit;

import org.eurostates.mosecommands.ArgumentCommands;

public interface BukkitCommands {

    BukkitCommand TOWN_COMMAND = new BukkitCommand(
            ArgumentCommands.TOWN_VIEW,
            ArgumentCommands.TOWN_CREATE,
            ArgumentCommands.TOWN_DELETE
    );
    BukkitCommand STATE_COMMAND = new BukkitCommand(
            ArgumentCommands.STATE_CREATE,
            ArgumentCommands.STATE_DELETE,
            ArgumentCommands.STATE_EDIT_NAME,
            ArgumentCommands.STATE_EDIT_LEGACY_COLOUR,
            ArgumentCommands.STATE_EDIT_TAG,
            ArgumentCommands.STATE_EDIT_PREFIX,
            ArgumentCommands.STATE_EDIT_CURRENCY,
            ArgumentCommands.STATE_VIEW,
            ArgumentCommands.STATE_FORCE_HANDOVER,
            ArgumentCommands.STATE_INVITE_COMMAND,
            ArgumentCommands.STATE_INVITE_ACCEPT_COMMAND,
            ArgumentCommands.STATE_LEAVE
    );
    BukkitCommand TECHNOLOGY_COMMAND = new BukkitCommand(
            ArgumentCommands.TECHNOLOGY_CREATE,
            ArgumentCommands.TECHNOLOGY_REQUIREMENT_ADD,
            ArgumentCommands.TECHNOLOGY_REQUIREMENT_REMOVE,
            ArgumentCommands.TECHNOLOGY_PERMISSION_ADD,
            ArgumentCommands.TECHNOLOGY_PERMISSION_REMOVE,
            ArgumentCommands.TECHNOLOGY_ASSIGN,
            ArgumentCommands.TECHNOLOGY_LIST,
            ArgumentCommands.TECHNOLOGY_VIEW
    );
}

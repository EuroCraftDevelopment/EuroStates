package org.eurostates.area;

import org.bukkit.ChatColor;
import org.eurostates.ownable.PlayerOwnable;

public interface Area {

    String getTag();

    String getName();

    char getLegacyChatColourCharacter();

    default ChatColor getLegacyChatColour() {
        return ChatColor.getByChar(this.getLegacyChatColourCharacter());
    }
}

package org.eurostates.area;

import org.bukkit.ChatColor;

public interface Area {

    String getTag();

    String getName();

    char getLegacyChatColourCharacter();

    default ChatColor getLegacyChatColour() {
        return ChatColor.getByChar(this.getLegacyChatColourCharacter());
    }
}

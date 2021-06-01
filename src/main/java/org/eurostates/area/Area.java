package org.eurostates.area;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public interface Area {

    @NotNull String getTag();

    @NotNull String getName();

    @NotNull char getLegacyChatColourCharacter();

    default @NotNull ChatColor getLegacyChatColour() {
        ChatColor colour = ChatColor.getByChar(this.getLegacyChatColourCharacter());
        if (colour == null) {
            throw new IllegalStateException("Attached legacy chat colour does not have a valid colour.");
        }
        return colour;
    }
}

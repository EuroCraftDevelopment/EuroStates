package org.eurostates.area;

import org.bukkit.ChatColor;
import org.eurostates.area.town.Town;
import org.eurostates.relationship.Relationship;
import org.eurostates.relationship.war.WarRelationship;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public interface Area {

    @NotNull String getTag();

    @NotNull String getName();

    char getLegacyChatColourCharacter();

    Set<Relationship> getRelationships();

    Optional<WarRelationship> getWarWith(Town town);

    default @NotNull ChatColor getLegacyChatColour() {
        ChatColor colour = ChatColor.getByChar(this.getLegacyChatColourCharacter());
        if (colour == null) {
            throw new IllegalStateException("Attached legacy chat colour does not have a valid colour.");
        }
        return colour;
    }
}

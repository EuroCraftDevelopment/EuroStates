package org.eurostates.area;

import org.bukkit.ChatColor;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.town.Town;
import org.eurostates.relationship.Relationship;
import org.eurostates.relationship.WarRelationship;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public interface Area {

    @NotNull String getTag();

    @NotNull String getName();

    @NotNull char getLegacyChatColourCharacter();

    Set<Relationship> getRelationships();

    default Set<Relationship> getRelationships(CustomState state) {
        return getRelationships()
                .parallelStream()
                .filter(r -> r.getStates().contains(state))
                .collect(Collectors.toSet());
    }

    default Set<WarRelationship> getWarsWith(Town town){
        return getRelationships(town.getState())
                .parallelStream()
                .filter(r -> r instanceof WarRelationship)
                .map(r -> (WarRelationship)r)
                .filter(r -> r
                        .getWarSides()
                        .parallelStream()
                        .anyMatch(s -> s.getTown().equals(town)))
                .collect(Collectors.toSet());
    }

    default @NotNull ChatColor getLegacyChatColour() {
        ChatColor colour = ChatColor.getByChar(this.getLegacyChatColourCharacter());
        if (colour == null) {
            throw new IllegalStateException("Attached legacy chat colour does not have a valid colour.");
        }
        return colour;
    }
}

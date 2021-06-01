package org.eurostates.area.town;

import org.bukkit.block.Block;
import org.eurostates.area.Area;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.States;
import org.eurostates.ownable.PlayerOwnable;
import org.eurostates.relationship.WarRelationship;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Town extends Area, PlayerOwnable {

    @NotNull Block getCentre();

    @NotNull UUID getStateId();

    default Optional<WarRelationship> getWarWith(Town town) {
        return getWarsWith(town).parallelStream().findAny();
    }

    @Override
    @Deprecated
    default Set<WarRelationship> getWarsWith(Town town) {
        return Area.super.getWarsWith(town);
    }

    @Override
    default char getLegacyChatColourCharacter() {
        return this.getState().getLegacyChatColourCharacter();
    }

    default @NotNull CustomState getState() {
        UUID stateId = this.getStateId();
        return States.CUSTOM_STATES
                .parallelStream()
                .filter(state -> state.getId().equals(stateId))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Could not find town with the id of " + stateId.toString()));
    }
}

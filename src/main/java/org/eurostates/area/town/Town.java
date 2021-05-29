package org.eurostates.area.town;

import org.bukkit.block.Block;
import org.eurostates.area.Area;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.States;
import org.eurostates.ownable.PlayerOwnable;

import java.util.UUID;

public interface Town extends Area, PlayerOwnable {

    Block getCentre();

    UUID getStateId();

    @Override
    default char getLegacyChatColourCharacter() {
        return this.getState().getLegacyChatColourCharacter();
    }

    default CustomState getState() {
        UUID stateId = this.getStateId();
        return States.CUSTOM_STATES
                .parallelStream()
                .filter(state -> state.getId().equals(stateId))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Could not find town with the id of " + stateId.toString()));
    }
}
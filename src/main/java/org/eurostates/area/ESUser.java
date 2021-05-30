package org.eurostates.area;

import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.State;
import org.eurostates.area.state.States;
import org.eurostates.area.town.Town;
import org.eurostates.ownable.PlayerOwnable;

import java.util.Optional;
import java.util.UUID;

public class ESUser implements PlayerOwnable {

    private final UUID uuid;
    private String rank;

    public ESUser(UUID uuid) {
        this(uuid, null);
    }

    public ESUser(UUID uuid, String rank) {
        this.uuid = uuid;
        this.rank = rank;
    }

    public String getRank() {
        if (this.rank != null) {
            return this.rank;
        }
        State state = getState();
        if (state instanceof CustomState && ((CustomState) state).getOwnerId().equals(this.uuid)) {
            return Ranks.KING;
        }
        if (state instanceof CustomState) {
            return Ranks.CITIZEN;
        }
        return Ranks.NOMAD;

    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Optional<Town> getTown() {
        return States
                .CUSTOM_STATES
                .parallelStream()
                .flatMap(state -> state.getTowns().stream())
                .filter(town -> town.getOwnerId().equals(this.uuid))
                .findAny();
    }

    public State getState() {
        Optional<Town> opTown = this.getTown();
        if (opTown.isPresent()) {
            return opTown.get().getState();
        }
        return States.NOMAD_STATE;
    }

    @Override
    public UUID getOwnerId() {
        return this.uuid;
    }
}

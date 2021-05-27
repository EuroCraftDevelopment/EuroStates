package org.eurostates.area;

import org.eurostates.area.Rank;
import org.eurostates.area.state.State;
import org.eurostates.area.state.States;
import org.eurostates.area.town.Town;

import java.util.Optional;
import java.util.UUID;

public class ESUser {

    private final UUID uuid;
    private UUID rankUUID;

    public ESUser(UUID uuid, UUID rankUUID) {
        this.uuid = uuid;
        this.rankUUID = rankUUID;
    }

    public Optional<UUID> getRankId() {
        return Optional.ofNullable(this.rankUUID);
    }

    public Optional<Rank> getRank() {
        Optional<Town> opTown = getTown();
        return opTown.flatMap(town -> town
                .getRanks()
                .parallelStream()
                .filter(rank -> rank.getId().equals(this.rankUUID))
                .findAny());

    }

    public void setRank(Rank rank) {
        Town town = getTown().orElseThrow(() -> new IllegalStateException("Player must be part of a town before setting a rank"));
        if (!rank.getState().equals(town.getState())) {
            throw new IllegalStateException("Rank is not part of the players state");
        }
        this.rankUUID = rank.getId();
    }

    public Optional<Town> getTown() {
        return States
                .CUSTOM_STATES
                .parallelStream()
                .flatMap(state -> state.getTowns().stream())
                .filter(town -> town.getCitizenIds().contains(this.uuid))
                .findAny();
    }

    public State getState() {
        Optional<Town> opTown = this.getTown();
        if (opTown.isPresent()) {
            return opTown.get().getState();
        }
        return States.NOMAD_STATE;
    }
}

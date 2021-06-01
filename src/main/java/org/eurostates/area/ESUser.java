package org.eurostates.area;

import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.State;
import org.eurostates.area.state.States;
import org.eurostates.area.town.Town;
import org.eurostates.ownable.PlayerOwnable;
import org.eurostates.parser.Savable;
import org.eurostates.parser.Serializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ESUser implements PlayerOwnable, Savable<ESUser, Map<String, Object>, String> {

    private final @NotNull UUID uuid;
    private @Nullable String rank;

    public ESUser(@NotNull UUID uuid) {
        this(uuid, null);
    }

    public ESUser(@NotNull UUID uuid, @Nullable String rank) {
        this.uuid = uuid;
        this.rank = rank;
    }

    public @NotNull String getRank() {
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

    public void setRank(@NotNull String rank) {
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

    public @NotNull State getState() {
        Optional<Town> opTown = this.getTown();
        if (opTown.isPresent()) {
            return opTown.get().getState();
        }
        return States.NOMAD_STATE;
    }

    @Override
    public @NotNull UUID getOwnerId() {
        return this.uuid;
    }

    //TODO
    @Override
    public @NotNull File getFile() {
        return null;
    }

    //TODO
    @Override
    public @NotNull Serializable<ESUser, Map<String, Object>> getSerializableParser() {
        return null;
    }

    //TODO
    @Override
    public @NotNull Serializable<ESUser, String> getIdParser() {
        return null;
    }

    //TODO
    @Override
    public @NotNull String getRootNode() {
        return null;
    }
}

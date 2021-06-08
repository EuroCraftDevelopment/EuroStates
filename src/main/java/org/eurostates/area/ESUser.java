package org.eurostates.area;

import org.eurostates.EuroStates;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.State;
import org.eurostates.area.state.States;
import org.eurostates.area.town.Town;
import org.eurostates.area.ownable.PlayerOwnable;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.Savable;
import org.eurostates.parser.area.user.GetterUserParser;
import org.eurostates.parser.area.user.LoadableUserParser;
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

    public void setRank(@Nullable String rank) {
        this.rank = rank;
    }

    public Optional<String> getAssignedRank() {
        return Optional.ofNullable(this.rank);
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

    @Override
    public @NotNull File getFile() {
        return new File(EuroStates.getPlugin().getDataFolder(), "data/users/" + Parsers.UUID.to(this.uuid) + ".yml");
    }

    @Override
    public @NotNull LoadableUserParser getSerializableParser() {
        return Parsers.LOADABLE_USER;
    }

    @Override
    public @NotNull GetterUserParser getIdParser() {
        return Parsers.GETTER_USER;
    }

    @Override
    public @NotNull String getRootNode() {
        return "user";
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ESUser)) {
            return false;
        }
        ESUser user = (ESUser) obj;
        return user.getOwnerId().equals(this.uuid);
    }
}

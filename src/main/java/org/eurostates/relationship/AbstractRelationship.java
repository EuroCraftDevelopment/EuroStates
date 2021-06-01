package org.eurostates.relationship;

import org.eurostates.area.state.CustomState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractRelationship implements Relationship {

    @NotNull Set<CustomState> states = new HashSet<>();

    public AbstractRelationship(CustomState... states) {
        this(Arrays.asList(states));
    }

    public AbstractRelationship(@NotNull Collection<CustomState> states) {
        this.states.addAll(states);
    }

    @Override
    public @NotNull Collection<CustomState> getStates() {
        return this.states;
    }

}

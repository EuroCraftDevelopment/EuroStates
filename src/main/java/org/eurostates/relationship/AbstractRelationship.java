package org.eurostates.relationship;

import org.eurostates.area.state.State;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractRelationship implements Relationship {

    @NotNull Set<State> states = new HashSet<>();

    public AbstractRelationship(State... states) {
        this(Arrays.asList(states));
    }

    public AbstractRelationship(@NotNull Collection<State> states) {
        this.states.addAll(states);
    }

    @Override
    public @NotNull Collection<State> getStates() {
        return this.states;
    }

}

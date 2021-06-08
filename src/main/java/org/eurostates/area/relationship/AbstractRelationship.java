package org.eurostates.area.relationship;

import org.eurostates.area.state.CustomState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AbstractRelationship implements Relationship {

    private final @NotNull Set<CustomState> states = new HashSet<>();
    private final @NotNull RelationshipStatus status;

    public AbstractRelationship(@NotNull RelationshipStatus status, CustomState... states) {
        this(status, Arrays.asList(states));
    }

    public AbstractRelationship(@NotNull RelationshipStatus status, Collection<CustomState> customStates) {
        this.status = status;
        this.states.addAll(customStates);
    }

    @Override
    public @NotNull Collection<CustomState> getStates() {
        return this.states;
    }

    @Override
    public @NotNull RelationshipStatus getStatus() {
        return this.status;
    }


}

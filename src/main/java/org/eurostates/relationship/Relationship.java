package org.eurostates.relationship;

import org.eurostates.area.state.State;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Relationship {

    @NotNull Collection<State> getStates();

    @NotNull RelationshipStatus getStatus();
}

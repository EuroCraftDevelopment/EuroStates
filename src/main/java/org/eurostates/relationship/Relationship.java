package org.eurostates.relationship;

import org.eurostates.area.state.CustomState;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Relationship {

    @NotNull Collection<CustomState> getStates();

    @NotNull RelationshipStatus getStatus();
}

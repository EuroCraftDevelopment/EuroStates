package org.eurostates.relationship;

import org.eurostates.area.state.CustomState;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class NeutralRelationship extends AbstractRelationship {

    public NeutralRelationship(CustomState... states) {
        super(states);
    }

    public NeutralRelationship(@NotNull Collection<CustomState> states) {
        super(states);
    }

    @Override
    public @NotNull RelationshipStatus getStatus() {
        return RelationshipStatus.NEUTRAL;
    }
}

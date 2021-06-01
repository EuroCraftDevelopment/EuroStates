package org.eurostates.relationship;

import org.eurostates.area.state.CustomState;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class AllyRelationship extends AbstractRelationship {

    public AllyRelationship(CustomState... states) {
        super(states);
    }

    public AllyRelationship(@NotNull Collection<CustomState> states) {
        super(states);
    }

    @Override
    public @NotNull RelationshipStatus getStatus() {
        return RelationshipStatus.ALLY;
    }
}

package org.eurostates.relationship;

import org.jetbrains.annotations.NotNull;

public class AllyRelationship extends AbstractRelationship {

    @Override
    public @NotNull RelationshipStatus getStatus() {
        return RelationshipStatus.ALLY;
    }
}

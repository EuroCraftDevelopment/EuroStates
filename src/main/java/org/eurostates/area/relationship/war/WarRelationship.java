package org.eurostates.area.relationship.war;

import org.eurostates.area.state.CustomState;
import org.eurostates.area.relationship.AbstractRelationship;
import org.eurostates.area.relationship.Relationship;
import org.eurostates.area.relationship.RelationshipStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WarRelationship implements Relationship {

    private final AbstractRelationship enemyRelationship;
    private final Set<WarTown> warTown = new HashSet<>();

    public WarRelationship(AbstractRelationship relationship) {
        if (!relationship.getStatus().equals(RelationshipStatus.ENEMY)) {
            throw new IllegalArgumentException("Relationship given is not a enemy status");
        }
        this.enemyRelationship = relationship;
    }

    @Override
    public @NotNull Collection<CustomState> getStates() {
        return this.enemyRelationship.getStates();
    }

    @Override
    public @NotNull RelationshipStatus getStatus() {
        return RelationshipStatus.WAR;
    }

    public AbstractRelationship getEnemyRelationship() {
        return this.enemyRelationship;
    }

    public Set<WarTown> getTowns() {
        return this.warTown;
    }
}

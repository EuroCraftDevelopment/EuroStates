package org.eurostates.area.relationship.war;

import org.eurostates.area.relationship.AbstractRelationship;
import org.eurostates.area.relationship.Relationship;
import org.eurostates.area.relationship.RelationshipStatus;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.town.Town;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

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

    public Optional<WarTown> getTown(Town town1, Town town2) {
        return this.getTowns().parallelStream().filter(warTown -> Stream.of(warTown.getTowns()).allMatch(warSide -> warSide.getTown().equals(town1) || warSide.getTown().equals(town2))).findAny();
    }
}

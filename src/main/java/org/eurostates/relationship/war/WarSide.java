package org.eurostates.relationship.war;

import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.town.Town;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WarSide {

    private final @NotNull Town town;
    private double score;
    private final @NotNull Set<ESUser> defendants = new HashSet<>();

    public WarSide(@NotNull Town town) {
        this(town, 0, Collections.singletonList(town.getOwnerUser()));
    }

    public WarSide(@NotNull Town town, double score, Collection<ESUser> defendants) {
        this.score = score;
        this.town = town;
        this.defendants.addAll(defendants);
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Set<ESUser> getDefendants() {
        return this.defendants;
    }

    public @NotNull CustomState getState() {
        return this.town.getState();
    }

    public @NotNull Town getTown() {
        return this.town;
    }
}

package org.eurostates.area.relationship.war;

import org.eurostates.area.ESUser;
import org.eurostates.area.town.Town;

import java.util.HashSet;
import java.util.Set;

public class WarSide {

    private final Town town;
    private final Set<ESUser> players = new HashSet<>();
    private double score;

    public WarSide(Town town) {
        this(town, 0);
    }

    public WarSide(Town town, double score) {
        this.town = town;
        this.score = score;
    }

    public Town getTown() {
        return town;
    }

    public Set<ESUser> getPlayers() {
        return players;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}

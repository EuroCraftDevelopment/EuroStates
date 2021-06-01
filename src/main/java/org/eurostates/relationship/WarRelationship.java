package org.eurostates.relationship;

import org.eurostates.EuroStates;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.town.Town;
import org.eurostates.config.Config;
import org.eurostates.relationship.war.WarSide;
import org.eurostates.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class WarRelationship implements Relationship {

    private final @NotNull LocalDateTime time;
    private final Set<WarSide> warSides = new HashSet<>();

    @Deprecated
    public WarRelationship(@NotNull LocalDateTime time) {
        throw new RuntimeException("Requires WarSide");
    }

    public WarRelationship(@NotNull LocalDateTime time, WarSide... states) {
        this(time, Arrays.asList(states));
    }

    public WarRelationship(@NotNull LocalDateTime time, @NotNull Collection<WarSide> states) {
        this.time = time;
        this.warSides.addAll(states);
    }

    /*
    --Battle System--
1- Leader of a nation can start a battle on a town that belongs to another state that they are in a "war" with.
(One nation may do one battle at one given moment)
  a- Leader must be in some proximity of the town, calculated depending on distance to the "center". (distance adjustable globally or per-town, idk)
2- [Done] Battle continues for 6 hours (probs adjustable), and then the side with most points at the end of the battle, gets the town. The town's mayor is automatically set to the invader's leader if the invaders win.
  how to get points:
  a- [Done] Kill someone, 50 points (adjustable)
  b- [Done] Stay in the area, this will give you 10 points each 10 minutes for every person.
  c- [Done] Once a state has all their towns seized, they are disbanded, with all their members going back to nomads.
     */

    public @NotNull LocalDateTime getTimeStarted() {
        return this.time;
    }

    public @NotNull LocalDateTime getTimeEnding() {
        int configOverallTime = Utils.throwOr(IOException.class, () -> EuroStates.getPlugin().getConfiguration().parse(Config.WAR_TIME_OVERALL), 6 * 60);
        return getTimeStarted().plus(configOverallTime, ChronoUnit.MINUTES);
    }

    public Optional<WarSide> getWarSide(Town town) {
        return getWarSides()
                .parallelStream()
                .filter(warSide -> warSide.getTown().equals(town))
                .findAny();
    }

    public @NotNull Collection<WarSide> getWarSides() {
        return this.warSides;
    }

    @Override
    public @NotNull Collection<CustomState> getStates() {
        return this.warSides.parallelStream().map(WarSide::getState).collect(Collectors.toSet());
    }

    @Override
    public @NotNull RelationshipStatus getStatus() {
        return RelationshipStatus.ENEMY;
    }
}

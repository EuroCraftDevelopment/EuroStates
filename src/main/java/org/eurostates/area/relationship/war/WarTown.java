package org.eurostates.area.relationship.war;

import org.eurostates.EuroStates;
import org.eurostates.area.town.Town;
import org.eurostates.config.Config;
import org.eurostates.util.Utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class WarTown {

    private final WarRelationship relationship;
    private final WarSide targetTown;
    private final WarSide causeTown;
    private final LocalDateTime startTime;

    public WarTown(WarRelationship relationship, WarSide ownerTown, WarSide targetTown, LocalDateTime startTime) {
        this.relationship = relationship;
        this.targetTown = targetTown;
        this.causeTown = ownerTown;
        this.startTime = startTime;
    }

    public WarRelationship getRelationship() {
        return relationship;
    }

    public WarSide getTargetTown() {
        return targetTown;
    }

    public WarSide getCauseTown() {
        return causeTown;
    }

    public Optional<WarSide> getTown(Town town){
        if(this.getCauseTown().getTown().equals(town)){
            return Optional.of(this.getCauseTown());
        }
        if(this.getTargetTown().getTown().equals(town)){
            return Optional.of(this.getTargetTown());
        }
        return Optional.empty();
    }

    public WarSide[] getTowns() {
        return new WarSide[]{this.causeTown, this.targetTown};
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return this.startTime.plusMinutes(Utils.throwOr(IOException.class, () -> EuroStates.getPlugin().getConfiguration().parse(Config.WAR_TIME_OVERALL), 6 * 60));
    }
}

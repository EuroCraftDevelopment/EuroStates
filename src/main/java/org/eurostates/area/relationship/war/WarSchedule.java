package org.eurostates.area.relationship.war;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;
import org.eurostates.EuroStates;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.town.Town;
import org.eurostates.config.Config;
import org.eurostates.area.ownable.PlayerOwnable;
import org.eurostates.util.Utils;
import org.eurostates.util.array.ArrayIterable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class WarSchedule implements Runnable {

    private final WarTown warTown;

    public WarSchedule(WarTown warTown) {
        this.warTown = warTown;
    }

    public static @NotNull BukkitTask startSchedule(@NotNull WarTown relationship) {
        int time = Utils.throwOr(IOException.class, () -> EuroStates.getPlugin().getConfiguration().parse(Config.WAR_TIME_CLOSE_TOWN), 6);
        double ticks = TimeUnit.SECONDS.convert(time, TimeUnit.MINUTES) / 20.0;
        return Bukkit.getScheduler().runTaskLater(EuroStates.getPlugin(), new WarSchedule(relationship), (int) ticks);
    }

    @Override
    public void run() {
        int configCloseTown = Utils.throwOr(IOException.class, () -> EuroStates
                        .getPlugin()
                        .getConfiguration()
                        .parse(Config.WAR_SCORE_CLOSE_TOWN),
                6);

        int configDistanceTown = Utils.throwOr(IOException.class, () -> EuroStates
                .getPlugin()
                .getConfiguration()
                .parse(Config.WAR_DISTANCE_CLOSE_TOWN), 16);

        Stream.of(this.warTown.getTowns()).forEach(war -> {
            Block block = war.getTown().getCentre();
            int nearTownScore = war
                    .getPlayers()
                    .stream()
                    .map(PlayerOwnable::getOwner)
                    .filter(OfflinePlayer::isOnline)
                    .map(OfflinePlayer::getPlayer)
                    .filter(Objects::nonNull)
                    .map(p -> {
                        double dis = p.getLocation().distanceSquared(block.getLocation());
                        if (dis > configDistanceTown) {
                            return configCloseTown;
                        }
                        return 0;
                    })
                    .reduce(0, Integer::sum);

            war.setScore(war.getScore() + nearTownScore);
        });

        if (LocalDateTime.now().isBefore(this.warTown.getEndTime())) {
            startSchedule(this.warTown);
            return;
        }
        Set<WarSide> compare = Utils.getBest(new ArrayIterable<>(this.warTown.getTowns()), (Comparator<WarSide>) (o1, o2) -> (int) (o2.getScore() - o1.getScore()));
        if (compare.size() != 1) {
            return;
        }
        WarSide winning = compare.iterator().next();
        Stream.of(this.warTown.getTowns()).forEach(side -> {
            if (side.equals(winning)) {
                return;
            }
            Town town = side.getTown();
            if (town instanceof Town) {
                Town cTown = (Town) town;
                cTown.setOwner(winning.getTown().getOwner());
            }
            CustomState state = town.getState();
            state.getTowns().remove(town);
            winning.getTown().getState().getTowns().add(town);

            if (state.getTowns().isEmpty()) {
                state.delete();
            }
        });
    }
}
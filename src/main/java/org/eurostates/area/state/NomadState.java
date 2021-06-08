package org.eurostates.area.state;

import net.luckperms.api.model.group.Group;
import org.bukkit.ChatColor;
import org.eurostates.EuroStates;
import org.eurostates.area.town.Town;
import org.eurostates.area.relationship.Relationship;
import org.eurostates.area.relationship.war.WarRelationship;
import org.eurostates.area.technology.Technology;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * The Nomad State is the state that is used if no state can be found
 */
public class NomadState implements State {

    NomadState() {
        //so you can't call "new NomadState"
    }

    @Override
    public @NotNull String getTag() {
        return "NOMAD";
    }

    @Override
    public @NotNull String getName() {
        return "NOMAD";
    }

    @Override
    public char getLegacyChatColourCharacter() {
        return ChatColor.WHITE.getChar();
    }

    /**
     * NomadState cannot form relationships
     *
     * @return A empty collection
     * @deprecated Nomad cannot form relationships
     */
    @Override
    @Deprecated
    public Set<Relationship> getRelationships() {
        return Collections.emptySet();
    }

    /**
     * NomadState cannot form war
     *
     * @param town The town to check with at war
     * @return {@link Optional#empty()}
     * @deprecated Nomad cannot form relationships
     */
    @Override
    @Deprecated
    public Optional<WarRelationship> getWarWith(Town town) {
        return Optional.empty();
    }

    /**
     * NomadState does not have ranks
     *
     * @return A empty collection
     * @deprecated Nomad cannot have ranks
     */
    @Override
    @Deprecated
    public @NotNull Set<String> getRanks() {
        return Collections.emptySet();
    }

    /**
     * cannot get the citizens of NomadState
     *
     * @return a empty collection
     * @deprecated Cannot get the citizens of the NomadState
     */
    @Override
    @Deprecated
    public @NotNull Set<UUID> getCitizenIds() {
        return Collections.emptySet();
    }

    @Override
    public @NotNull CompletableFuture<Group> getOrCreateGroup() {
        return EuroStates.getLuckPermsApi().getGroupManager().createAndLoadGroup("Nomad");
    }

    @Override
    public @NotNull Optional<Group> getGroup() {
        return Optional.ofNullable(EuroStates.getLuckPermsApi().getGroupManager().getGroup("Nomad"));
    }

    /**
     * NomadState does not have towns
     *
     * @return A empty collection
     * @deprecated NomadState does not have towns
     */
    @Override
    @Deprecated
    public @NotNull Set<Town> getTowns() {
        return Collections.emptySet();
    }

    /**
     * NomadState will always have the base technology
     *
     * @return A empty collection
     * @deprecated NomadState does not have technology
     */
    @Override
    @Deprecated
    public @NotNull Set<Technology> getTechnology() {
        return Collections.emptySet();
    }
}

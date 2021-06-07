package org.eurostates.area.state;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.eurostates.EuroStates;
import org.eurostates.area.ESUser;
import org.eurostates.area.town.Town;
import org.eurostates.ownable.PlayerOwnable;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.Savable;
import org.eurostates.parser.area.state.GetterStateParser;
import org.eurostates.parser.area.state.LoadableStateParser;
import org.eurostates.relationship.AbstractRelationship;
import org.eurostates.relationship.Relationship;
import org.eurostates.relationship.RelationshipStatus;
import org.eurostates.relationship.war.WarRelationship;
import org.eurostates.technology.Technology;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CustomState implements State, PlayerOwnable, Savable<CustomState, Map<String, Object>, String> {

    private final @NotNull Set<Town> towns = new HashSet<>();
    private final @NotNull Set<UUID> users = new HashSet<>();
    private final @NotNull Set<Technology> technologies = new HashSet<>();
    private final @NotNull UUID id;
    private final String permissionGroupName;
    private @NotNull String tag;
    private @NotNull String name;
    private @NotNull String currency;
    private char chatColour;
    private @NotNull UUID owner;

    @Deprecated
    public CustomState(@NotNull UUID id, @NotNull String tag, @NotNull String name, @NotNull String currency, char chatColour, UUID uuid) {
        this(id, tag, name, currency, chatColour, Parsers.GETTER_USER.fromId(uuid), name);
    }

    public CustomState(@NotNull UUID id, @NotNull String tag, @NotNull String name, @NotNull String currency, char chatColour, @NotNull ESUser owner, String permissionGroupName) {
        if (tag.length() != 3) {
            throw new IllegalArgumentException("Tag must be 3 letters long");
        }
        this.tag = tag;
        this.name = name;
        this.currency = currency;
        this.chatColour = chatColour;
        this.id = id;
        this.permissionGroupName = permissionGroupName;
        this.owner = owner.getOwnerId();
        this.register(owner);
    }

    public @NotNull UUID getId() {
        return this.id;
    }

    @Override
    public @NotNull Optional<Group> getGroup() {
        GroupManager api = EuroStates.getLuckPermsApi().getGroupManager();
        Group group = api.getGroup(this.getGroupName());
        if (group == null) {
            return Optional.empty();
        }
        return Optional.of(group);
    }

    @Override
    public @NotNull CompletableFuture<Group> getOrCreateGroup() {
        GroupManager api = EuroStates.getLuckPermsApi().getGroupManager();
        return api.createAndLoadGroup(this.getGroupName());
    }

    public String getGroupName() {
        return permissionGroupName;
    }

    public void updatePermissions() {
        CompletableFuture<Group> groupFuture = getOrCreateGroup();
        groupFuture.thenRun(() -> {
            Group group;
            try {
                group = groupFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return;
            }
            CustomState
                    .this
                    .getTechnology()
                    .stream()
                    .flatMap(tec -> tec.getPermissions().parallelStream())
                    .map(str -> Node.builder(str).build())
                    .forEach(node -> group.data().add(node));
            EuroStates.getLuckPermsApi().getGroupManager().saveGroup(group);
        });
    }

    public @NotNull UUID getOwnerId() {
        return this.owner;
    }

    public void setChatColour(char chatColour) {
        this.chatColour = chatColour;
    }

    @Deprecated
    public void setOwner(@NotNull UUID owner) {
        this.owner = owner;
    }

    public void setOwner(@NotNull OfflinePlayer owner) {
        this.owner = owner.getUniqueId();
    }

    @Override
    public @NotNull String getTag() {
        return this.tag;
    }

    public void setTag(@NotNull String tag) {
        this.tag = tag;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getCurrency() {
        return this.currency;
    }

    public void setCurrency(@NotNull String currency) {
        this.currency = currency;
    }

    @Override
    public char getLegacyChatColourCharacter() {
        return this.chatColour;
    }

    @Override
    public @NotNull Set<UUID> getCitizenIds() {
        return this.users;
    }

    public Relationship getRelationship(CustomState state) {
        return getRelationships()
                .parallelStream()
                .filter(r -> r.getStates().contains(state))
                .findAny().orElseGet(() -> new AbstractRelationship(RelationshipStatus.NEUTRAL, CustomState.this, state));
    }

    public Optional<WarRelationship> getWarWith(Town town) {
        Relationship relationship = getRelationship(town.getState());
        if (!(relationship instanceof WarRelationship)) {
            return Optional.empty();
        }
        WarRelationship war = (WarRelationship) relationship;
        if (war.getTowns().parallelStream().anyMatch(s -> s.getTargetTown().getTown().equals(town))) {
            return Optional.empty();
        }
        return Optional.of(war);
    }

    public Set<User> getLuckPermsCitizens() {
        return this.users.stream().map(user -> EuroStates.getLuckPermsApi().getUserManager().getUser(user)).collect(Collectors.toSet());
    }

    public void register(ESUser user) {
        this.users.add(user.getOwnerId());
        CompletableFuture<Group> groupFuture = this.getOrCreateGroup();
        groupFuture.thenRun(() -> {
            Group group;
            try {
                group = groupFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return;
            }
            UserManager userManager = EuroStates.getLuckPermsApi().getUserManager();
            User luckUser = userManager.getUser(user.getOwnerId());
            if (luckUser == null) {
                return;
            }
            luckUser.setPrimaryGroup(group.getName());
            userManager.saveUser(luckUser);
        });
    }

    @Override
    public void save(@NotNull File file) throws IOException {
        this.getEuroStatesCitizens().forEach(u -> {
            try {
                u.save();
            } catch (IOException e) {
                System.err.println("Couldn't save player " + u.getOwner().getName());
                e.printStackTrace();
            }
        });
        Savable.super.save(file);
    }

    @Override
    public @NotNull Set<Town> getTowns() {
        return this.towns;
    }

    @Override
    public @NotNull Set<Technology> getTechnology() {
        return this.technologies;
    }

    @Override
    public @NotNull File getFile() {
        return new File(EuroStates.getPlugin().getDataFolder(), "data/state/" + this.getId() + ".yml");
    }

    @Override
    public @NotNull LoadableStateParser getSerializableParser() {
        return Parsers.LOADABLE_STATE;
    }

    @Override
    public @NotNull GetterStateParser getIdParser() {
        return Parsers.GETTER_STATE;
    }

    @Override
    public @NotNull String getRootNode() {
        return "State";
    }

    public void delete() {
        File file = this.getFile();
        boolean deleted = file.delete();
        if (!deleted) Bukkit.getLogger().warning("Could not delete state file: " + file);
        States.CUSTOM_STATES.remove(this);
    }
}

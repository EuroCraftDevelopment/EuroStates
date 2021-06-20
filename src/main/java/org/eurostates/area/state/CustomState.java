package org.eurostates.area.state;

import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.eurostates.EuroStates;
import org.eurostates.area.ESUser;
import org.eurostates.area.ownable.PlayerOwnable;
import org.eurostates.area.relationship.AbstractRelationship;
import org.eurostates.area.relationship.Relationship;
import org.eurostates.area.relationship.RelationshipStatus;
import org.eurostates.area.relationship.war.WarRelationship;
import org.eurostates.area.technology.Technology;
import org.eurostates.area.town.Town;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.Savable;
import org.eurostates.parser.area.state.GetterStateParser;
import org.eurostates.parser.area.state.LoadableStateParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * A custom state created by a user within the server. @see State for more info
 */
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

    /**
     * @param id         id of the custom state, if new use {@link UUID#randomUUID()}
     * @param tag        Tag of the state, this can change. Requires to be 3 characters
     * @param name       Name of the state
     * @param currency   The currency name
     * @param chatColour The colour of the players chat when a citizen of this state sends a message
     * @param uuid       The UUID of the owner
     * @deprecated Old constructor, do not use
     */
    @Deprecated
    public CustomState(@NotNull UUID id, @NotNull String tag, @NotNull String name, @NotNull String currency, char chatColour, UUID uuid) {
        this(id, tag, name, currency, chatColour, Parsers.GETTER_USER.fromId(uuid), name);
    }

    /**
     * @param id                  Id of the custom state, if new use {@link UUID#randomUUID()}
     * @param tag                 Tag of the state, this can change. Requires to be 3 characters
     * @param name                Name of the state
     * @param currency            The currency name
     * @param chatColour          The colour of the players chat when a citizen of this state sends a message
     * @param owner               The owner of the state
     * @param permissionGroupName The permission group name in LuckPerms
     */
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

    /**
     * Gets the Id of the state
     *
     * @return A UUID of the state
     */
    public @NotNull UUID getId() {
        return this.id;
    }

    /**
     * Gets the LuckPerms group of the state
     *
     * @return A LuckPerms Group object of the state
     */
    @Override
    public @NotNull Optional<Group> getGroup() {
        GroupManager api = EuroStates.getLuckPermsApi().getGroupManager();
        Group group = api.getGroup(this.getGroupName());
        if (group == null) {
            return Optional.empty();
        }
        return Optional.of(group);
    }

    /**
     * Gets or creates the LuckPerms group of the state
     *
     * @return A LuckPerms group object of the state
     */
    @Override
    public @NotNull CompletableFuture<Group> getOrCreateGroup() {
        GroupManager api = EuroStates.getLuckPermsApi().getGroupManager();
        return api.createAndLoadGroup(this.getGroupName());
    }

    /**
     * Gets the LuckPerms group name of the state
     *
     * @return The name of the LuckPerms group
     */
    public String getGroupName() {
        return permissionGroupName;
    }

    /**
     * Updates the LuckPerms group with the permissions from the technologies applied to this state
     */
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

    /**
     * Gets the owners UUID
     *
     * @return The UUID of the owner
     */
    public @NotNull UUID getOwnerId() {
        return this.owner;
    }

    /**
     * Sets the chat colour of the state via the legacy minecraft chat colour character. This will show when a player sends a message who belongs to this state.
     *
     * @param chatColour The legacy character colour
     */
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

    @Override
    public Set<Relationship> getRelationships() {
        return EuroStates.getPlugin().getRelationships().parallelStream().filter(r -> r.getStates().contains(CustomState.this)).collect(Collectors.toSet());
    }

    public void register(ESUser user) {
        this.users.add(user.getOwnerId());
        UserManager userManager = EuroStates.getLuckPermsApi().getUserManager();
        CompletableFuture<Group> groupFuture = this.getOrCreateGroup();
        CompletableFuture<User> userFuture = userManager.loadUser(user.getOwnerId());
        CompletableFuture<Void> future = CompletableFuture.allOf(groupFuture, userFuture);
        future.thenRunAsync(() -> {
            Group group;
            User luckUser;
            System.out.println("Both futures complete");
            try {
                group = groupFuture.get();
                luckUser = userFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
                throw new IllegalStateException("The impossible happened", e);
            }

            InheritanceNode node = InheritanceNode.builder().group(group).value(true).build();
            NodeMap data = luckUser.data();
            DataMutateResult dataResult = data.add(node);
            System.out.println("dataResult: " + dataResult.name());
            if (dataResult.wasSuccessful()) {
                userManager.saveUser(luckUser).thenRunAsync(() -> {
                    System.out.println("Saved the user");
                });
            }
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

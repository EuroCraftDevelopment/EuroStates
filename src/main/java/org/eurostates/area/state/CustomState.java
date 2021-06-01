package org.eurostates.area.state;

import org.bukkit.OfflinePlayer;
import org.eurostates.EuroStates;
import org.eurostates.area.ESUser;
import org.eurostates.area.town.Town;
import org.eurostates.ownable.PlayerOwnable;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.Savable;
import org.eurostates.parser.area.state.GetterStateParser;
import org.eurostates.parser.area.state.LoadableStateParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class CustomState implements State, PlayerOwnable, Savable<CustomState, Map<String, Object>, String> {

    private final @NotNull Set<Town> towns = new HashSet<>();
    private final @NotNull Set<String> permissions = new HashSet<>();
    private final @NotNull Set<ESUser> users = new HashSet<>();
    private final @NotNull UUID id;
    private @NotNull String tag;
    private @NotNull String name;
    private char chatColour;
    private @NotNull UUID owner;

    public CustomState(@NotNull UUID id, @NotNull String tag, @NotNull String name, char chatColour, @NotNull UUID owner) {
        this(id, tag, name, chatColour, owner, Collections.emptySet());
    }

    public CustomState(@NotNull UUID id, @NotNull String tag, @NotNull String name, char chatColour, @NotNull UUID owner, @NotNull Collection<String> permissions) {
        if (tag.length() != 3) {
            throw new IllegalArgumentException("Tag must be 3 letters long");
        }
        this.tag = tag;
        this.name = name;
        this.chatColour = chatColour;
        this.owner = owner;
        this.permissions.addAll(permissions);
        this.id = id;
    }

    public @NotNull UUID getId() {
        return this.id;
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

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public char getLegacyChatColourCharacter() {
        return this.chatColour;
    }

    @Override
    public @NotNull Set<ESUser> getEuroStatesCitizens() {
        return this.users;
    }

    @Override
    public @NotNull Set<Town> getTowns() {
        return this.towns;
    }

    @Override
    public @NotNull Set<String> getPermissions() {
        return this.permissions;
    }

    @Override
    public @NotNull File getFile() {
        return new File(EuroStates.getPlugin().getDataFolder(), "data/state/" + this.getId().toString() + ".yml");
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
}

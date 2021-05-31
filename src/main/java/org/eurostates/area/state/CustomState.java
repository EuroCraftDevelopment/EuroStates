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

import java.io.File;
import java.util.*;

public class CustomState implements State, PlayerOwnable, Savable<CustomState, Map<String, Object>, String> {

    private final Set<Town> towns = new HashSet<>();
    private final Set<String> permissions = new HashSet<>();
    private final Set<ESUser> users = new HashSet<>();
    private final UUID id;
    private String tag;
    private String name;
    private char chatColour;
    private UUID owner;

    public CustomState(UUID id, String tag, String name, char chatColour, UUID owner) {
        this(id, tag, name, chatColour, owner, Collections.emptySet());
    }

    public CustomState(UUID id, String tag, String name, char chatColour, UUID owner, Collection<String> permissions) {
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

    public UUID getId() {
        return this.id;
    }

    public UUID getOwnerId() {
        return this.owner;
    }

    public void setChatColour(char chatColour) {
        this.chatColour = chatColour;
    }

    @Deprecated
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void setOwner(OfflinePlayer owner) {
        this.owner = owner.getUniqueId();
    }

    @Override
    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public char getLegacyChatColourCharacter() {
        return this.chatColour;
    }

    @Override
    public Set<ESUser> getEuroStatesCitizens() {
        return this.users;
    }

    @Override
    public Set<Town> getTowns() {
        return this.towns;
    }

    @Override
    public Set<String> getPermissions() {
        return this.permissions;
    }

    @Override
    public File getFile() {
        return new File(EuroStates.getPlugin().getDataFolder(), "data/state/" + this.getId().toString() + ".yml");
    }

    @Override
    public LoadableStateParser getSerializableParser() {
        return Parsers.LOADABLE_STATE;
    }

    @Override
    public GetterStateParser getIdParser() {
        return Parsers.GETTER_STATE;
    }

    @Override
    public String getRootNode() {
        return "State";
    }
}

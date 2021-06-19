package org.eurostates.area.technology;

import org.eurostates.EuroStates;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.Savable;
import org.eurostates.parser.Serializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Technology implements Savable<Technology, Map<String, Object>, String> {

    private final UUID id;
    private String identifier;
    private String name;
    private final Set<Technology> requirements = new HashSet<>();
    private final Set<String> permissions = new HashSet<>();

    public Technology(@NotNull UUID id, @NotNull String identifier, @NotNull String name) {
        this(id, identifier, name, Collections.emptySet(), Collections.emptySet());
    }

    public Technology(@NotNull UUID id, @NotNull String identifier, @Nullable String name,
                      @NotNull Collection<String> permissions, @NotNull Collection<Technology> technologies) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.permissions.addAll(permissions);
        this.requirements.addAll(technologies);
    }

    public UUID getID() {
        return this.id;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(@NotNull String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return this.name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void setPermissions(@NotNull Collection<String> permissions) {
        this.permissions.clear();
        this.permissions.addAll(permissions);
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

    @Deprecated
    public Set<String> getRequirements() {
        return this.requirements.parallelStream().map(tec -> tec.identifier).collect(Collectors.toSet());
    }

    public Set<Technology> getDependents(){
        return this.requirements;
    }

    @Override
    public @NotNull File getFile() {
        return new File(EuroStates.getPlugin().getDataFolder(), "data/technology/" + Parsers.UUID.to(this.id) + ".yml");
    }

    //TODO
    @Override
    public @NotNull Serializable<Technology, Map<String, Object>> getSerializableParser() {
        return null;
    }

    //TODO
    @Override
    public @NotNull Serializable<Technology, String> getIdParser() {
        return null;
    }

    //TODO
    @Override
    public @NotNull String getRootNode() {
        return null;
    }
}

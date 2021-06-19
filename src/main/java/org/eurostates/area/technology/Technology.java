package org.eurostates.area.technology;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.eurostates.EuroStates;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.Savable;
import org.eurostates.parser.Serializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Technology implements Savable<Technology, Map<String, Object>, String> {

    private final UUID id;
    private String name;
    private String description;
    private final Set<Technology> requirements = new HashSet<>();
    private final Set<String> permissions = new HashSet<>();

    public Technology(@NotNull UUID id, @NotNull String name) {
        this(id, name, null, Collections.emptySet(), Collections.emptySet());
    }

    public Technology(@NotNull UUID id, @NotNull String name, @Nullable String description,
                      @NotNull Collection<String> permissions, @NotNull Collection<Technology> technologies) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions.addAll(permissions);
        this.requirements.addAll(technologies);
    }

    public UUID getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(this.description);
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
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
        return this.requirements.parallelStream().map(tec -> tec.name).collect(Collectors.toSet());
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

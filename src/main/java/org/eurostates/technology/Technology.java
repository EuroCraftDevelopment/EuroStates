package org.eurostates.technology;

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

    public static final String ID_NODE = "id";
    public static final String DESC_NODE = "description";
    public static final String REQ_NODE = "requirements";
    public static final String PERM_NODE = "permissions";

    public Technology(@NotNull UUID id, @NotNull String name) {
        this(id, name, null, Collections.emptySet());
    }

    public Technology(@NotNull UUID id, @NotNull String name, @Nullable String description, @NotNull Collection<String> permissions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions.addAll(permissions);
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

    @Deprecated //magic values! stop!
    public void setRequirements(Collection<String> requirements) {
        throw new RuntimeException("NO MAGIC VALUE");
    }

    @Deprecated
    public Set<String> getRequirements() {
        return this.requirements.parallelStream().map(tec -> tec.name).collect(Collectors.toSet());
    }

    @Deprecated
    public static File getFile(String id) {
        Plugin plugin = EuroStates.getPlugin();
        return new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "technology" + File.separator + id + ".yml");
    }

    @Deprecated
    public static Technology getFromFile(String id) throws IOException {
        /*Technology technology = new Technology(id);
        File file = getFile(id);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        technology.setID(ParseLoadedData.getString(config, ID_NODE));
        technology.setPermissions(new HashSet<>(ParseLoadedData.getStringList(config, PERM_NODE)));
        technology.setRequirements(new HashSet<>(ParseLoadedData.getStringList(config, REQ_NODE)));
        return technology;*/
        throw new RuntimeException("Not implemented");
    }

    @Deprecated
    public void saveToFile(String id) throws IOException {
        File file = getFile(id);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set(ID_NODE, this.id);
        config.set(DESC_NODE, this.description);
        config.set(REQ_NODE, this.requirements);
        config.set(PERM_NODE, this.permissions);

        config.save(file);
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

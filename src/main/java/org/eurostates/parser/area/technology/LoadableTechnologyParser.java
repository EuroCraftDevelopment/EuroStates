package org.eurostates.parser.area.technology;

import org.bukkit.configuration.file.YamlConfiguration;
import org.eurostates.area.exceptions.TechRequirementNotLoaded;
import org.eurostates.area.technology.Technology;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.StringMapParser;
import org.eurostates.util.lamda.throwable.bi.ThrowableBiFunction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LoadableTechnologyParser implements StringMapParser<Technology> {
    public static final String ID_NODE = "id";
    public static final String NAME_NODE = "name";
    public static final String DESCRIPTION_NODE = "description";

    public static final String REQUIREMENTS_NODE = "requirements";
    public static final String PERMISSIONS_NODE = "permissions";

    @Override
    public @NotNull Map<String, Object> to(@NotNull Technology from) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put(ID_NODE, Parsers.UUID.to(from.getID()));
        map.put(NAME_NODE, from.getIdentifier());
        map.put(DESCRIPTION_NODE, from.getName());

        map.put(REQUIREMENTS_NODE, from
            .getDependents()
            .parallelStream()
            .map(tech -> tech.getID().toString())
            .collect(Collectors.toList()));

        map.put(PERMISSIONS_NODE, new ArrayList<String>(from.getPermissions()));

        return map;
    }

    @Override
    public @NotNull Technology from(@NotNull Map<String, Object> from) throws IOException {
        UUID id = Parsers.UUID.from(get(from, ID_NODE));
        String name = get(from, NAME_NODE);
        String description = get(from, DESCRIPTION_NODE);

        Set<String> permissions = new HashSet<>(get(from, PERMISSIONS_NODE));

        Set<String> stringRequirements = new HashSet<>(get(from, REQUIREMENTS_NODE));

        Set<Technology> requirements;
        try {
            requirements = new HashSet<>(Parsers.collectFromOrThrow(Parsers.GETTER_TECHNOLOGY, stringRequirements));
        } catch (IOException e) {
            throw new TechRequirementNotLoaded();
        }

        return new Technology(id, name, description, permissions, requirements);
    }

    @Override
    public @NotNull Map<String, ThrowableBiFunction<YamlConfiguration, String, ?, IOException>> getParser() {
        Map<String, ThrowableBiFunction<YamlConfiguration, String, ?, IOException>> map = new HashMap<>();

        map.put(ID_NODE, YamlConfiguration::getString);
        map.put(NAME_NODE, YamlConfiguration::getString);
        map.put(DESCRIPTION_NODE, YamlConfiguration::getString);

        map.put(REQUIREMENTS_NODE, YamlConfiguration::getStringList);
        map.put(PERMISSIONS_NODE, YamlConfiguration::getStringList);

        return map;
    }

    private <T> T get(Map<String, Object> map, String node) throws IOException {
        Object obj = map.get(node);
        if (obj == null) {
            throw new IOException("Cannot find node of " + node + " in map:\n" + map.entrySet().parallelStream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining("\n")));
        }
        return (T) obj;
    }
}

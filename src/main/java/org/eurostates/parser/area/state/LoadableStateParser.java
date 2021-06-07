package org.eurostates.parser.area.state;

import org.bukkit.configuration.file.YamlConfiguration;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.town.CustomTown;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.StringMapParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class LoadableStateParser implements StringMapParser<CustomState> {

    public static final String TAG_NODE = "tag";
    public static final String NAME_NODE = "name";
    public static final String OWNER_NODE = "owner";
    public static final String COLOUR_NODE = "colour";
    public static final String ID_NODE = "id";
    public static final String CURRENCY_NODE = "currency";

    public static final String CITIZENS_NODE = "citizens";
    public static final String TECHNOLOGY_NODE = "technology";
    public static final String TOWNS_NODE = "towns";

    @Override
    public @NotNull Map<String, BiFunction<YamlConfiguration, String, ?>> getParser() {
        Map<String, BiFunction<YamlConfiguration, String, ?>> map = new HashMap<>();
        map.put(TAG_NODE, YamlConfiguration::getString);
        map.put(NAME_NODE, YamlConfiguration::getString);
        map.put(OWNER_NODE, YamlConfiguration::getString);
        map.put(COLOUR_NODE, YamlConfiguration::getString);
        map.put(ID_NODE, YamlConfiguration::getString);
        map.put(CURRENCY_NODE, YamlConfiguration::getString);

        map.put(CITIZENS_NODE, YamlConfiguration::getStringList);
        map.put(TOWNS_NODE, YamlConfiguration::getStringList);
        return map;
    }

    @Override
    public @NotNull Map<String, Object> to(@NotNull CustomState from) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put(ID_NODE, Parsers.UUID.to(from.getId()));
        map.put(TAG_NODE, from.getTag());
        map.put(NAME_NODE, from.getName());
        map.put(OWNER_NODE, Parsers.UUID.to(from.getOwnerId()));
        map.put(COLOUR_NODE, from.getLegacyChatColourCharacter() + "");
        //map.put(TECHNOLOGY_NODE, from.getTechnology());
        map.put(CITIZENS_NODE, Parsers.collectToOrThrow(Parsers.UUID, from.getCitizenIds()));
        map.put(CURRENCY_NODE, from.getCurrency());
        map.put(TOWNS_NODE, from
                .getTowns()
                .parallelStream()
                .filter(t -> t instanceof CustomTown)
                .map(t -> ((CustomTown) t).getId().toString())
                .collect(Collectors.toList()));
        return map;
    }

    @Override
    public @NotNull CustomState from(@NotNull Map<String, Object> from) throws IOException {
        UUID id = Parsers.UUID.from(get(from, ID_NODE));
        String tag = get(from, TAG_NODE);
        String name = get(from, NAME_NODE);
        String currency = get(from, CURRENCY_NODE);
        UUID owner = Parsers.UUID.from(get(from, OWNER_NODE));
        char colour = this.<String>get(from, COLOUR_NODE).charAt(0);
        CustomState state;
        try {
            state = new CustomState(id, tag, name, currency, colour, owner);
        } catch (Throwable e) {
            throw new IOException(e);
        }
        //List<String> permissions = get(from, PERMISSIONS_NODE);
        List<String> citizenIdsStr = get(from, CITIZENS_NODE);
        List<UUID> users = Parsers.collectFromOrThrow(Parsers.UUID, citizenIdsStr);
        state.getCitizenIds().addAll(users);
        return state;
    }

    private <T> T get(Map<String, Object> map, String node) throws IOException {
        Object obj = map.get(node);
        if (obj == null) {
            throw new IOException("Cannot find node of " + node + " in map:\n" + map.entrySet().parallelStream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining("\n")));
        }
        return (T) obj;
    }
}

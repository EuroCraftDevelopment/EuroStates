package org.eurostates.parser.area.state;

import org.eurostates.area.ESUser;
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
import java.util.stream.Collectors;

public class LoadableStateParser implements StringMapParser<CustomState> {

    public static final String TAG_NODE = "meta.tag";
    public static final String NAME_NODE = "meta.name";
    public static final String OWNER_NODE = "meta.owner";
    public static final String COLOUR_NODE = "meta.colour";
    public static final String ID_NODE = "meta.id";
    public static final String CURRENCY_NODE = "meta.currency";

    public static final String CITIZENS_NODE = "citizens";
    public static final String PERMISSIONS_NODE = "permissions";
    public static final String TOWNS_NODE = "towns";

    @Override
    public @NotNull Map<String, Object> to(@NotNull CustomState from) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put(ID_NODE, Parsers.UUID.to(from.getId()));
        map.put(TAG_NODE, from.getTag());
        map.put(NAME_NODE, from.getName());
        map.put(OWNER_NODE, Parsers.UUID.to(from.getOwnerId()));
        map.put(COLOUR_NODE, from.getLegacyChatColourCharacter() + "");
        map.put(CURRENCY_NODE, from.getCurrency());
        map.put(PERMISSIONS_NODE, from.getPermissions());
        map.put(CITIZENS_NODE, Parsers.collectToOrThrow(Parsers.LOADABLE_USER, from.getEuroStatesCitizens()));
        map.put(TOWNS_NODE, from
                .getTowns()
                .parallelStream()
                .filter(t -> t instanceof CustomTown)
                .map(t -> ((CustomTown) t).getId().toString())
                .collect(Collectors.toSet()));
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
        List<String> permissions = get(from, PERMISSIONS_NODE);
        List<ESUser> users = Parsers.collectFromOrThrow(Parsers.LOADABLE_USER, get(from, CITIZENS_NODE));
        state.getEuroStatesCitizens().addAll(users);
        state.getPermissions().addAll(permissions);
        return state;
    }

    private <T> T get(Map<String, Object> map, String node) {
        Object obj = map.get(node);
        if (obj == null) {
            throw new NullPointerException("Cannot find node of " + node + " in map");
        }
        return (T) obj;
    }
}

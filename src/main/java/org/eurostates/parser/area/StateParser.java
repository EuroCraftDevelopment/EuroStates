package org.eurostates.parser.area;

import org.eurostates.area.Rank;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.town.CustomTown;
import org.eurostates.parser.StringMapParser;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StateParser implements StringMapParser<CustomState> {

    public static final String TAG_NODE = "meta.tag";
    public static final String NAME_NODE = "meta.name";
    public static final String OWNER_NODE = "meta.owner";
    public static final String COLOUR_NODE = "meta.colour";
    public static final String ID_NODE = "meta.id";

    public static final String PERMISSIONS_NODE = "permissions";
    public static final String TOWNS_NODE = "towns";
    public static final String RANKS_NODE = "ranks";

    @Override
    public Map<String, Object> to(CustomState from) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID_NODE, from.getId().toString());
        map.put(TAG_NODE, from.getTag());
        map.put(NAME_NODE, from.getName());
        map.put(OWNER_NODE, from.getOwnerId().toString());
        map.put(COLOUR_NODE, from.getLegacyChatColourCharacter() + "");

        map.put(PERMISSIONS_NODE, from.getPermissions());
        map.put(TOWNS_NODE, from
                .getTowns()
                .parallelStream()
                .filter(t -> t instanceof CustomTown)
                .map(t -> ((CustomTown) t).getId().toString())
                .collect(Collectors.toSet()));
        map.put(RANKS_NODE, from
                .getRanks()
                .parallelStream()
                .map(rank -> rank.getId().toString() + "||" + rank.getDisplay())
                .collect(Collectors.toSet()));
        return map;
    }

    //should make this throwable safe

    @Override
    public CustomState from(Map<String, Object> from) throws NullPointerException {
        UUID id = UUID.fromString(get(from, ID_NODE));
        String tag = get(from, TAG_NODE);
        String name = get(from, NAME_NODE);
        UUID owner = get(from, OWNER_NODE);
        char colour = this.<String>get(from, COLOUR_NODE).charAt(0);

        CustomState state = new CustomState(id, tag, name, colour, owner);

        List<String> permissions = get(from, PERMISSIONS_NODE);
        Set<UUID> towns = this.<List<String>>get(from, TOWNS_NODE).parallelStream().map(UUID::fromString).collect(Collectors.toSet());
        Set<Rank> ranks = this.<List<String>>get(from, RANKS_NODE).parallelStream().map(str -> {
            String[] split = str.split(Pattern.quote("||"));
            UUID rankId = UUID.fromString(split[0]);
            String display = split[1];
            return new Rank(state, rankId, display);
        }).collect(Collectors.toSet());

        state.getPermissions().addAll(permissions);
        //load towns
        state.getRanks().addAll(ranks);
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

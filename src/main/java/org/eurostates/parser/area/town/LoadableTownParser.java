package org.eurostates.parser.area.town;

import org.bukkit.block.Block;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.town.CustomTown;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.StringMapParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoadableTownParser implements StringMapParser<CustomTown> {

    public static final String UUID_NODE = "Id";
    public static final String TAG_NODE = "Tag";
    public static final String NAME_NODE = "Name";
    public static final String OWNER_NODE = "Owner";
    public static final String STATE_NODE = "State";
    public static final String CENTRE_NODE = "Centre";

    @Override
    public @NotNull Map<String, Object> to(@NotNull CustomTown from) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put(UUID_NODE, Parsers.UUID.to(from.getId()));
        map.put(TAG_NODE, from.getTag());
        map.put(NAME_NODE, from.getName());
        map.put(OWNER_NODE, from.getOwnerId());
        map.put(STATE_NODE, from.getStateId());
        map.put(CENTRE_NODE, Parsers.BLOCK_LOCATION.to(from.getCentre()));
        return map;
    }

    @Override
    public @NotNull CustomTown from(@NotNull Map<String, Object> from) throws IOException {
        UUID id = Parsers.UUID.from(notNull((String) from.get(UUID_NODE), "No Town Id found"));
        String tag = notNull((String) from.get(TAG_NODE), "No tag node found");
        String name = notNull((String) from.get(NAME_NODE), "No name node found");
        UUID owner = Parsers.UUID.from(notNull((String) from.get(OWNER_NODE), "No Owner found"));
        CustomState state = Parsers.GETTER_STATE.from(notNull((String) from.get(STATE_NODE), "No State Id found"));
        Block centre = Parsers.BLOCK_LOCATION.from(notNull((Map<String, Object>) from.get(CENTRE_NODE), "No centre found"));
        return new CustomTown(id, tag, name, owner, state, centre);
    }

    private <T> T notNull(T value, String error) throws IOException {
        if (value == null) {
            throw new IOException(error);
        }
        return value;
    }
}

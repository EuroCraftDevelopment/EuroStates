package org.eurostates.parser.area.user;

import org.eurostates.area.ESUser;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.StringMapParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//TODO
public class LoadableUserParser implements StringMapParser<ESUser> {

    private static final String UUID_NODE = "UUID";
    private static final String RANK_NODE = "Rank";

    @Override
    public @NotNull Map<String, Object> to(@NotNull ESUser from) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put(UUID_NODE, Parsers.UUID.to(from.getOwnerId()));
        from.getAssignedRank().ifPresent(t -> map.put(RANK_NODE, t));
        return map;
    }

    @Override
    public @NotNull ESUser from(@NotNull Map<String, Object> from) throws IOException {
        UUID uuid = Parsers.UUID.from(notNull((String) from.get(UUID_NODE)));
        String rank = (String) from.get(UUID_NODE);
        return new ESUser(uuid, rank);
    }

    private <T> T notNull(T value) throws IOException {
        if (value == null) {
            throw new IOException("Unknown UUID");
        }
        return value;
    }
}

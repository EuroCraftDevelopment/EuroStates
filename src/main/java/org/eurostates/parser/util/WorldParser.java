package org.eurostates.parser.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.StringParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class WorldParser implements StringParser<World> {
    @Override
    public @NotNull String to(@NotNull World from) throws IOException {
        return Parsers.UUID.to(from.getUID());
    }

    @Override
    public @NotNull World from(@NotNull String from) throws IOException {
        UUID uuid = Parsers.UUID.from(from);
        World world = Bukkit.getWorld(uuid);
        if (world == null) {
            throw new IOException("Unknown world with id of " + uuid);
        }
        return world;
    }
}

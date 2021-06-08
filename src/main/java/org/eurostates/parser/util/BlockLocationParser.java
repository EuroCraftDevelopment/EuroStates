package org.eurostates.parser.util;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.eurostates.util.lamda.throwable.bi.ThrowableBiFunction;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.StringMapParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BlockLocationParser implements StringMapParser<Block> {

    public static final String X = "X";
    public static final String Y = "Y";
    public static final String Z = "Z";
    public static final String WORLD = "World";

    @Override
    public @NotNull Map<String, ThrowableBiFunction<YamlConfiguration, String, ?, IOException>> getParser() {
        Map<String, ThrowableBiFunction<YamlConfiguration, String, ?, IOException>> map = new HashMap<>();
        map.put(X, YamlConfiguration::getInt);
        map.put(Y, YamlConfiguration::getInt);
        map.put(Z, YamlConfiguration::getInt);
        map.put(WORLD, YamlConfiguration::getString);
        return map;
    }

    @Override
    public @NotNull Map<String, Object> to(@NotNull Block from) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put(X, from.getX());
        map.put(Y, from.getY());
        map.put(Z, from.getZ());
        map.put(WORLD, Parsers.WORLD.to(notNull(from.getWorld(), "World is null in location")));
        return map;
    }

    @Override
    public @NotNull Block from(@NotNull Map<String, Object> from) throws IOException {
        int x = (int) notNull(from.get(X), "No X value");
        int y = (int) notNull(from.get(Y), "No Y value");
        int z = (int) notNull(from.get(Z), "No Z value");
        World world = Parsers.WORLD.from((String) notNull(from.get(WORLD), "No world value"));
        return world.getBlockAt(x, y, z);
    }

    private <T> T notNull(T value, String error) throws IOException {
        if (value == null) {
            throw new IOException(error);
        }
        return value;
    }
}

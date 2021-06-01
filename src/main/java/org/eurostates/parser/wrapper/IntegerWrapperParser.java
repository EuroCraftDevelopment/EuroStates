package org.eurostates.parser.wrapper;

import org.bukkit.configuration.file.YamlConfiguration;
import org.eurostates.parser.Serializable;
import org.jetbrains.annotations.NotNull;

public class IntegerWrapperParser implements Serializable<Integer, Integer> {
    @Override
    public @NotNull Integer to(@NotNull Integer from) {
        return from;
    }

    @Override
    public @NotNull Integer from(@NotNull Integer from) {
        return from;
    }

    @Override
    public void serialize(@NotNull YamlConfiguration yaml, @NotNull String node, @NotNull Integer value) {
        yaml.set(node, value);
    }

    @Override
    public Integer deserialize(@NotNull YamlConfiguration yaml, @NotNull String node) {
        return yaml.getInt(node);
    }
}

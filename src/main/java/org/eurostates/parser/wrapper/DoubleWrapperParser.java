package org.eurostates.parser.wrapper;

import org.bukkit.configuration.file.YamlConfiguration;
import org.eurostates.parser.Serializable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DoubleWrapperParser implements Serializable<Double, Double> {
    @Override
    public @NotNull Double to(@NotNull Double from) {
        return from;
    }

    @Override
    public @NotNull Double from(@NotNull Double from) {
        return from;
    }

    @Override
    public void serialize(@NotNull YamlConfiguration yaml, @NotNull String node, @NotNull Double value) throws IOException {
        yaml.set(node, value);
    }

    @Override
    public Double deserialize(@NotNull YamlConfiguration yaml, @NotNull String node) {
        return yaml.getDouble(node);
    }
}

package org.eurostates.parser;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface StringParser<T> extends Serializable<T, String> {

    @Override
    default void serialize(@NotNull YamlConfiguration config, @NotNull String node, @NotNull T value) throws IOException {
        if (value == null) {
            config.set(node, null);
            return;
        }
        config.set(node, this.to(value));
    }

    @Override
    default T deserialize(@NotNull YamlConfiguration config, @NotNull String node) throws IOException {
        String asString = config.getString(node);
        if (asString == null) {
            throw new NullPointerException("No value present at " + node);
        }
        return this.from(asString);
    }
}

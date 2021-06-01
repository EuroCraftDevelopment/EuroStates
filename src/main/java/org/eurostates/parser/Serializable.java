package org.eurostates.parser;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Serializable<T, R> extends Parser<T, R> {

    void serialize(@NotNull YamlConfiguration yaml, @NotNull String node, @NotNull T value) throws IOException;

    T deserialize(@NotNull YamlConfiguration yaml, @NotNull String node) throws IOException;
}

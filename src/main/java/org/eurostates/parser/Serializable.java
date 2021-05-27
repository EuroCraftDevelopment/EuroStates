package org.eurostates.parser;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public interface Serializable<T, R> extends Parser<T, R> {

    void serialize(YamlConfiguration yaml, String node, T value) throws IOException;

    T deserialize(YamlConfiguration yaml, String node) throws IOException;
}

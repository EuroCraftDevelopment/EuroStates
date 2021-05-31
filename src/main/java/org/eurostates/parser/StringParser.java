package org.eurostates.parser;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public interface StringParser<T> extends Serializable<T, String> {

    @Override
    default void serialize(YamlConfiguration config, String node, T value) throws IOException {
        if (value == null) {
            config.set(node, null);
            return;
        }
        config.set(node, this.to(value));
    }

    @Override
    default T deserialize(YamlConfiguration config, String node) throws IOException {
        String asString = config.getString(node);
        if (asString == null) {
            throw new NullPointerException("No value present at " + node);
        }
        return this.from(asString);
    }
}

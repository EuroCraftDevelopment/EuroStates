package org.eurostates.config.node;

import org.bukkit.configuration.file.YamlConfiguration;
import org.eurostates.parser.Serializable;

import java.io.IOException;

public class SerializableConfigNode<T, F> extends ConfigNode<T, F> {

    public SerializableConfigNode(Serializable<T, F> parser, String... node) {
        super(parser, node);
    }

    @Override
    public T parse(YamlConfiguration configuration) throws IOException {
        return this.getParser().deserialize(configuration, this.getBukkitNode());
    }

    @Override
    public void set(YamlConfiguration configuration, T value) throws IOException {
        this.getParser().serialize(configuration, this.getBukkitNode(), value);
    }
}

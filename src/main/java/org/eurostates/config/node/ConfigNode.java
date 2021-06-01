package org.eurostates.config.node;

import org.bukkit.configuration.file.YamlConfiguration;
import org.eurostates.parser.Serializable;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ConfigNode<T, S> {

    String[] node;
    Serializable<T, S> parser;

    public ConfigNode(Serializable<T, S> parser, String... node) {
        this.parser = parser;
        this.node = node;
    }

    public abstract T parse(YamlConfiguration configuration) throws IOException;

    public abstract void set(YamlConfiguration configuration, T value) throws IOException;

    public Serializable<T, S> getParser() {
        return this.parser;
    }

    public String[] getNode() {
        return this.node;
    }

    public String getBukkitNode() {
        return Stream.of(this.getNode()).collect(Collectors.joining("."));
    }
}

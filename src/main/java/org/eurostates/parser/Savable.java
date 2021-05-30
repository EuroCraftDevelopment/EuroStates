package org.eurostates.parser;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public interface Savable<T, F, E> {

    File getFile();

    Serializable<T, F> getSerializableParser();

    Serializable<T, E> getIdParser();

    String getRootNode();

    default void save() throws IOException {
        save(getFile());
    }

    default void save(File file) throws IOException {
        try {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            this.getSerializableParser().serialize(configuration, this.getRootNode(), (T) this);
            configuration.save(file);
        } catch (ClassCastException e) {
            throw new IOException("The first generic of savable needs to be the implementing class", e);
        }
    }
}

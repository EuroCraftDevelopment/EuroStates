package org.eurostates.parser;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public interface Savable<T, F, E> {

    @NotNull File getFile();

    @NotNull Serializable<T, F> getSerializableParser();

    @NotNull Serializable<T, E> getIdParser();

    @NotNull String getRootNode();

    default void save() throws IOException {
        save(getFile());
    }

    default void save(@NotNull File file) throws IOException {
        try {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            this.getSerializableParser().serialize(configuration, this.getRootNode(), (T) this);
            configuration.save(file);
        } catch (ClassCastException e) {
            throw new IOException("The first generic of savable needs to be the implementing class", e);
        }
    }
}

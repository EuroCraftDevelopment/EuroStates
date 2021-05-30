package org.eurostates.parser;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

public interface StringMapParser<T> extends Serializable<T, Map<String, Object>> {

    @Override
    default void serialize(YamlConfiguration yaml, String node, T value) throws IOException {
        Map<String, Object> map = this.to(value);
        map.forEach((key, mapValue) -> {
            yaml.set(node + "." + key, mapValue);
        });
    }

    @Override
    default T deserialize(YamlConfiguration yaml, String node) throws IOException {
        ConfigurationSection section = yaml.getConfigurationSection(node);
        if (section == null) {
            throw new NullPointerException("No value at " + node);
        }
        Map<String, Object> map = section.getKeys(false)
                .stream()
                .map(key -> new AbstractMap.SimpleImmutableEntry<>(key, deserializeEntry(yaml, node, key)))
                .collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue));
        return this.from(map);
    }

    default Object deserializeEntry(YamlConfiguration yaml, String node, String key) {
        return yaml.get(node);
    }
}

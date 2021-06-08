package org.eurostates.parser;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.eurostates.util.lamda.throwable.bi.ThrowableBiFunction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public interface StringMapParser<T> extends Serializable<T, Map<String, Object>> {

    @NotNull Map<String, ThrowableBiFunction<YamlConfiguration, String, ?, IOException>> getParser();

    @Override
    default void serialize(@NotNull YamlConfiguration yaml, @NotNull String node, @NotNull T value) throws IOException {
        Map<String, Object> map = this.to(value);
        map.forEach((key, mapValue) -> {
            yaml.set(node + "." + key, mapValue);
        });
    }

    @Override
    default T deserialize(@NotNull YamlConfiguration yaml, @NotNull String node) throws IOException {
        ConfigurationSection section = yaml.getConfigurationSection(node);
        if (section == null) {
            throw new NullPointerException("No value at " + node);
        }
        Map<String, Object> map = section.getKeys(false)
                .stream()
                .map(key -> new AbstractMap.SimpleImmutableEntry<>(key, deserializeEntry(yaml, node + "." + key, key)))
                .map(entry -> {
                    if (entry.getValue() instanceof MemorySection) {
                        MemorySection memorySection = (MemorySection) entry.getValue();
                        String path = memorySection.getCurrentPath();
                        Map<String, ThrowableBiFunction<YamlConfiguration, String, ?, IOException>> parser = this.getParser();
                        ThrowableBiFunction<YamlConfiguration, String, ?, IOException> function = parser.get(entry.getKey());
                        if (function == null) {
                            System.err.println(StringMapParser.this.getClass().getSimpleName() + ": Could not get function for " + (entry.getKey()) + ". Using memory location instead");
                            return entry;
                        }
                        try {
                            Object result = function.apply(yaml, path);
                            return new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), result);
                        } catch (IOException e) {
                            return null;
                        }
                    }
                    return entry;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue));
        return this.from(map);
    }

    default Object deserializeEntry(YamlConfiguration yaml, String node, String key) {
        return yaml.get(node);
    }
}

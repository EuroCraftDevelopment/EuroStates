package org.eurostates.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.eurostates.config.node.ConfigNode;
import org.eurostates.config.node.SerializableConfigNode;
import org.eurostates.parser.Parsers;

import java.io.File;
import java.io.IOException;

public class Config {

    private final File file;

    public static final SerializableConfigNode<Double, Double> WAR_SCORE_KILL = new SerializableConfigNode<>(Parsers.DOUBLE_WRAPPER, "War", "Score", "Kill");
    public static final SerializableConfigNode<Integer, Integer> WAR_SCORE_CLOSE_TOWN = new SerializableConfigNode<>(Parsers.INTEGER_WRAPPER, "War", "Score", "CloseTown");
    public static final SerializableConfigNode<Integer, Integer> WAR_TIME_OVERALL = new SerializableConfigNode<>(Parsers.INTEGER_WRAPPER, "War", "Time", "Override");
    public static final SerializableConfigNode<Integer, Integer> WAR_TIME_CLOSE_TOWN = new SerializableConfigNode<>(Parsers.INTEGER_WRAPPER, "War", "Time", "CloseTown");
    public static final SerializableConfigNode<Integer, Integer> WAR_DISTANCE_CLOSE_TOWN = new SerializableConfigNode<>(Parsers.INTEGER_WRAPPER, "War", "Distance", "CloseTown");

    public Config(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public YamlConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(this.file);
    }

    public <T, F> T parse(ConfigNode<T, F> node) throws IOException {
        return node.parse(this.getConfig());
    }

    public <T, F> void set(ConfigNode<T, F> config, T value) throws IOException {
        YamlConfiguration yaml = this.getConfig();
        config.set(yaml, value);
        yaml.save(this.file);
    }
}

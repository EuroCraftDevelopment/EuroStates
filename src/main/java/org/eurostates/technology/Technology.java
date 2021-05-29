package org.eurostates.technology;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.eurostates.EuroStates;
import org.eurostates.functions.ParseLoadedData;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Technology {

    private String id;
    private Set<String> requirements = new HashSet<>();
    private Set<String> permissions = new HashSet<>();

    public static final String ID_NODE = "id";
    public static final String REQ_NODE = "requirements";
    public static final String PERM_NODE = "permissions";

    public Technology(String id){
        this.id = id;
    }

    public Technology(String id, Set<String> requirements, Set<String> permissions){
        this.id = id;
        this.requirements = requirements;
        this.permissions = permissions;
    }

    public String getID(){ return this.id; }
    public void setID(String id){ this.id = id; }

    public void setPermissions(Set<String> permissions) { this.permissions = permissions; }
    public Set<String> getPermissions() { return permissions; }

    public void setRequirements(Set<String> requirements) { this.requirements = requirements; }

    public Set<String> getRequirements() { return requirements; }

    public static File getFile(String id){
        Plugin plugin = EuroStates.getPlugin();
        return new File(plugin.getDataFolder()+File.separator+"data"+File.separator+"technology"+File.separator+id+".yml");
    }

    public static Technology getFromFile(String id) throws IOException {
        Technology technology = new Technology(id);
        File file = getFile(id);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        technology.setID(ParseLoadedData.getString(config, ID_NODE));
        technology.setPermissions(new HashSet<>(ParseLoadedData.getStringList(config, PERM_NODE)));
        technology.setRequirements(new HashSet<>(ParseLoadedData.getStringList(config, REQ_NODE)));

        return technology;
    }

    public void saveToFile(String id) throws IOException {
        File file = getFile(id);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set(ID_NODE, this.id);
        config.set(REQ_NODE, this.requirements);
        config.set(PERM_NODE, this.permissions);

        config.save(file);
    }

}

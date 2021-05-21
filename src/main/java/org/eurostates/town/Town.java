package org.eurostates.town;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.eurostates.EuroStates;
import org.eurostates.functions.ParseLoadedData;
import org.eurostates.state.State;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Town {
    private String tag; // 4-Letter Identifier for the town.
    private String name; // Actual Human Readable Name
    private UUID mayor; // Mayor UUID
    private String state; // 3-Letter Identifier for the State the Town Belongs to.

    public static final String TAG_NODE = "meta.tag";
    public static final String NAME_NODE = "meta.name";
    public static final String MAYOR_NODE = "meta.mayor";
    public static final String STATE_NODE = "meta.state";

    public Town(String tag) {
        this(tag, null, null, null); // there is reset :)
    }

    public Town(String tag, String name, UUID mayor, String state) {
        this.tag = tag;
        this.name = name;
        this.mayor = mayor;
        this.state = state;
    }

    public String getTag() { return this.tag; }
    public void setTag(String tag) { this.tag = tag; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public UUID getMayor() { return this.mayor; }
    public OfflinePlayer getMayorPlayer() { return Bukkit.getOfflinePlayer(this.mayor); }
    public void setMayor(UUID mayor) { this.mayor = mayor; }

    public String getState() { return this.state; }
    public State getStateObj() throws IOException { return State.getFromFile(State.getFile(this.state)); }
    public void setState(String state) { this.state = state; }


    public static Town getFromFile(File file) throws IOException {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String tag = ParseLoadedData.getString(config, TAG_NODE);
        if(tag.length()!=4){throw new IOException("[ES_ERR]: Parse for .yml value load failed: Town Tags must be 4 letters.");}

        String name = ParseLoadedData.getString(config, NAME_NODE);

        UUID mayor = ParseLoadedData.getUUID(config, MAYOR_NODE);

        String state = ParseLoadedData.getString(config, STATE_NODE);
        if(state.length()!=3){throw new IOException("[ES_ERR]: Parse for .yml value load failed: State Tags must be 3 letters.");}

        return new Town(tag, name, mayor, state);
    }

    public static File getFile(String tag){
        Plugin plugin = EuroStates.getPlugin();
        return new File(plugin.getDataFolder()+File.separator+"data"+File.separator+"town"+File.separator+tag+".yml");
    }

    public void saveToFile(String tag) throws IOException {
        File file = getFile(tag);
        saveToFile(file);
    }

    public void saveToFile(File file) throws IOException {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(TAG_NODE, this.tag);
        config.set(NAME_NODE, this.name);
        config.set(MAYOR_NODE, this.mayor);
        config.set(STATE_NODE, this.state);

        config.save(file);
    }
}

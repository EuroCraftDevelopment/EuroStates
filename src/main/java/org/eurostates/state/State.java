package org.eurostates.state;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.eurostates.EuroStates;
import org.eurostates.functions.ParseLoadedData;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class State {
    private final String tag; // 3-Letter Identifier for the state.
    private final String name; // Actual Human Readable Name
    private final UUID owner; // Owner UUID
    private final char colour; // Color for the chat prefix
    private final Set<UUID> citizens = new HashSet<>();
    private final Set<String> permissions = new HashSet<>();
    private final Set<String> towns = new HashSet<>(); // For member towns

    public static final String TAG_NODE = "meta.tag";
    public static final String NAME_NODE = "meta.name";
    public static final String OWNER_NODE = "meta.owner";
    public static final String COLOUR_NODE = "meta.colour";

    public static final String CITIZENS_NODE = "citizens";
    public static final String PERMISSIONS_NODE = "permissions";
    public static final String TOWNS_NODE = "towns";


    public State(String tag) {
        this(tag, null, null, 'r'); // there is reset :)
    }

    public State(String tag, String name, UUID owner, char colour) {
        this.tag = tag;
        this.name = name;
        this.owner = owner;
        this.colour = colour; // bloody American spelling :( LMAO
    }

    public String getTag() {
        return this.tag;
    }

    public String getName() {
        return this.name;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public OfflinePlayer getOwnerPlayer() {
        return Bukkit.getOfflinePlayer(this.owner);
    }

    public char getColour() {
        return this.colour;
    }

    public String getColourCode() {
        return "§" + this.getColour();
    }

    public ChatColor getBukkitColor() {
        return ChatColor.getByChar(this.getColour());
    }

    public Set<UUID> getCitizens() {
        return this.citizens;
    }

    public Set<OfflinePlayer> getOfflineCitizens() {
        return this.citizens.stream().map(Bukkit::getOfflinePlayer).collect(Collectors.toSet());
    }

    public Set<Player> getOnlineCitizens() {
        return Bukkit.getOnlinePlayers().stream().filter(p -> this.citizens.contains(p.getUniqueId())).collect(Collectors.toSet());
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

    // Load Data from File
    public static State getFromFile(File file) throws IOException {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String tag = ParseLoadedData.getString(config, TAG_NODE);
        if (tag.length() != 3) {throw new IOException("[ES_ERR]: State Tag Invalid (Not 3 Characters.)");}

        String name = ParseLoadedData.getString(config, NAME_NODE);

        UUID owner = ParseLoadedData.getUUID(config, OWNER_NODE);

        String colour = ParseLoadedData.getString(config, COLOUR_NODE);
        if (colour.length() != 1) { throw new IOException("\"[ES_ERR]: Parse for .yml value load failed: Invalid Color Value, needs to be 1 character."); }

        Set<UUID> citizens = ParseLoadedData.getUUIDSet(config, CITIZENS_NODE);

        List<String> permissions = ParseLoadedData.getStringList(config, PERMISSIONS_NODE);

        State state = new State(tag, name, owner, colour.charAt(0));
        state.citizens.addAll(citizens);
        state.permissions.addAll(permissions);
        return state;
    }

    public static File getFile(String tag){
        Plugin plugin = EuroStates.getPlugin();
        return new File(plugin.getDataFolder()+File.separator+"data"+File.separator+"state"+File.separator+tag+".yml");
    }

    public void saveToFile(String tag) throws IOException {
        File file = getFile(tag);
        saveToFile(file);
    }

    // Save Data to File
    public void saveToFile(File file) throws IOException {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(TAG_NODE, this.tag);
        config.set(NAME_NODE, this.name);
        config.set(OWNER_NODE, this.owner.toString());
        config.set(COLOUR_NODE, Character.toString(this.colour));
        config.set(CITIZENS_NODE, this.citizens.parallelStream().map(UUID::toString).collect(Collectors.toList())); // ;) yeah i mean, yeah.
        config.set(PERMISSIONS_NODE, new ArrayList<>(this.permissions));
        config.set(TOWNS_NODE, new ArrayList<>(this.towns));

        config.save(file);
    }

}


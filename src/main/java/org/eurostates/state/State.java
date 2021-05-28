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

@Deprecated
public class State {
    public static final String TAG_NODE = "meta.tag";
    public static final String NAME_NODE = "meta.name";
    public static final String OWNER_NODE = "meta.owner";
    public static final String COLOUR_NODE = "meta.colour";
    public static final String CITIZENS_NODE = "citizens";
    public static final String PERMISSIONS_NODE = "permissions";
    public static final String TOWNS_NODE = "towns";
    private String tag; // 3-Letter Identifier for the state. ex: TUR JAP
    private String name; // Actual Human Readable Name. ex: Turkey Japan
    private UUID owner; // Owner UUID
    private char colour; // Color for the chat prefix
    private Set<UUID> citizens = new HashSet<>();
    private Set<String> permissions = new HashSet<>();
    private Set<String> towns = new HashSet<>(); // For member towns ex: ISTN TOKY


    public State(String tag) {
        this(tag, null, null, 'r', new HashSet<>(), new HashSet<>(), new HashSet<>()); // there is reset :)
    }

    public State(String tag, String name, UUID owner, char colour, Set<UUID> citizens, Set<String> permissions, Set<String> towns) {
        this.tag = tag;
        this.name = name;
        this.owner = owner;
        this.colour = colour; // bloody American spelling :( LMAO
        this.citizens = citizens;
        this.permissions = permissions;
        this.towns = towns;
    }

    // Load Data from File
    public static State getFromFile(String tag) throws IOException {
        File file = getFile(tag);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        State state = new State(tag);

        String ttag = ParseLoadedData.getString(config, TAG_NODE);
        if (tag.length() != 3) {
            throw new IOException("[ES_ERR]: State Tag Invalid (Not 3 Characters.)");
        }
        state.setTag(ttag);

        String name = ParseLoadedData.getString(config, NAME_NODE);
        state.setName(name);

        UUID owner = ParseLoadedData.getUUID(config, OWNER_NODE);
        state.setOwner(owner);

        String colour = ParseLoadedData.getString(config, COLOUR_NODE);
        if (colour.length() != 1) {
            throw new IOException("[ES_ERR]: Parse for .yml value load failed: Invalid Color Value, needs to be 1 character.");
        }
        state.setColour(colour.charAt(0));

        Set<UUID> citizens = ParseLoadedData.getUUIDSet(config, CITIZENS_NODE);

        List<String> permissions = ParseLoadedData.getStringList(config, PERMISSIONS_NODE);

        List<String> towns = ParseLoadedData.getStringList(config, TOWNS_NODE);


        state.citizens.addAll(citizens);
        state.permissions.addAll(permissions);
        state.towns.addAll(towns);
        return state;
    }

    public static File getFile(String tag) {
        Plugin plugin = EuroStates.getPlugin();
        return new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "state" + File.separator + tag + ".yml");
    }

    // GETTER and SETTERs
    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public OfflinePlayer getOwnerPlayer() {
        return Bukkit.getOfflinePlayer(this.owner);
    }

    public char getColour() {
        return this.colour;
    }

    public void setColour(char colour) {
        this.colour = colour;
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

    public void setCitizens(Set<UUID> citizens) {
        this.citizens = citizens;
    }

    public Set<OfflinePlayer> getOfflineCitizens() {
        return this.citizens.stream().map(Bukkit::getOfflinePlayer).collect(Collectors.toSet());
    }

    public Set<Player> getOnlineCitizens() {
        return Bukkit.getOnlinePlayers().stream().filter(p -> this.citizens.contains(p.getUniqueId())).collect(Collectors.toSet());
    }

    public boolean addCitizen(UUID citizen) {
        return this.citizens.add(citizen);
    }

    public boolean removeCitizen(UUID citizen) {
        return this.citizens.remove(citizen);
    }

    public Set<String> getTowns() {
        return this.towns;
    }

    public void setTowns(Set<String> towns) {
        this.towns = towns;
    }

    public boolean addTown(String town) {
        return this.towns.add(town);
    }

    public boolean removeTown(String town) {
        return this.towns.remove(town);
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public boolean addPermission(String permission) {
        return this.permissions.add(permission);
    }

    public boolean removePermission(String permission) {
        return this.permissions.remove(permission);
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


package org.eurostates.state;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class State {
    private String tag; // 3-Letter Identifier for the state.
    private String name; // Actual Human Readable Name
    private UUID owner_UUID; // Owner UUID
    private ChatColor color; // Color for the chat prefix

    private List<UUID> citizens_UUID_list = new ArrayList<>();
    private List<Permission> permissions_list = new ArrayList<>();

    public static final String TAG_NODE = "meta.tag";
    public static final String NAME_NODE = "meta.name";
    public static final String OWNER_NODE = "meta.owner";
    public static final String COLOUR_NODE = "meta.colour";

    public static final String CITIZENS_NODE = "citizens";
    public static final String PERMISSIONS_NODE = "permissions";


    public State(String tag){
        this.tag = tag;
    }

    // Load Data from File
    public static State getFromFile(File file){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String tag = config.getString(TAG_NODE);
        if (tag == null || tag.length() > 3) {
            throw new IOException("Tag is not valid");
        }
        String name = config.getString(NAME_NODE);
        if (name == null) {
            throw new IOException("Name is missing");
        }
        UUID owner;
        try {
            String ownerString = config.getString(OWNER_NODE);
            if (ownerString == null) {
                throw new IOException("Owner is not present");
            }
            owner = UUID.fromString(ownerString);
        } catch (Throwable e) {
            throw new IOException("Owner is not a valid UUID", e);
        }
        String colour = config.getString(COLOUR_NODE); //of course bukkit doesn't have 'getCharacter'
        if (colour == null || colour.length() != 1) {
            throw new IOException("colour is missing");
        }
        Set<UUID> citizens = config
                .getStringList(CITIZENS_NODE)
                .parallelStream()
                .filter(str -> {
                    try {
                        UUID.fromString(str);
                        return true;
                    } catch (Throwable e) {
                        return false;
                    }
                })
                .map(UUID::fromString)
                .collect(Collectors.toSet()); // what is this wizardry -> lambda wizardry
        List<String> permissions = config.getStringList(PERMISSIONS_NODE);

        State state = new State();

        //got a example state Not really,
    }

    // Save Data to File
    public void saveToFile(File file){

    }

}


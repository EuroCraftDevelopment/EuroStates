package org.eurostates.state;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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


        State state = new State();

        //got a example state Not really,
    }

    // Save Data to File
    public void saveToFile(File file){

    }

}


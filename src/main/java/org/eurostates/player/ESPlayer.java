package org.eurostates.player;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ESPlayer {
    private final String state_tag;
    private final UUID id;

    public static final String STATE_TAG_NODE= "state_tag";
    public static final String UUID_NODE = "uuid";

    public ESPlayer(UUID id){
        this.state_tag="Nomad";
        this.id = id;
    }

    public ESPlayer(UUID id, String state_tag){
        this.state_tag=state_tag;
        this.id=id;
    }

    public static ESPlayer getFromFile(File file) throws IOException {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        UUID p_UUID;
        try {
            String p_string = config.getString(UUID_NODE);
            if (p_string==null){throw new IOException("UUID Not Found");}
            p_UUID = UUID.fromString(p_string);
        } catch (Throwable e) {
            throw new IOException("UUID Not Valid" + e);
        }

        String p_state_tag = config.getString(STATE_TAG_NODE);
        if(p_state_tag == null){throw new IOException("State Tag Not Found");}

        ESPlayer player = new ESPlayer(p_UUID, p_state_tag);
        return player;
    }

}

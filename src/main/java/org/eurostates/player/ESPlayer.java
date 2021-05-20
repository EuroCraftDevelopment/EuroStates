package org.eurostates.player;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.eurostates.EuroStates;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ESPlayer {
    private final String state_tag;
    private final UUID id;

    public static final String STATE_TAG_NODE= "state_tag";
    public static final String UUID_NODE = "uuid";

    public ESPlayer(UUID id){
        this.state_tag="NOMAD";
        this.id = id;
    }

    public ESPlayer(UUID id, String state_tag){
        this.state_tag=state_tag;
        this.id=id;
    }

    public String getStateTag(){return this.state_tag;}

    public static File getFile(String uuid){
        Plugin plugin = EuroStates.getPlugin();
        return new File(plugin.getDataFolder()+File.separator+"data"+File.separator+"player"+File.separator+uuid+".yml");
    }

    public void saveToFile(String uuid) throws IOException {
        File file = getFile(uuid);
        saveToFile(file);
    }

    public void saveToFile(File file) throws IOException {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(UUID_NODE, this.id.toString());
        config.set(STATE_TAG_NODE, this.state_tag);
        config.save(file);
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

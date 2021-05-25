package org.eurostates.player;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.eurostates.EuroStates;
import org.eurostates.functions.ParseLoadedData;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ESPlayer {
    private String state_tag;
    private String rank;
    private UUID id;
    private String mayor_of;

    public static final String STATE_TAG_NODE= "state_tag";
    public static final String UUID_NODE = "uuid";
    public static final String RANK_NODE = "rank";
    public static final String MAYOR_OF_NODE = "mayor_of";

    public ESPlayer(UUID id){
        this.state_tag="NOMAD";
        this.rank="NO NATION";
        this.mayor_of="None";
        this.id = id;
    }

    public ESPlayer(UUID id, String state_tag, String rank, String mayor_of){
        this.state_tag=state_tag;
        this.rank=rank;
        this.id=id;
        this.mayor_of=mayor_of;
    }

    public String getStateTag(){return this.state_tag;}
    public void setStateTag(String tag){this.state_tag = tag;}

    public UUID getId(){return this.id;}
    public void setId(UUID id){this.id = id;}

    public String getRank(){return this.rank;}
    public void setRank(String rank){this.rank = rank;}

    public String getMayorOf(){return this.mayor_of;}
    public void setMayorOf(String mayor_of){this.mayor_of=mayor_of;}

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
        config.set(RANK_NODE,this.rank);
        config.set(MAYOR_OF_NODE, this.mayor_of);
        config.save(file);
    }

    public static ESPlayer getFromFile(String uuid) throws IOException {
        File file = getFile(uuid);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        ESPlayer player = new ESPlayer(UUID.fromString(uuid));

        player.setId(ParseLoadedData.getUUID(config, UUID_NODE));
        player.setStateTag(ParseLoadedData.getString(config, STATE_TAG_NODE));
        player.setRank(ParseLoadedData.getString(config, RANK_NODE));
        player.setMayorOf(ParseLoadedData.getString(config, RANK_NODE));

        return player;
    }
}

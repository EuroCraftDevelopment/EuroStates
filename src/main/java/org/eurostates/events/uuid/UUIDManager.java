package org.eurostates.events.uuid;

import org.apache.commons.lang.ObjectUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.eurostates.EuroStates;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class UUIDManager {

    //hi
    // hi :)

    public static void saveUUID(String UUID, String username) throws IOException {
        EuroStates plugin = EuroStates.retPlugin();
        File file = new File(plugin.getDataFolder()+File.separator+"uuid.yml");
        FileConfiguration uuid_file = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()){file.createNewFile();}
        uuid_file.set(UUID, username);
        uuid_file.save(file);
    }

    public static String getUUID(String UUID){
        EuroStates plugin = EuroStates.retPlugin();
        File file = new File(plugin.getDataFolder()+File.separator+"uuid.yml");
        FileConfiguration uuid_file = YamlConfiguration.loadConfiguration(file);
        String username = uuid_file.getString(UUID);
        if(username == null){}
        return username;
    }
}

package org.eurostates.functions;

import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ParseLoadedData {
    public static String getString(YamlConfiguration config, String node) throws IOException {
        String string = config.getString(node);
        if(string==null){throw new IOException("[ES_ERR]: Parse for .yml value load failed: Missing Node: "+node);}
        return string;
    }

    public static UUID getUUID(YamlConfiguration config, String node) throws IOException {
        UUID p_UUID;
        try {
            String uuid_string = config.getString(node);
            if (uuid_string== null) { throw new IOException("[ES_ERR]: Parse for .yml value load failed: Missing Node: "+node); }
            p_UUID = UUID.fromString(uuid_string);

        } catch (Throwable e) { throw new IOException("[ES_ERR]: Parse for UUID .yml value load failed:"+node, e); }

        return p_UUID;
    }

    public static List<String> getStringList(YamlConfiguration config, String node) throws IOException{
        List<String> stringList = config.getStringList(node);
        return stringList;
    }

    public static Set<UUID> getUUIDSet(YamlConfiguration config, String node) throws IOException{
        Set<UUID> uuidSet = config
                .getStringList(node)
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

        // if(uuidSet.size()==0){throw new IOException("ES_ERR]: Parse for .yml value load failed: UUIDSet has length of 0.");}

        return uuidSet;
    }
}

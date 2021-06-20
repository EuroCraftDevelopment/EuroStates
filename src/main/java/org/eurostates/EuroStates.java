package org.eurostates;


import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPI;
import org.eurostates.area.ESUser;
import org.eurostates.area.relationship.Relationship;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.States;
import org.eurostates.area.technology.Technologies;
import org.eurostates.area.technology.Technology;
import org.eurostates.area.town.Town;
import org.eurostates.config.Config;
import org.eurostates.dynmap.DAPIProvider;
import org.eurostates.dynmap.MarkerSetManager;
import org.eurostates.events.GeneralListener;
import org.eurostates.events.WarListener;
import org.eurostates.mosecommands.bukkit.BukkitCommand;
import org.eurostates.mosecommands.bukkit.BukkitCommands;
import org.eurostates.parser.Parsers;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public final class EuroStates extends JavaPlugin {

    static EuroStates plugin;
    static LuckPerms luckPermsApi;
    static DynmapCommonAPI dynmapApi;

    private final Set<Relationship> relationships = new HashSet<>();
    private final Set<ESUser> users = new HashSet<>();

    public static @NotNull EuroStates getPlugin() {
        return plugin;
    }

    public static @NotNull DynmapCommonAPI getDynmapApi() {
        return dynmapApi;
    }

    public static @NotNull LuckPerms getLuckPermsApi() {
        return luckPermsApi;
    }

    private void initStates() {
        Bukkit.getLogger().info("States init.");
        Set<CustomState> states = loadStates();
        Set<Town> towns = loadTowns();
        states.forEach(state -> {
            Set<Town> assignedTowns = towns
                    .parallelStream()
                    .filter(town -> town.getState().equals(state))
                    .collect(Collectors.toSet());
            state.getTowns().addAll(assignedTowns);
        });
        Set<ESUser> users = loadUsers();
        this.users.addAll(users);
    }

    private void initTechnologies() {
        Bukkit.getLogger().info("Technologies init.");
        Set<Technology> technologies = loadTechnologies();
    }

    private @NotNull Set<Town> loadTowns() {
        File path = new File(EuroStates.getPlugin().getDataFolder(), "data/towns/");
        File[] files = path.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) {
            return Collections.emptySet();
        }
        String rootNode = "Town";

        Set<Town> set = new HashSet<>();
        for (File file : files) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            try {
                set.add(Parsers.LOADABLE_TOWN.deserialize(config, rootNode));
            } catch (IOException e) {
                System.err.println("Error: Couldn't load town file of " + file.getPath());
                e.printStackTrace();
            }
        }
        return set;
    }

    private @NotNull Set<ESUser> loadUsers() {
        File path = new File(EuroStates.getPlugin().getDataFolder(), "data/users/");
        File[] files = path.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) {
            return Collections.emptySet();
        }
        String rootNode = "user";

        Set<ESUser> users = new HashSet<>(files.length);
        for (File file : files) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            try {
                users.add(Parsers.LOADABLE_USER.deserialize(config, rootNode));
            } catch (Throwable e) {
                System.err.println("Error: Couldn't load state file of " + file.getPath());
                e.printStackTrace();
            }
        }
        this.users.addAll(users);
        return users;
    }

    private @NotNull Set<Technology> loadTechnologies() {
        File path = new File(EuroStates.getPlugin().getDataFolder(), "data/technology/");
        File[] files = path.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) {
            return Collections.emptySet();
        }
        String rootNode = "technology";

        Set<Technology> technologies = new HashSet<>(files.length);
        for (File file : files) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            try {
                technologies.add(Parsers.LOADABLE_TECHNOLOGY.deserialize(config, rootNode));
            } catch (Throwable e) {
                System.err.println("Error: Couldn't load technology file of " + file.getPath());
                e.printStackTrace();
            }
        }
        Technologies.TECHNOLOGIES.addAll(technologies);
        return technologies;
    }

    private @NotNull Set<CustomState> loadStates() {
        File path = new File(EuroStates.getPlugin().getDataFolder(), "data/state/");
        File[] files = path.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) {
            return Collections.emptySet();
        }
        String rootNode = "State";

        Set<CustomState> states = new HashSet<>(files.length);
        for (File file : files) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            try {
                states.add(Parsers.LOADABLE_STATE.deserialize(config, rootNode));
            } catch (Throwable e) {
                System.err.println("Error: Couldn't load state file of " + file.getPath());
                e.printStackTrace();
            }
        }
        States.CUSTOM_STATES.addAll(states);
        return states;
    }

    public Config getConfiguration() {
        return new Config(new File(this.getDataFolder(), "config.yml"));
    }

    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPermsApi = provider.getProvider();
        }

        plugin = this; // For further plugin obj access
        //CommandHandler.commandLauncher();

        initTechnologies();
        initStates();

        dynmapApi = DAPIProvider.registerDynmap(plugin);
        MarkerSetManager.initMarkerSet();


        try {
            registerCommand("town", BukkitCommands.TOWN_COMMAND);
            registerCommand("state", BukkitCommands.STATE_COMMAND);
            registerCommand("technology", BukkitCommands.TECHNOLOGY_COMMAND);
        } catch (IOException e) {
            this.setEnabled(false);
            e.printStackTrace();
            return;
        }

        //EventHandler.registerEvents();
        Bukkit.getPluginManager().registerEvents(new GeneralListener(), this);
        Bukkit.getPluginManager().registerEvents(new WarListener(), this);

    }

    private void registerCommand(@NotNull String commandLabel, @NotNull BukkitCommand cmd) throws IOException {
        PluginCommand bukkitCommand = this.getCommand(commandLabel);
        if (bukkitCommand == null) {
            throw new IOException("EuroState: Could not find " + commandLabel + " command");
        }
        bukkitCommand.setExecutor(cmd);
        bukkitCommand.setTabCompleter(cmd);
    }

    public @NotNull Set<Town> getTowns() {
        return States
                .CUSTOM_STATES
                .parallelStream()
                .flatMap(state -> state.getTowns().parallelStream())
                .collect(Collectors.toSet());
    }

    public @NotNull Optional<Town> getTown(@NotNull UUID uuid) {
        return this.getTowns()
                .parallelStream()
                .filter(town -> town.getId().equals(uuid))
                .findAny();
    }

    public @NotNull Set<ESUser> getUsers() {
        return this.users;
    }

    public @NotNull Optional<ESUser> getUser(@NotNull UUID uuid) {
        return this
                .getUsers()
                .parallelStream()
                .filter(user -> {
                    UUID userId = user.getOwnerId();
                    return userId.equals(uuid);
                })
                .findAny();
    }

    public void register(ESUser user) {
        if (!user.getAssignedRank().isPresent()) {
            //NO NEED TO REGISTER
            return;
        }
        this.users.add(user);
    }
}

package org.eurostates;


import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.States;
import org.eurostates.area.town.CustomTown;
import org.eurostates.area.town.Town;
import org.eurostates.events.EventHandler;
import org.eurostates.mosecommands.ArgumentCommands;
import org.eurostates.mosecommands.bukkit.BukkitCommand;
import org.eurostates.parser.Parsers;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public final class EuroStates extends JavaPlugin {

    static EuroStates plugin;
    static LuckPerms api;

    @Override
    public void onEnable() {
        // Plugin startup logic
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }

        plugin = this; // For further plugin obj access
        //CommandHandler.commandLauncher();

        PluginCommand townBukkitCommand = this.getCommand("Town");
        if (townBukkitCommand == null) {
            System.err.println("Disabling EuroState: Could not find town command");
            this.setEnabled(false);
            return;
        }
        TabExecutor townCommand = new BukkitCommand(ArgumentCommands.TOWN_VIEW);
        townBukkitCommand.setExecutor(townCommand);
        townBukkitCommand.setTabCompleter(townCommand);


        EventHandler.registerEvents();
    }

    public static EuroStates getPlugin() {
        return plugin;
    }

    public Set<Town> getTowns() {
        return States
                .CUSTOM_STATES
                .parallelStream()
                .flatMap(state -> state.getTowns().parallelStream())
                .collect(Collectors.toSet());
    }

    public Optional<CustomTown> getTown(@NotNull UUID uuid) {
        return this.getTowns()
                .parallelStream()
                .filter(town -> town instanceof CustomTown)
                .map(town -> (CustomTown) town)
                .filter(town -> town.getId().equals(uuid))
                .findAny();
    }

    public Set<ESUser> getUsers() {
        return States
                .CUSTOM_STATES
                .parallelStream()
                .flatMap(s -> s.getEuroStatesCitizens().parallelStream())
                .collect(Collectors.toSet());
    }

    public Optional<ESUser> getUser(@NotNull UUID uuid) {
        return this
                .getUsers()
                .parallelStream()
                .filter(user -> user.getOwnerId().equals(uuid))
                .findAny();
    }

    private static void initStates() {
        Set<CustomState> states = loadStates();
        Set<CustomTown> towns = loadTowns();
        states.forEach(state -> {
            Set<CustomTown> assignedTowns = towns
                    .parallelStream()
                    .filter(town -> town.getState().equals(state))
                    .collect(Collectors.toSet());
            state.getTowns().addAll(assignedTowns);
        });
    }

    private static Set<CustomTown> loadTowns() {
        File path = new File(EuroStates.getPlugin().getDataFolder(), "data/state/");
        File[] files = path.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) {
            return Collections.emptySet();
        }
        String rootNode = "State";

        Set<CustomTown> set = new HashSet<>();
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

    private static Set<CustomState> loadStates() {
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
            } catch (IOException e) {
                System.err.println("Error: Couldn't load state file of " + file.getPath());
                e.printStackTrace();
            }
        }
        States.CUSTOM_STATES.addAll(states);
        return states;
    }

    public static LuckPerms getLuckPermsApi() {
        return api;
    }
}

package org.eurostates.commands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.eurostates.EuroStates;
import org.eurostates.commands.states.StatesBase;
import org.eurostates.commands.states.StatesCmdHandler;
import org.eurostates.commands.town.TownBase;
import org.eurostates.commands.town.TownCmdHandler;
import org.eurostates.commands.town.admin.CreateTown;
import org.eurostates.commands.town.user.ViewTown;

import java.util.Objects;

public class CommandHandler {
    public static void commandLauncher(){
        Plugin plugin = EuroStates.getPlugin();

        // States command reg
        StatesCmdHandler states_handler = new StatesCmdHandler();
        states_handler.register("states", new StatesBase());

        Objects.requireNonNull(Bukkit.getPluginCommand("states")).setExecutor(states_handler);


        // Town command reg
        TownCmdHandler town_handler = new TownCmdHandler();
        town_handler.register("town", new TownBase());
        town_handler.register("view", new ViewTown());
        town_handler.register("create", new CreateTown());

        Objects.requireNonNull(Bukkit.getPluginCommand("town")).setExecutor(town_handler);
    }
}

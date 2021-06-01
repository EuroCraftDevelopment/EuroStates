package org.eurostates.mosecommands.commands.state.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.eurostates.area.state.CustomState;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.area.CustomStateArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class StateDeleteCommand implements ArgumentCommand {
    public static final ExactArgument ADMIN_ARGUMENT = new ExactArgument("admin");
    public static final ExactArgument DELETE_ARGUMENT = new ExactArgument("delete");
    public static final CustomStateArgument STATE_ARGUMENT = new CustomStateArgument("state");


    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                ADMIN_ARGUMENT,
                DELETE_ARGUMENT,
                STATE_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.of("eurostates.admin");
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        CustomState state = context.getArgument(this, STATE_ARGUMENT);

        state.delete();

        Bukkit.broadcastMessage(ChatColor.BLUE+"[EuroStates] "+
                ChatColor.WHITE+"State "+state.getName()+" was forcibly disbanded.");

        return true;
    }
}

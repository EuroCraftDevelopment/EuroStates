package org.eurostates.mosecommands.commands.state.global;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.States;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.eurostates.parser.Parsers;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class StateLeaveCommand implements ArgumentCommand {
    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{new ExactArgument("leave")};
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.empty();
    }

    @Override
    public boolean canRun(@NotNull CommandSender sender) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if (States.CUSTOM_STATES.parallelStream().flatMap(s -> s.getCitizenIds().parallelStream()).noneMatch(uuid -> player.getUniqueId().equals(uuid))) {
            return false;
        }

        return ArgumentCommand.super.canRun(sender);
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        if (!(context.getSource() instanceof Player)) {
            context.getSource().sendMessage("Player only command");
            return false;
        }
        Player player = (Player) context.getSource();
        Optional<CustomState> resultState = States
                .CUSTOM_STATES
                .parallelStream()
                .filter(state -> state
                        .getCitizenIds()
                        .parallelStream()
                        .anyMatch(uuid -> player.getUniqueId().equals(uuid)))
                .findAny();
        if (!resultState.isPresent()) {
            player.sendMessage("Unknown state");
            return true;
        }
        CustomState state = resultState.get();
        state.unregister(Parsers.GETTER_USER.fromId(player.getUniqueId()));
        player.sendMessage("You have left state of " + state.getName());
        return true;
    }
}

package org.eurostates.mosecommands.commands.state.leader;

import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.source.OfflinePlayerArgument;
import org.eurostates.mosecommands.arguments.source.PlayerArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class StateInviteCommand implements ArgumentCommand {
    public static final ExactArgument INVITE_ARGUMENT = new ExactArgument("invite");
    public static final PlayerArgument PLAYER_ARGUMENT = new PlayerArgument("player");

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                INVITE_ARGUMENT,
                PLAYER_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.empty();
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        return false;
    }
}

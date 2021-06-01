package org.eurostates.mosecommands;

import org.bukkit.command.CommandSender;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

//this was taken from the Ships translation layer ;) Its been modified a little to be more suited to bukkit
public interface ArgumentCommand {

    @NotNull CommandArgument<?>[] getArguments();

    Optional<String> getPermission();

    boolean run(@NotNull CommandContext context, @NotNull String[] arg);

    default boolean canRun(@NotNull CommandSender sender) {
        Optional<String> opPermission = this.getPermission();
        return opPermission.map(sender::hasPermission).orElse(true);
    }
}

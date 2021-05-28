package org.eurostates.mosecommands;

import org.bukkit.command.CommandSender;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandContext;

import java.util.Optional;

//this was taken from the Ships translation layer ;) Its been modified a little to be more suited to bukkit
public interface ArgumentCommand {

    CommandArgument<?>[] getArguments();

    Optional<String> getPermission();

    boolean run(CommandContext context, String arg);

    default boolean canRun(CommandSender sender) {
        Optional<String> opPermission = this.getPermission();
        return opPermission.map(sender::hasPermission).orElse(true);
    }
}

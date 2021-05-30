package org.eurostates.mosecommands.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.context.CommandContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BukkitCommand implements TabExecutor {

    private Collection<ArgumentCommand> commands;

    public BukkitCommand(ArgumentCommand... commands) {
        this.commands = Arrays.asList(commands);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandContext context = new CommandContext(sender, this.commands, args);
        Optional<ArgumentCommand> opCommand = context.getCompleteCommand();
        if (!opCommand.isPresent()) {
            context.getErrors().forEach(e -> sender.sendMessage(e.getError()));
            return false;
        }
        ArgumentCommand cmd = opCommand.get();
        if (!cmd.canRun(sender)) {
            return true;
        }
        return cmd.run(context, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        CommandContext context = new CommandContext(sender, this.commands, args);
        return context
                .getPotentialCommands()
                .stream()
                .flatMap(c -> context.getSuggestions(c).parallelStream())
                .collect(Collectors.toList());
    }
}

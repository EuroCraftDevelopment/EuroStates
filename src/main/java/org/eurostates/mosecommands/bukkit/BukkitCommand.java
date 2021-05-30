package org.eurostates.mosecommands.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.ArgumentCommands;
import org.eurostates.mosecommands.context.CommandContext;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BukkitCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandContext context = new CommandContext(sender, getCommands(), args);
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
        CommandContext context = new CommandContext(sender, getCommands(), args);
        return context
                .getPotentialCommands()
                .stream()
                .flatMap(c -> context.getSuggestions(c).parallelStream())
                .collect(Collectors.toList());
    }

    private Set<ArgumentCommand> getCommands() {
        Set<ArgumentCommand> set = new HashSet<>();
        set.add(ArgumentCommands.TOWN_VIEW);
        return set;
    }
}

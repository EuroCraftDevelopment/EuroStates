package org.eurostates.mosecommands.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BukkitCommand implements TabExecutor {

    private final @NotNull Collection<ArgumentCommand> commands;

    public BukkitCommand(ArgumentCommand... commands) {
        this.commands = Arrays.asList(commands);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        CommandContext context = new CommandContext(sender, this.commands, args);
        Optional<ArgumentCommand> opCommand = context.getCompleteCommand();
        if (!opCommand.isPresent()) {
            context.getErrors().forEach(e -> sender.sendMessage(e.getError()));
            context.getPotentialCommands().stream().filter(c -> c.canRun(sender)).forEach(a -> {
                String usage = Stream
                        .of(a.getArguments())
                        .map(CommandArgument::getUsage)
                        .collect(Collectors.joining(" "));
                sender.sendMessage(label + " " + usage);
            });
            return true;
        }
        ArgumentCommand cmd = opCommand.get();
        if (!cmd.canRun(sender)) {
            return true;
        }
        return cmd.run(context, args);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        CommandContext context = new CommandContext(sender, this.commands, args);
        return context
                .getPotentialCommands()
                .stream()
                .flatMap(c -> context.getSuggestions(c).parallelStream())
                .collect(Collectors.toList());
    }
}

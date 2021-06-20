package org.eurostates.mosecommands.commands.technology.global;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.technology.Technology;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.area.CustomStateArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Collectors;

public class TechnologyListCommand implements ArgumentCommand {
    private static final ExactArgument LIST_ARGUMENT = new ExactArgument("list");
    private static final CustomStateArgument STATE_ARGUMENT = new CustomStateArgument("state");

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                LIST_ARGUMENT,
                STATE_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.empty();
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        CustomState state = context.getArgument(this, STATE_ARGUMENT);
        CommandSender source = context.getSource();

        source.sendMessage(ChatColor.RED + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat(" ", 64));
        source.sendMessage(ChatColor.BLUE + "EuroStates " + ChatColor.WHITE + state.getName()+"'s Technologies");
        source.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));

        source.sendMessage(ChatColor.WHITE+ state.getTechnology()
                                                    .stream()
                                                    .map(Technology::getIdentifier)
                                                    .collect(Collectors.joining(", ")));

        source.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));

        return true;
    }
}

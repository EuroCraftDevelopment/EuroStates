package org.eurostates.mosecommands.commands.technology.global;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.eurostates.area.technology.Technology;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.area.TechnologyArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Collectors;

public class TechnologyViewCommand implements ArgumentCommand {
    private static final TechnologyArgument TECHNOLOGY_ARGUMENT = new TechnologyArgument("technology");

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                TECHNOLOGY_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.empty();
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        Technology technology = context.getArgument(this, TECHNOLOGY_ARGUMENT);
        CommandSender source = context.getSource();

        source.sendMessage(ChatColor.RED + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat(" ", 64));
        source.sendMessage(ChatColor.BLUE + "EuroStates " + ChatColor.WHITE + "Technology Info");
        source.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));

        source.sendMessage(ChatColor.WHITE + "Technology Identifier: " + ChatColor.GRAY + technology.getIdentifier());
        source.sendMessage(ChatColor.WHITE + "Technology Name: " + ChatColor.GRAY + technology.getName());
        source.sendMessage(ChatColor.WHITE + "Required Technologies: " + ChatColor.DARK_GRAY + technology.getDependents()
                .stream()
                .map(Technology::getName)
                .collect(Collectors.joining(", ")));

        source.sendMessage(ChatColor.WHITE + "Allows Permissions: " + ChatColor.DARK_GRAY + String.join(", ", technology.getPermissions()));

        source.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 64));
        return true;
    }
}

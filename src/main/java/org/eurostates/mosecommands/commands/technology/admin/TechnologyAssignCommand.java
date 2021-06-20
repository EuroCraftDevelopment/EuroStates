package org.eurostates.mosecommands.commands.technology.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.technology.Technology;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.area.CustomStateArgument;
import org.eurostates.mosecommands.arguments.area.TechnologyArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TechnologyAssignCommand implements ArgumentCommand {
    private static final TechnologyArgument TECHNOLOGY_ARGUMENT = new TechnologyArgument("tech");
    private static final ExactArgument ASSIGN_ARGUMENT = new ExactArgument("assign");
    private static final CustomStateArgument STATE_ARGUMENT = new CustomStateArgument("state");

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                TECHNOLOGY_ARGUMENT,
                ASSIGN_ARGUMENT,
                STATE_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.of("eurostates.admin");
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        Technology tech = context.getArgument(this, TECHNOLOGY_ARGUMENT);
        CustomState state = context.getArgument(this, STATE_ARGUMENT);
        Set<Technology> technologies = state.getTechnology();

        if(technologies.contains(tech)) {
            context.getSource().sendMessage("That already is assigned to this nation.");
            return true;
        }

        // check if nation has required technologies
        Set<Technology> missingTechs = tech.getDependents()
                .stream()
                .filter(technology -> (!state.getTechnology().contains(technology)))
                .collect(Collectors.toSet());


        if(!missingTechs.isEmpty()) {
            context.getSource().sendMessage("State is missing technologies:");
            missingTechs.forEach(t -> context.getSource().sendMessage(t.getIdentifier()));
            return true;
        }

        state.getTechnology().add(tech);
        state.updatePermissions();
        try {
            state.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        context.getSource().sendMessage("Added "+tech.getIdentifier()+" as a technology to "+state.getName());
        Bukkit.broadcastMessage(ChatColor.BLUE+"[EuroStates] "+
                ChatColor.WHITE + state.getName() + " has researched \""+tech.getName()+"\".");

        return true;
    }
}

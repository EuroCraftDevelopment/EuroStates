package org.eurostates.mosecommands.commands.technology.admin;

import org.eurostates.area.technology.Technology;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.area.TechnologyArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

public class TechnologyRequirementAddCommand implements ArgumentCommand {
    private static final TechnologyArgument TECHNOLOGY_ARGUMENT = new TechnologyArgument("tech");
    private static final ExactArgument REQUIREMENT_ARGUMENT = new ExactArgument("requirement");
    private static final ExactArgument ADD_ARGUMENT = new ExactArgument("add");
    private static final TechnologyArgument REQ_TECH_ARGUMENT = new TechnologyArgument("required_tech");

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                TECHNOLOGY_ARGUMENT,
                REQUIREMENT_ARGUMENT,
                ADD_ARGUMENT,
                REQ_TECH_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.of("eurostates.admin");
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        Technology technology = context.getArgument(this, TECHNOLOGY_ARGUMENT);
        Technology newRequirement = context.getArgument(this, REQ_TECH_ARGUMENT);

        if(technology.getID().equals(newRequirement.getID())) {
            context.getSource().sendMessage("You cant add something to itself as a requirement dumbass");
            return true;
        }

        if(technology.getDependents().contains(newRequirement)) {
            context.getSource().sendMessage("It already has that as a requirement dumbass.");
            return true;
        }

        technology.getDependents().add(newRequirement);
        try {
            technology.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        context.getSource().sendMessage("Added " + newRequirement.getIdentifier() + " as a requirement to "+technology.getIdentifier());
        return true;
    }
}

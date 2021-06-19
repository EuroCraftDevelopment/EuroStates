package org.eurostates.mosecommands.commands.technology.admin;

import org.eurostates.area.technology.Technology;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.area.TechnologyArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.simple.StringArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

public class TechnologyPermissionAddCommand implements ArgumentCommand {
    private static final TechnologyArgument TECHNOLOGY_ARGUMENT = new TechnologyArgument("tech");
    private static final ExactArgument PERMISSION_ARGUMENT = new ExactArgument("permission");
    private static final ExactArgument ADD_ARGUMENT = new ExactArgument("add");
    private static final StringArgument NEW_PERMISSION_ARGUMENT = new StringArgument("new_permission");

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                TECHNOLOGY_ARGUMENT,
                PERMISSION_ARGUMENT,
                ADD_ARGUMENT,
                NEW_PERMISSION_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.of("eurostates.admin");
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        Technology technology = context.getArgument(this, TECHNOLOGY_ARGUMENT);
        String permission = context.getArgument(this, NEW_PERMISSION_ARGUMENT);


        if(technology.getPermissions().contains(permission)) {
            context.getSource().sendMessage("It already has that as a permission dumbass.");
            return true;
        }

        technology.getPermissions().add(permission);
        try {
            technology.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        context.getSource().sendMessage("Added " + permission + " as a permission to "+technology.getIdentifier());
        return true;
    }
}
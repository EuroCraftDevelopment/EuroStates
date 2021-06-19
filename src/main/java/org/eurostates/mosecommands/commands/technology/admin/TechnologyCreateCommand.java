package org.eurostates.mosecommands.commands.technology.admin;

import org.eurostates.area.technology.Technologies;
import org.eurostates.area.technology.Technology;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.operation.RemainingArgument;
import org.eurostates.mosecommands.arguments.simple.StringArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TechnologyCreateCommand implements ArgumentCommand {
    public static final ExactArgument CREATE_ARGUMENT = new ExactArgument("create");
    public static final StringArgument NAME_ARGUMENT = new StringArgument("name");
    public static final RemainingArgument<String> REMAINING_ARGUMENT =
            new RemainingArgument<String> ("description", new StringArgument(""));

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                CREATE_ARGUMENT,
                NAME_ARGUMENT,
                REMAINING_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.of("eurostates.admin");
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        String name = context.getArgument(this, NAME_ARGUMENT);
        List<String> remainingList = context.getArgument(this, REMAINING_ARGUMENT);

        String description = String.join(" ", remainingList);

        Technology technology = new Technology(UUID.randomUUID(), name, description);
        Technologies.TECHNOLOGIES.add(technology);
        context.getSource().sendMessage("Created new technology with identifier " + name + " and name " + description);
        try {
            technology.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}

package org.eurostates.mosecommands.commands.state.leader;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.States;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.ParseCommandArgument;
import org.eurostates.mosecommands.arguments.area.CustomStateArgument;
import org.eurostates.mosecommands.arguments.chat.LegacyColourCodeArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.operation.OptionalArgument;
import org.eurostates.mosecommands.arguments.operation.PreArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Optional;
import java.util.UUID;

public class StateEditLegacyColourCommand implements ArgumentCommand {

    public static final ExactArgument EDIT_ARGUMENT = new ExactArgument("edit");
    public static final ExactArgument NAME_ARGUMENT = new ExactArgument("color");
    public static final LegacyColourCodeArgument NEW_COLOUR_ARGUMENT = new LegacyColourCodeArgument("new color");
    private static final PreArgument<CustomState> STATE_PRE_ARGUMENT = new PreArgument<>(new CustomStateArgument("state"), (context, argument) -> new AbstractMap.SimpleImmutableEntry<>(context.getSource().hasPermission("eurostates.admin"), 0));
    private static final ParseCommandArgument<CustomState> STATE_OPTIONAL_DEFAULT = (context, argument) -> {
        if (!(context.getSource() instanceof Player)) {
            throw new IOException("A none player needs to specify a state");
        }
        Player player = (Player) context.getSource();
        Optional<CustomState> opState = getOwningState(player.getUniqueId());
        if (!opState.isPresent()) {
            throw new IOException("You don't own a state, please provide one");
        }
        return new AbstractMap.SimpleEntry<>(opState.get(), 0);
    };
    public static final OptionalArgument<CustomState> STATE_ARGUMENT = new OptionalArgument<>(STATE_PRE_ARGUMENT, STATE_OPTIONAL_DEFAULT);

    private static Optional<CustomState> getOwningState(UUID uuid) {
        return States.CUSTOM_STATES.parallelStream().filter(state -> state.getOwnerId().equals(uuid)).findAny();
    }

    @Override
    public @NotNull CommandArgument<?>[] getArguments() {
        return new CommandArgument[]{
                EDIT_ARGUMENT,
                STATE_ARGUMENT,
                NAME_ARGUMENT,
                NEW_COLOUR_ARGUMENT
        };
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.empty();
    }

    @Override
    public boolean canRun(@NotNull CommandSender sender) {
        if (sender.hasPermission("eurostates.admin")) {
            return true;
        }
        if (!(sender instanceof Player)) {
            return false;
        }
        return getOwningState(((Player) sender).getUniqueId()).isPresent();
    }

    @Override
    public boolean run(@NotNull CommandContext context, @NotNull String[] arg) {
        ChatColor newColour = context.getArgument(this, NEW_COLOUR_ARGUMENT);
        CustomState state = context.getArgument(this, STATE_ARGUMENT);
        state.setChatColour(newColour.getChar());
        context.getSource().sendMessage("Changed state color to " + newColour + newColour.name().toLowerCase().replaceAll("_", " "));
        try {
            state.save();
        } catch (IOException e) {
            context.getSource().sendMessage("Could not save state. Console error provided");
            e.printStackTrace();
        }
        return true;
    }
}


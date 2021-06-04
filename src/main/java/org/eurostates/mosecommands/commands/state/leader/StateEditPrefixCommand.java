package org.eurostates.mosecommands.commands.state.leader;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eurostates.area.ESUser;
import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.States;
import org.eurostates.mosecommands.ArgumentCommand;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.arguments.ParseCommandArgument;
import org.eurostates.mosecommands.arguments.area.CustomStateArgument;
import org.eurostates.mosecommands.arguments.operation.ExactArgument;
import org.eurostates.mosecommands.arguments.operation.OptionalArgument;
import org.eurostates.mosecommands.arguments.operation.PreArgument;
import org.eurostates.mosecommands.arguments.simple.StringArgument;
import org.eurostates.mosecommands.arguments.source.OfflinePlayerArgument;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Optional;
import java.util.UUID;

public class StateEditPrefixCommand implements ArgumentCommand {

    public static final ExactArgument EDIT_ARGUMENT = new ExactArgument("edit");
    public static final ExactArgument NAME_ARGUMENT = new ExactArgument("prefix");
    public static final OfflinePlayerArgument OFFLINE_PLAYER_ARGUMENT = new OfflinePlayerArgument("player");
    public static final StringArgument NEW_PREFIX_ARGUMENT = new StringArgument("new prefix");
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
                OFFLINE_PLAYER_ARGUMENT,
                NEW_PREFIX_ARGUMENT
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
        String newPrefix = context.getArgument(this, NEW_PREFIX_ARGUMENT);
        if (newPrefix.length() > 21 || newPrefix.length() < 3) {
            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                    ChatColor.RED + "Prefix cannot be shorter than 3 or longer than 21.");
            return true;
        }

        CustomState state = context.getArgument(this, STATE_ARGUMENT);
        UUID offlineUUID = context.getArgument(this, OFFLINE_PLAYER_ARGUMENT).getUniqueId();
        Optional<ESUser> opUser = state.getEuroStatesCitizens().parallelStream().filter(p -> p.getOwnerId().equals(offlineUUID)).findAny();
        if (!opUser.isPresent()) {
            context.getSource().sendMessage(ChatColor.BLUE + "[EuroStates] " +
                    ChatColor.RED + "That user is not a member of your state.");
            return true;
        }

        ESUser user = opUser.get();
        String oldPre = user.getAssignedRank().map(v -> " from " + v).orElse("");
        user.setRank(newPrefix);
        context.getSource().sendMessage("Changed prefix" + oldPre + " to " + newPrefix);
        try {
            state.save();
        } catch (IOException e) {
            context.getSource().sendMessage("Could not save state. Console error provided");
            e.printStackTrace();
        }
        return true;
    }
}



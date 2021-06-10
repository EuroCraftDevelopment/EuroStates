package org.eurostates.mosecommands.arguments.source;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @deprecated use {@link org.eurostates.mosecommands.arguments.operation.CollectionArgument#getAsOnlinePlayer(String)} instead
 */
@Deprecated
public class PlayerArgument implements CommandArgument<Player> {

    private final @NotNull String id;

    public PlayerArgument(@NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public Map.@NotNull Entry<Player, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<Player> argument) throws IOException {
        String arg = context.getCommand()[argument.getFirstArgument()];
        if (arg.length() == 0) {
            throw new IOException("Invalid user");
        }
        Player player = Bukkit.getPlayer(arg);
        return new AbstractMap.SimpleImmutableEntry<>(player, argument.getFirstArgument() + 1);
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<Player> argument) {
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .filter(playerName -> playerName.toLowerCase().startsWith(peek.toLowerCase()))
                .collect(Collectors.toList());
    }
}

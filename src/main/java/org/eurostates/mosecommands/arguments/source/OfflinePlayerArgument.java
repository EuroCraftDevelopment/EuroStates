package org.eurostates.mosecommands.arguments.source;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
import java.util.stream.Stream;

/**
 * @deprecated Use {@link org.eurostates.mosecommands.arguments.operation.CollectionArgument}
 */
@Deprecated
public class OfflinePlayerArgument implements CommandArgument<OfflinePlayer> {

    private @NotNull String id;

    public OfflinePlayerArgument(@NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public Map.@NotNull Entry<OfflinePlayer, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<OfflinePlayer> argument) throws IOException {
        String arg = context.getCommand()[argument.getFirstArgument()];
        if (arg.length() == 0) {
            throw new IOException("Invalid user");
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(arg);
        return new AbstractMap.SimpleImmutableEntry<>(player, argument.getFirstArgument() + 1);
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<OfflinePlayer> argument) {
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
        return Stream.of(Bukkit.getOfflinePlayers())
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .filter(playerName -> playerName.toLowerCase().startsWith(peek.toLowerCase()))
                .collect(Collectors.toList());
    }
}

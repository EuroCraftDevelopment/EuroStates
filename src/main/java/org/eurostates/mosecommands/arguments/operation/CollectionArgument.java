package org.eurostates.mosecommands.arguments.operation;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.eurostates.util.Utils;
import org.eurostates.util.lamda.throwable.single.ThrowableFunction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CollectionArgument<T> implements CommandArgument<T> {

    private final String id;
    private final Supplier<Collection<T>> getter;
    private final ThrowableFunction<T, String, IOException> toString;

    public CollectionArgument(String id, Supplier<Collection<T>> supplier, ThrowableFunction<T, String, IOException> throwableFunction) {
        this.id = id;
        this.getter = supplier;
        this.toString = throwableFunction;
    }

    public static CollectionArgument<OfflinePlayer> getAsOfflinePlayer(String id) {
        return new CollectionArgument<>(id, () -> Arrays.asList(Bukkit.getOfflinePlayers()), OfflinePlayer::getName);
    }

    public static CollectionArgument<? extends Player> getAsOnlinePlayer(String id) {
        return new CollectionArgument<>(id, Bukkit::getOnlinePlayers, Player::getName);
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull Map.Entry<T, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) throws IOException {
        String arg = context.getCommand()[argument.getFirstArgument()];
        for (T value : this.getter.get()) {
            String asString;
            try {
                asString = this.toString.apply(value);
            } catch (IOException e) {
                continue;
            }
            if (arg.equalsIgnoreCase(asString)) {
                return new AbstractMap.SimpleImmutableEntry<>(value, argument.getFirstArgument() + 1);
            }
        }
        throw new IOException("Invalid input for " + this.id);
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) {
        String arg = context.getCommand()[argument.getFirstArgument()].toLowerCase();
        return this
                .getter
                .get()
                .stream()
                .map(v -> Utils.throwOr(IOException.class, () -> this.toString.apply(v), null))
                .filter(Objects::nonNull)
                .filter(v -> v.toLowerCase().contains(arg))
                .sorted((o1, o2) -> {
                    if(o1.toLowerCase().startsWith(arg) && !o2.toLowerCase().startsWith(arg)){
                        return 1;
                    }
                    if(o2.toLowerCase().startsWith(arg) && !o1.toLowerCase().startsWith(arg)){
                        return -1;
                    }
                    return o1.compareToIgnoreCase(o2);
                })
                .collect(Collectors.toList());
    }
}

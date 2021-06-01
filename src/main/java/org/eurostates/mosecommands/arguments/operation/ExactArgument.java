package org.eurostates.mosecommands.arguments.operation;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ExactArgument implements CommandArgument<String> {

    private final @NotNull String id;
    private final String[] lookup;
    private final boolean caseSens;

    public ExactArgument(@NotNull String id) {
        this(id, false, id);
    }

    public ExactArgument(@NotNull String id, boolean caseSens, String... lookup) {
        if (lookup.length == 0) {
            throw new IllegalArgumentException("Lookup cannot be []");
        }
        this.id = id;
        this.lookup = lookup;
        this.caseSens = caseSens;
    }

    public @NotNull String[] getLookup() {
        return this.lookup;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    private boolean anyMatch(@NotNull String arg) {
        for (String a : this.lookup) {
            if ((this.caseSens && a.equals(arg)) || (!this.caseSens && a.equalsIgnoreCase(arg))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull Map.Entry<String, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<String> argument) throws IOException {
        String arg = context.getCommand()[argument.getFirstArgument()];
        if (anyMatch(arg)) {
            return new AbstractMap.SimpleImmutableEntry<>(arg, argument.getFirstArgument() + 1);
        }
        throw new IOException("Unknown argument of '" + arg + "'");
    }

    @Override
    public List<String> suggest(@NotNull CommandContext context, @NotNull CommandArgumentContext<String> argument) {
        String arg = "";
        if (context.getCommand().length > argument.getFirstArgument()) {
            arg = context.getCommand()[argument.getFirstArgument()];
        }
        List<String> args = new ArrayList<>();
        for (String look : this.lookup) {
            if (look.toLowerCase().startsWith(arg.toLowerCase())) {
                args.add(look);
            }
        }
        return args;
    }

    @Override
    public @NotNull String getUsage() {
        return "<" + Arrays.stream(this.lookup).map(t -> "\"" + t + "\"").collect(Collectors.joining("/")) + ">";
    }
}
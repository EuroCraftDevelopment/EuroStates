package org.eurostates.mosecommands.arguments.chat;

import org.bukkit.ChatColor;
import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LegacyColourCodeArgument implements CommandArgument<ChatColor> {

    private final String id;

    public LegacyColourCodeArgument(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Map.Entry<ChatColor, Integer> parse(CommandContext context, CommandArgumentContext<ChatColor> argument) throws IOException {
        String arg = context.getCommand()[argument.getFirstArgument()];
        if (arg.length() != 1) {
            throw new IOException("Not a valid legacy chat colour");
        }
        @Nullable ChatColor chatColor = ChatColor.getByChar(arg.charAt(0));
        if (chatColor == null) {
            throw new IOException("Not a valid legacy chat colour");
        }
        return new AbstractMap.SimpleImmutableEntry<>(chatColor, argument.getFirstArgument() + 1);
    }

    @Override
    public List<String> suggest(CommandContext commandContext, CommandArgumentContext<ChatColor> argument) {
        return Stream.of(ChatColor.values()).map(colour -> colour.getChar() + "").collect(Collectors.toList());
    }
}

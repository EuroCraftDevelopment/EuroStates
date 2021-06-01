package org.eurostates.parser.util;

import org.eurostates.parser.StringParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class UUIDParser implements StringParser<UUID> {
    @Override
    public @NotNull String to(@NotNull UUID from) {
        return from.toString();
    }

    @Override
    public @NotNull UUID from(@NotNull String from) throws IOException {
        try {
            return UUID.fromString(from);
        } catch (IllegalArgumentException e) {
            throw new IOException(e);
        }
    }
}

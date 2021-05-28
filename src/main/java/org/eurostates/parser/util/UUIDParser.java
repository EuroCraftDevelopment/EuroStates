package org.eurostates.parser.util;

import org.eurostates.parser.StringParser;

import java.io.IOException;
import java.util.UUID;

public class UUIDParser implements StringParser<UUID> {
    @Override
    public String to(UUID from) {
        return from.toString();
    }

    @Override
    public UUID from(String from) throws IOException {
        try {
            return UUID.fromString(from);
        } catch (IllegalArgumentException e) {
            throw new IOException(e);
        }
    }
}

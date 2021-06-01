package org.eurostates.parser.area.state;

import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.States;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.StringParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class GetterStateParser implements StringParser<CustomState> {
    @Override
    public @NotNull String to(@NotNull CustomState from) throws IOException {
        return Parsers.UUID.to(from.getId());
    }

    @Override
    public @NotNull CustomState from(@NotNull String from) throws IOException {
        UUID uuid = Parsers.UUID.from(from);
        return States
                .CUSTOM_STATES
                .parallelStream()
                .filter(state -> state.getId().equals(uuid))
                .findAny()
                .orElseThrow(() -> new IOException("Unknown State of " + uuid));
    }
}

package org.eurostates.parser.area.technology;

import org.eurostates.area.state.CustomState;
import org.eurostates.area.state.States;
import org.eurostates.area.technology.Technologies;
import org.eurostates.area.technology.Technology;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.StringParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class GetterTechnologyParser implements StringParser<Technology> {
    @Override
    public @NotNull String to(@NotNull Technology from) throws IOException {
        return Parsers.UUID.to(from.getID());
    }

    @Override
    public @NotNull Technology from(@NotNull String from) throws IOException {
        UUID uuid = Parsers.UUID.from(from);
        return Technologies
                .TECHNOLOGIES
                .parallelStream()
                .filter(technology -> technology.getID().equals(uuid))
                .findAny()
                .orElseThrow(() -> new IOException("Unknown Technology of " + uuid));
    }
}

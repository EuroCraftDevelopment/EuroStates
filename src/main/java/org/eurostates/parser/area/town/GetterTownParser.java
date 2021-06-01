package org.eurostates.parser.area.town;

import org.eurostates.EuroStates;
import org.eurostates.area.town.CustomTown;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.StringParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GetterTownParser implements StringParser<CustomTown> {
    @Override
    public @NotNull String to(@NotNull CustomTown from) throws IOException {
        return Parsers.UUID.to(from.getId());
    }

    @Override
    public @NotNull CustomTown from(@NotNull String from) throws IOException {
        return EuroStates.getPlugin().getTown(Parsers.UUID.from(from)).orElseThrow(() -> new IOException("Unknown town of " + from));
    }
}

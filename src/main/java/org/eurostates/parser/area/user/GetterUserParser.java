package org.eurostates.parser.area.user;

import org.eurostates.EuroStates;
import org.eurostates.area.ESUser;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.StringParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class GetterUserParser implements StringParser<ESUser> {

    public @NotNull UUID toId(@NotNull ESUser from) throws IOException {
        return from.getOwnerId();
    }

    public @NotNull ESUser fromId(@NotNull UUID from) {
        return EuroStates.getPlugin().getUser(from).orElse(new ESUser(from));
    }

    @Override
    public @NotNull String to(@NotNull ESUser from) throws IOException {
        return Parsers.UUID.to(toId(from));
    }

    @Override
    public @NotNull ESUser from(@NotNull String from) throws IOException {
        return fromId(Parsers.UUID.from(from));
    }
}

package org.eurostates.parser.area.town;

import org.eurostates.area.town.CustomTown;
import org.eurostates.area.town.Town;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.StringParser;

import java.io.IOException;

public class GetterTownParser implements StringParser<CustomTown> {
    @Override
    public String to(CustomTown from) throws IOException {
        return Parsers.UUID.to(from.getId());
    }

    @Override
    public CustomTown from(String from) throws IOException {
        return null;
    }
}

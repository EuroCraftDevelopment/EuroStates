package org.eurostates.parser.area.user;

import org.eurostates.area.ESUser;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.StringParser;

import java.io.IOException;
import java.util.UUID;

public class GetterUserParser implements StringParser<ESUser> {
    public UUID toId(ESUser from) throws IOException {
        return null;
    }

    public ESUser fromId(UUID from) throws IOException {
        return null;
    }

    @Override
    public String to(ESUser from) throws IOException {
        return Parsers.UUID.to(toId(from));
    }

    @Override
    public ESUser from(String from) throws IOException {
        return fromId(Parsers.UUID.from(from));
    }
}

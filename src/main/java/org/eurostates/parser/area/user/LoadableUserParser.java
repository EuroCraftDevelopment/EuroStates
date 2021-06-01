package org.eurostates.parser.area.user;

import org.eurostates.area.ESUser;
import org.eurostates.parser.StringMapParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

//TODO
public class LoadableUserParser implements StringMapParser<ESUser> {
    @Override
    public @NotNull Map<String, Object> to(@NotNull ESUser from) throws IOException {
        return null;
    }

    @Override
    public @NotNull ESUser from(@NotNull Map<String, Object> from) throws IOException {
        return null;
    }
}

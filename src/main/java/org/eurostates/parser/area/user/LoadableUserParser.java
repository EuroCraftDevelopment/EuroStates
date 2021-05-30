package org.eurostates.parser.area.user;

import org.eurostates.area.ESUser;
import org.eurostates.parser.StringMapParser;

import java.io.IOException;
import java.util.Map;

public class LoadableUserParser implements StringMapParser<ESUser> {
    @Override
    public Map<String, Object> to(ESUser from) throws IOException {
        return null;
    }

    @Override
    public ESUser from(Map<String, Object> from) throws IOException {
        return null;
    }
}

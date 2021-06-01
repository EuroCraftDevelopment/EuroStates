package org.eurostates.parser.wrapper;

import org.eurostates.parser.StringParser;
import org.jetbrains.annotations.NotNull;

public class StringWrapperParser implements StringParser<String> {
    @Override
    public @NotNull String to(@NotNull String from) {
        return from;
    }

    @Override
    public @NotNull String from(@NotNull String from) {
        return from;
    }
}

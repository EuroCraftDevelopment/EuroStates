package org.eurostates.parser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Parser<T, R> {

    @NotNull R to(@NotNull T from) throws IOException;

    @NotNull T from(@NotNull R from) throws IOException;
}

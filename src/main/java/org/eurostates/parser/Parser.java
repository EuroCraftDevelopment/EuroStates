package org.eurostates.parser;

import java.io.IOException;

public interface Parser<T, R> {

    R to(T from) throws IOException;

    T from(R from) throws IOException;
}

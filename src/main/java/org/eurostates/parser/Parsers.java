package org.eurostates.parser;

import org.eurostates.parser.area.state.GetterStateParser;
import org.eurostates.parser.area.state.LoadableStateParser;
import org.eurostates.parser.area.user.GetterUserParser;
import org.eurostates.parser.util.LocationParser;
import org.eurostates.parser.util.UUIDParser;
import org.eurostates.parser.util.WorldParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

public final class Parsers {

    public static final UUIDParser UUID = new UUIDParser();
    public static final WorldParser WORLD = new WorldParser();
    public static final LocationParser LOCATION = new LocationParser();

    public static final GetterStateParser GETTER_STATE = new GetterStateParser();
    public static final GetterUserParser GETTER_USER = new GetterUserParser();

    public static final LoadableStateParser LOADABLE_STATE = new LoadableStateParser();

    private Parsers() {
        throw new IllegalArgumentException("Whats the benefit to calling this");
    }

    private static <T, R> Function<R, T> fromOrNull(Parser<T, R> parser) {
        return (value -> {
            try {
                return parser.from(value);
            } catch (IOException e) {
                return null;
            }
        });
    }

    private static <T, R> Function<T, R> toOrNull(Parser<T, R> parser) {
        return (value -> {
            try {
                return parser.to(value);
            } catch (IOException e) {
                return null;
            }
        });
    }

    private static <T, R> ThrowableFunction<R, T, IOException> fromOrThrow(Parser<T, R> parser) {
        return (parser::from);
    }

    private static <T, R> ThrowableFunction<T, R, IOException> toOrThrow(Parser<T, R> parser) {
        return (parser::to);
    }

    public static <T, R, C extends Collection<T>> C collectOrFilter(Stream<R> from, Function<R, T> map, Collector<T, ?, C> collector) {
        //this is using a magic value of null, but its faster then parsing twice and the magic value doesn't escape the method
        return from
                .map(map)
                .filter(Objects::nonNull)
                .collect(collector);
    }

    public static <T, R> List<T> collectOrThrow(ThrowableFunction<R, T, IOException> function, Iterable<R> iterable) throws IOException {
        List<T> retur = new ArrayList<>();
        for (R type : iterable) {
            retur.add(function.apply(type));
        }
        return retur;
    }

    public static <T, R, C extends Collection<T>> C collectFromOrFilter(Stream<R> from, Parser<T, R> parser, Collector<T, ?, C> collector) {
        return collectOrFilter(from, fromOrNull(parser), collector);
    }

    public static <T, R, C extends Collection<R>> C collectToOrFilter(Stream<T> from, Parser<T, R> parser, Collector<R, ?, C> collector) {
        return collectOrFilter(from, toOrNull(parser), collector);
    }

    public static <T, R> List<T> collectFromOrThrow(Parser<T, R> parser, Iterable<R> iterable) throws IOException {
        return collectOrThrow(fromOrThrow(parser), iterable);
    }

    public static <T, R> List<R> collectToOrThrow(Parser<T, R> parser, Iterable<T> iterable) throws IOException {
        return collectOrThrow(toOrThrow(parser), iterable);
    }

    public interface ThrowableFunction<O, M, T extends Throwable> {

        M apply(O obj) throws T;

    }


}

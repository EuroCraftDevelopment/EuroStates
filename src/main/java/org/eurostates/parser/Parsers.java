package org.eurostates.parser;

import org.eurostates.lamda.throwable.single.ThrowableFunction;
import org.eurostates.parser.area.state.GetterStateParser;
import org.eurostates.parser.area.state.LoadableStateParser;
import org.eurostates.parser.area.town.GetterTownParser;
import org.eurostates.parser.area.town.LoadableTownParser;
import org.eurostates.parser.area.user.GetterUserParser;
import org.eurostates.parser.area.user.LoadableUserParser;
import org.eurostates.parser.util.BlockLocationParser;
import org.eurostates.parser.util.LocationParser;
import org.eurostates.parser.util.UUIDParser;
import org.eurostates.parser.util.WorldParser;
import org.jetbrains.annotations.NotNull;

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
    public static final BlockLocationParser BLOCK_LOCATION = new BlockLocationParser();

    public static final GetterStateParser GETTER_STATE = new GetterStateParser();
    public static final GetterUserParser GETTER_USER = new GetterUserParser();
    public static final GetterTownParser GETTER_TOWN = new GetterTownParser();

    public static final LoadableStateParser LOADABLE_STATE = new LoadableStateParser();
    public static final LoadableTownParser LOADABLE_TOWN = new LoadableTownParser();
    public static final LoadableUserParser LOADABLE_USER = new LoadableUserParser();

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

    public static <T, R, C extends Collection<T>> @NotNull C collectOrFilter(@NotNull Stream<R> from, @NotNull Function<R, T> map, @NotNull Collector<T, ?, C> collector) {
        //this is using a magic value of null, but its faster then parsing twice and the magic value doesn't escape the method
        return from
                .map(map)
                .filter(Objects::nonNull)
                .collect(collector);
    }

    public static <T, R> List<T> collectOrThrow(@NotNull ThrowableFunction<R, T, IOException> function, @NotNull Iterable<R> iterable) throws IOException {
        List<T> retur = new ArrayList<>();
        for (R type : iterable) {
            retur.add(function.apply(type));
        }
        return retur;
    }

    public static <T, R, C extends Collection<T>> @NotNull C collectFromOrFilter(@NotNull Stream<R> from, @NotNull Parser<T, R> parser, @NotNull Collector<T, ?, C> collector) {
        return collectOrFilter(from, fromOrNull(parser), collector);
    }

    public static <T, R, C extends Collection<R>> @NotNull C collectToOrFilter(@NotNull Stream<T> from, @NotNull Parser<T, R> parser, @NotNull Collector<R, ?, C> collector) {
        return collectOrFilter(from, toOrNull(parser), collector);
    }

    public static <T, R> List<T> collectFromOrThrow(@NotNull Parser<T, R> parser, @NotNull Iterable<R> iterable) throws IOException {
        return collectOrThrow(fromOrThrow(parser), iterable);
    }

    public static <T, R> List<R> collectToOrThrow(@NotNull Parser<T, R> parser, @NotNull Iterable<T> iterable) throws IOException {
        return collectOrThrow(toOrThrow(parser), iterable);
    }
}

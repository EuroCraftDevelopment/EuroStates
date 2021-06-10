package org.eurostates.mosecommands.arguments.simple;

import org.eurostates.mosecommands.arguments.CommandArgument;
import org.eurostates.mosecommands.context.CommandArgumentContext;
import org.eurostates.mosecommands.context.CommandContext;
import org.eurostates.util.lamda.throwable.single.ThrowableFunction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class NumberArgument<N extends Number> implements CommandArgument<N> {

    private final String id;
    private final ThrowableFunction<String, N, NumberFormatException> parse;

    public NumberArgument(String id, ThrowableFunction<String, N, NumberFormatException> parse){
        this.id = id;
        this.parse = parse;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public Map.@NotNull Entry<N, Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<N> argument) throws IOException {
        String arg = context.getCommand()[argument.getFirstArgument()];
        try{
            return new AbstractMap.SimpleImmutableEntry<>(this.parse.apply(arg), argument.getFirstArgument() + 1);
        }catch (NumberFormatException e){
            throw new IOException("Not a valid number");
        }
    }

    @Override
    public @NotNull List<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<N> argument) {
        return Collections.emptyList();
    }

    public static NumberArgument<Integer> asInt(String id){
        return new NumberArgument<>(id, Integer::parseInt);
    }

    public static NumberArgument<Double> asDouble(String id){
        return new NumberArgument<>(id, Double::parseDouble);
    }
}

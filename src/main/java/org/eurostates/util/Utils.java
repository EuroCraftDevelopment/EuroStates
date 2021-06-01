package org.eurostates.util;

import org.eurostates.lamda.throwable.single.ThrowableConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Utils {

    enum Compare {
        BETTER,
        EQUAL,
        WORSE
    }

    static <F, E extends Throwable> @NotNull F throwOr(@NotNull Class<E> clazz, @NotNull ThrowableConsumer<F, E> consumer, @NotNull F fail) {
        try {
            return consumer.run();
        } catch (Throwable e) {
            if (clazz.isInstance(e)) {
                return fail;
            }
            throw new RuntimeException(e);
        }
    }

    static <F, T, E extends T> @NotNull F canCast(@NotNull T object, @NotNull Class<E> clazz, @NotNull Function<E, F> function, @NotNull Function<T, F> elseFunction) {
        if (clazz.isInstance(object)) {
            return function.apply((E) object);
        }
        return elseFunction.apply(object);
    }

    static <T> Set<T> getBest(@NotNull Iterable<T> iterable, @NotNull Comparator<T> check) {
        return getBest(iterable, (BiFunction<T, T, Compare>) (t, t2) -> {
            int diff = check.compare(t, t2);
            if (diff == 0) {
                return Compare.EQUAL;
            }
            if (diff > 0) {
                return Compare.BETTER;
            }
            return Compare.WORSE;
        });
    }

    static <T> Set<T> getBest(@NotNull Iterable<T> iterable, @NotNull BiFunction<T, T, Compare> check) {
        Set<T> winning = null;
        T lastValueAdded = null;
        for (T value : iterable) {
            if (lastValueAdded == null) {
                winning = new HashSet<>();
                winning.add(value);
                lastValueAdded = value;
            }
            Compare compare = check.apply(lastValueAdded, value);
            switch (compare) {
                case BETTER:
                    winning.clear();
                    winning.add(value);
                    lastValueAdded = value;
                    break;
                case EQUAL:
                    winning.add(value);
                    break;
                case WORSE:
                    break;
            }
        }
        return winning;
    }
}

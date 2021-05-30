package org.eurostates.util;

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

    static <F, T, E extends T> F canCast(T object, Class<E> clazz, Function<E, F> function, Function<T, F> elseFunction) {
        if (clazz.isInstance(object)) {
            return function.apply((E) object);
        }
        return elseFunction.apply(object);
    }

    static <T> Set<T> getBest(Iterable<T> iterable, BiFunction<T, T, Compare> check) {
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

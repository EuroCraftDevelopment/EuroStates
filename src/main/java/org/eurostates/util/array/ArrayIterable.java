package org.eurostates.util.array;

import org.jetbrains.annotations.NotNull;

public class ArrayIterable<T> implements Iterable<T> {

    private final T[] array;

    @Deprecated
    public ArrayIterable() {
        throw new IllegalArgumentException("Array must be specified");
    }

    public ArrayIterable(T[] array) {
        this.array = array;
    }

    @NotNull
    @Override
    public ArrayIterator<T> iterator() {
        return iterator(0);
    }

    @NotNull
    public ArrayIterator<T> iterator(int target) {
        return new ArrayIterator<>(target, this.array);
    }
}

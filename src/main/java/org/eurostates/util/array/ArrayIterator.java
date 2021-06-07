package org.eurostates.util.array;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {

    private final T[] array;
    private int target;

    @Deprecated
    public ArrayIterator(int target) {
        throw new IllegalArgumentException("Array must be specified");
    }

    public ArrayIterator(int target, T... array) {
        this.array = array;
        this.target = target;
    }

    public T[] toArray(){
        return this.array;
    }

    public ArrayIterable<T> toIterable(){
        return new ArrayIterable<>(this.array);
    }

    @Override
    public boolean hasNext() {
        return this.target == array.length;
    }

    @Override
    public T next() {
        T next = this.array[this.target];
        this.target++;
        return next;
    }
}

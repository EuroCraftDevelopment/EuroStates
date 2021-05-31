package org.eurostates.lamda.throwable.bi;

public interface ThrowableBiConsumer<E, F, T extends Throwable> {

    void consume(E obj1, F obj2) throws T;
}

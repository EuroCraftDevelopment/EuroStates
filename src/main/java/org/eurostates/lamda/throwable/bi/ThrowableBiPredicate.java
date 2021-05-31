package org.eurostates.lamda.throwable.bi;

public interface ThrowableBiPredicate<C, E, T extends Throwable> {

    boolean test(C obj1, E obj2) throws T;

}

package org.eurostates.lamda.throwable.bi;

public interface ThrowableBiFunction<M, E, R, T extends Throwable> {

    R apply(M arg1, E arg2) throws T;

}

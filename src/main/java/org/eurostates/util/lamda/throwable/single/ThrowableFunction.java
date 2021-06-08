package org.eurostates.util.lamda.throwable.single;

public interface ThrowableFunction<O, M, T extends Throwable> {

    M apply(O obj) throws T;

}
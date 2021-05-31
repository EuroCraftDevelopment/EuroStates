package org.eurostates.lamda.throwable.single;

public interface ThrowableConsumer<F, T extends Throwable> {

    F run() throws T;

}

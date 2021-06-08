package org.eurostates.util.lamda.throwable.single;

public interface ThrowableConsumer<F, T extends Throwable> {

    F run() throws T;

}

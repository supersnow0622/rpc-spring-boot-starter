package com.wlx.middleware.rpc.network.future;

import java.util.concurrent.Future;

public interface WriteFuture<T> extends Future<T> {

    void setResult(T t);

    Throwable cause();

    void setCause(Throwable cause);

    void setWriteSuccess(boolean writeSuccess);

    boolean isWriteSuccess();

    boolean isTimeout();



}

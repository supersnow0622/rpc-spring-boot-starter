package com.wlx.middleware.rpc.network.future;

import com.wlx.middleware.rpc.network.msg.Response;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SyncWriteFuture implements WriteFuture<Response> {

    private final long beginTime = System.currentTimeMillis();

    private Response response;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private boolean writeSuccess;

    private Throwable cause;

    private long timeout;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return true;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return response != null;
    }

    @Override
    public Response get() throws InterruptedException {
        countDownLatch.await();
        return response;
    }

    @Override
    public Response get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        countDownLatch.await(timeout, unit);
        return response;
    }

    @Override
    public void setResult(Response response) {
        this.response = response;
        countDownLatch.countDown();
    }

    @Override
    public Throwable cause() {
        return cause;
    }

    @Override
    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public void setWriteSuccess(boolean writeSuccess) {
        this.writeSuccess = writeSuccess;
    }

    @Override
    public boolean isWriteSuccess() {
        return writeSuccess;
    }

    @Override
    public boolean isTimeout() {
        return System.currentTimeMillis() - beginTime > timeout;
    }


}

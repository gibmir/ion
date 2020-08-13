package com.github.gibmir.ion.lib.netty.client.environment.mock;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FutureChannelMock {

  private FutureChannelMock() {
  }

  @SuppressWarnings("unchecked")
  public static Future<Channel> newMock(Channel channel, final boolean isSuccess) throws ExecutionException, InterruptedException {
    Future<Channel> future = mock(Future.class);
    when(future.isDone()).then(__ -> isSuccess);
//    doAnswer(__ -> true).when(future).isDone();
    doAnswer(__ -> channel).when(future).get();

    doAnswer(invocation -> {
      GenericFutureListener argument = invocation.getArgument(0, GenericFutureListener.class);
      argument.operationComplete(new Future<>() {
        @Override
        public boolean isSuccess() {
          return isSuccess;
        }

        @Override
        public boolean isCancellable() {
          return false;
        }

        @Override
        public Throwable cause() {
          return null;
        }

        @Override
        public Future<Object> addListener(GenericFutureListener<? extends Future<? super Object>> listener) {
          return null;
        }

        @Override
        public Future<Object> addListeners(GenericFutureListener<? extends Future<? super Object>>... listeners) {
          return null;
        }

        @Override
        public Future<Object> removeListener(GenericFutureListener<? extends Future<? super Object>> listener) {
          return null;
        }

        @Override
        public Future<Object> removeListeners(GenericFutureListener<? extends Future<? super Object>>... listeners) {
          return null;
        }

        @Override
        public Future<Object> sync() {
          return null;
        }

        @Override
        public Future<Object> syncUninterruptibly() {
          return null;
        }

        @Override
        public Future<Object> await() {
          return null;
        }

        @Override
        public Future<Object> awaitUninterruptibly() {
          return null;
        }

        @Override
        public boolean await(long timeout, TimeUnit unit) {
          return false;
        }

        @Override
        public boolean await(long timeoutMillis) {
          return false;
        }

        @Override
        public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
          return false;
        }

        @Override
        public boolean awaitUninterruptibly(long timeoutMillis) {
          return false;
        }

        @Override
        public Object getNow() {
          return channel;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
          return false;
        }

        @Override
        public boolean isCancelled() {
          return false;
        }

        @Override
        public boolean isDone() {
          return isSuccess;
        }

        @Override
        public Object get() {
          return channel;
        }

        @Override
        public Object get(long timeout, TimeUnit unit) {
          return channel;
        }
      });
      return null;
    }).when(future).addListener(any());
    return future;
  }
}

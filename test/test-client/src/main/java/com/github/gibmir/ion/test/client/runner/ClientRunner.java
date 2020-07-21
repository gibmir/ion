package com.github.gibmir.ion.test.client.runner;

import com.github.gibmir.ion.api.client.request.Request1;
import com.github.gibmir.ion.api.client.factory.RequestFactory;
import com.github.gibmir.ion.api.client.factory.provider.RequestFactoryProvider;
import com.github.gimbir.ion.test.common.procedure.TestStringProcedure;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClientRunner {
  public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
    RequestFactory requestFactory = RequestFactoryProvider.load().provide();
    Request1<String, String> request = requestFactory.singleArg(TestStringProcedure.class, String.class);
    LocalTime start = LocalTime.now();
    int counter = 0;
    for (int i = 0; i < 1000; i++) {
      String argument = request.positionalCall("id-1", "argument").get(10, TimeUnit.SECONDS);
      if (argument!=null) {
        counter++;
      }
    }
    System.out.println(Duration.between(start, LocalTime.now())+":"+counter);
  }
}

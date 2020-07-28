package com.github.gibmir.ion.test.client.runner;

import com.github.gibmir.ion.api.client.batch.request.BatchRequest;
import com.github.gibmir.ion.api.client.batch.response.BatchResponse;
import com.github.gibmir.ion.api.client.factory.RequestFactory;
import com.github.gibmir.ion.api.client.factory.provider.RequestFactoryProvider;
import com.github.gibmir.ion.api.client.request.Request1;
import com.github.gimbir.ion.test.common.procedure.TestStringProcedure;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ClientRunner {
  public static void main(String[] args) {
    RequestFactory requestFactory = RequestFactoryProvider.load().provide();
//    sendBatch(requestFactory);
    Request1<String, String> request = requestFactory.singleArg(TestStringProcedure.class, String.class);
    request.notificationCall("pog");
//    send1000Request(request);
  }

  private static void send1000Request(Request1<String, String> request) throws InterruptedException, ExecutionException, java.util.concurrent.TimeoutException {
    LocalTime start = LocalTime.now();
    int counter = 0;
    for (int i = 0; i < 1000; i++) {
      String argument = request.positionalCall("id-1", "argument").get(10, TimeUnit.SECONDS);
      if (argument != null) {
        counter++;
      }
    }
    System.out.println(Duration.between(start, LocalTime.now()) + ":" + counter);
  }

  private static void sendBatch(RequestFactory requestFactory) throws InterruptedException, ExecutionException {
    BatchRequest batchRequest = requestFactory.batch()
      .addPositional("first-batch", TestStringProcedure.class, "argument", String.class)
      .addPositional("second-batch", TestStringProcedure.class, "secondArgument", String.class)
      .addPositional("third-batch", TestStringProcedure.class, "thirdArgument", String.class)
      .build();
    BatchResponse batchResponse = batchRequest.batchCall().get();
    System.out.println(batchResponse.getBatchResponseElements());
  }
}

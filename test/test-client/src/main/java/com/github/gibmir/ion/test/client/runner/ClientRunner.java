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
import java.util.concurrent.TimeoutException;

public class ClientRunner {
  public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
    RequestFactory requestFactory = RequestFactoryProvider.load().provide();
    sendBatch(requestFactory);
    send5Request(requestFactory);
    send5NamedRequest(requestFactory);
    sendNotification(requestFactory);
  }

  private static void send5Request(RequestFactory requestFactory) throws InterruptedException, ExecutionException, TimeoutException {
    Request1<String, String> request = requestFactory.singleArg(TestStringProcedure.class);
    LocalTime start = LocalTime.now();
    int counter = 0;
    for (int i = 0; i < 5; i++) {
      String argument = request.positionalCall("id-1", "argument").get(10, TimeUnit.SECONDS);
      if (argument != null) {
        counter++;
      }
    }
    System.out.println(Duration.between(start, LocalTime.now()) + ":" + counter);
  }

  private static void sendNotification(RequestFactory requestFactory) {
    Request1<String, String> request = requestFactory.singleArg(TestStringProcedure.class);
    request.notificationCall("notification message");
  }

  private static void send5NamedRequest(RequestFactory requestFactory) throws InterruptedException, ExecutionException, TimeoutException {
    Request1<String, String> request = requestFactory.singleArg(TestStringProcedure.class);
    LocalTime start = LocalTime.now();
    int counter = 0;
    for (int i = 0; i < 5; i++) {
      String argument = request.namedCall("id-1", "argument").get(10, TimeUnit.SECONDS);
      if (argument != null) {
        counter++;
      }
    }
    System.out.println(Duration.between(start, LocalTime.now()) + ":" + counter);
  }

  private static void sendBatch(RequestFactory requestFactory) throws InterruptedException, ExecutionException {
    BatchRequest batchRequest = requestFactory.batch()
      .addPositional("first-batch", TestStringProcedure.class, "argument")
      .addPositional("second-batch", TestStringProcedure.class, "secondArgument")
      .addPositional("third-batch", TestStringProcedure.class, "thirdArgument")
      .addNotification(TestStringProcedure.class, "notification argument")
      .build();
    BatchResponse batchResponse = batchRequest.call().get();
    batchResponse.getBatchResponseElements().forEach(batchElement -> System.out.println(batchElement.getId() + ":" + batchElement.getResponseObject()));
  }
}

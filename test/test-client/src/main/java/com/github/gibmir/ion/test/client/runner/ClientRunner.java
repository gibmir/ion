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
    sendNotification(requestFactory);
    sendRequest(requestFactory, 1);
    sendNamedRequest(requestFactory, 1);
  }

  private static void sendRequest(RequestFactory requestFactory, int times) throws InterruptedException, ExecutionException, TimeoutException {
    Request1<String, String> request = requestFactory.singleArg(TestStringProcedure.class);
    LocalTime start = LocalTime.now();
    int counter = 0;
    for (int i = 0; i < times; i++) {
      String argument = request.positionalCall("id-1", "argument").get(10, TimeUnit.SECONDS);
      if (argument != null) {
        counter++;
      }
    }
    System.out.println(Duration.between(start, LocalTime.now()) + ":" + counter);
  }

  private static void sendNotification(RequestFactory requestFactory) {
    Request1<String, String> request = requestFactory.singleArg(TestStringProcedure.class);
    request.positionalNotificationCall("notification message");
    request.namedNotificationCall("notification message");
    System.out.println("notification was sent");
  }

  private static void sendNamedRequest(RequestFactory requestFactory, int times) throws InterruptedException, ExecutionException, TimeoutException {
    Request1<String, String> request = requestFactory.singleArg(TestStringProcedure.class);
    LocalTime start = LocalTime.now();
    int counter = 0;
    for (int i = 0; i < times; i++) {
      String argument = request.namedCall("id-1", "argument").get(10, TimeUnit.SECONDS);
      if (argument != null) {
        counter++;
      }
    }
    System.out.println(Duration.between(start, LocalTime.now()) + ":" + counter);
  }

  private static void sendBatch(RequestFactory requestFactory) throws InterruptedException, ExecutionException {
    BatchRequest batchRequest = requestFactory.batch()
      .addPositionalRequest("first-batch", TestStringProcedure.class, "argument")
      .addPositionalRequest("second-batch", TestStringProcedure.class, "secondArgument")
      .addPositionalRequest("third-batch", TestStringProcedure.class, "thirdArgument")
      .addPositionalNotification(TestStringProcedure.class, "notification argument")
      .build();
    BatchResponse batchResponse = batchRequest.call().get();
    batchResponse.getBatchResponseElements().forEach(batchElement -> System.out.println(batchElement.getId() + ":" + batchElement.getResponseObject()));
  }
}

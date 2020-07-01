package com.github.gibmir.ion.lib.http.client.request;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import org.junit.jupiter.api.Test;

class HttpRequest0Test {
  public interface TestApi extends JsonRemoteProcedure1<String, String> {
  }

  @Test
  void call() {
//    HttpRequestFactory httpRequestFactory = new HttpRequestFactory(/*здесь дефолтные значения и java httpClient*/);
//
    //позиционный запрос
//    httpRequestFactory.singleArg(TestApi.class, String.class)
    //Можно слепить из URL(в фабрике дефолт)
//      .uri(URI.create("http://tobipizda"))
//      .timeout(Duration.ofSeconds(30))
//      .charset(StandardCharsets.UTF_8)
//      .positionalCall("pizdatoe id", "method argument")
    // Вот тут CompletableFuture - твори что хочешь
//      .whenComplete((result, throwable) -> System.out.println(result + ":" + throwable));

    //именованный запрос
//    httpRequestFactory.singleArg(TestApi.class, String.class)
    //Можно слепить из URL
//      .uri(URI.create("http://tobipizda"))
//      .timeout(Duration.ofSeconds(30))
//      .charset(StandardCharsets.UTF_8)
//      .namedCall("pizdatoe id", NamedArgument.nameAs("beauty", "argument"))
    // Вот тут CompletableFuture - твори что хочешь
//      .whenComplete((result, throwable) -> System.out.println(result + ":" + throwable));

    //нотификационный запрос
//    httpRequestFactory.singleArg(TestApi.class, String.class)
    //Можно слепить из URL
//      .uri(URI.create("http://tobipizda"))
//      .timeout(Duration.ofSeconds(30))
//      .charset(StandardCharsets.UTF_8)
    //вернет void
//      .notificationCall("argument");
  }
}

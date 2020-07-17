package com.github.gibmir.ion.lib.http.client.request;

import com.github.gibmir.ion.api.client.request.factory.RequestFactory;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.lib.http.client.request.factory.HttpRequestFactory;
import com.github.gibmir.ion.lib.http.client.request.factory.provider.HttpRequestFactoryProvider;
import com.github.gibmir.ion.lib.http.client.sender.HttpRequestSender;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Duration;

class HttpRequest0Test {
  public interface TestProcedure extends JsonRemoteProcedure1<RequestDto, ResponseDto>, JsonRemoteProcedure0<ResponseDto> {
  }

  private static class RequestDto {
  }

  private static class ResponseDto {
  }

  @Test
  void call() {

  }
}

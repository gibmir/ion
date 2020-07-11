package com.github.gibmir.ion.lib.http.client.request;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.lib.http.client.request.factory.HttpRequestFactory;
import org.junit.jupiter.api.Test;

import java.net.URI;

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

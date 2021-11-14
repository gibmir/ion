package com.github.gibmir.ion.lib.netty.client.tcp.environment.mock;

import org.checkerframework.checker.nullness.qual.NonNull;

import javax.json.bind.Jsonb;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class JsonbMock {

  private JsonbMock() {
  }

  public static Jsonb newMock(@NonNull Object object) {
    Jsonb jsonb = mock(Jsonb.class);
    doAnswer(__ -> object.toString()).when(jsonb).toJson(any());
    doAnswer(__ -> object.toString()).when(jsonb).fromJson(anyString(), any());
    return jsonb;
  }
}

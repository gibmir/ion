package com.github.gibmir.ion.api.client.request.factory.properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class RequestConstants {
  private RequestConstants() {
  }
  //todo jsonb config
  public static final String REQUEST_TIMEOUT_PROPERTY = "";
  public static final String REQUEST_CHARSET_PROPERTY = "";
  public static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(1);
  public static final Jsonb DEFAULT_JSONB = JsonbBuilder.create();
  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
}

package com.github.gibmir.ion.api.client.request.factory.properties;

import com.github.gibmir.ion.api.client.request.argument.NamedArgument;
import com.github.gibmir.ion.api.client.request.factory.properties.environment.Environment;
import com.github.gibmir.ion.api.client.request.factory.properties.environment.stub.ConfigurationTestStub;
import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class RequestConfigurationUtilsTest {

  @Test
  void testCreateCharsetWith() {
  }

  @Test
  void testCreateTimeoutWith() {
  }

  @Test
  void testCreateJsonbWith() {
    Configuration configurationTestStub = new ConfigurationTestStub(Map.of(ConfigurationUtils.JSONB_NULL_VALUES, false));
    Jsonb jsonb = ConfigurationUtils.createJsonbWith(configurationTestStub);
    String json = jsonb.toJson(Map.of("name","obje","names","obj"));
    System.out.println(json);
  }
}

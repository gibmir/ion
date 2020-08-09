package com.github.gibmir.ion.lib.netty.client.environment.mock;

import javax.json.JsonValue;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class JsonValueMock {
  public static JsonValue newMock(JsonValue.ValueType valueType) {
    JsonValue jsonValue = mock(JsonValue.class);
    doAnswer(__ -> valueType).when(jsonValue).getValueType();
    return jsonValue;
  }
}

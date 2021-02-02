package com.github.gibmir.ion.lib.netty.client.sender.codecs.decoder;

import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DecoderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonParsingException;

import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_REAL_JSONB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonRpcResponseDecoderTest {

  public static final EmbeddedChannel TEST_EMBEDDED_CHANNEL = new EmbeddedChannel();
  public static final JsonRpcResponseDecoder TEST_JSON_RPC_RESPONSE_DECODER = new JsonRpcResponseDecoder(TEST_REAL_JSONB,
    TestEnvironment.TEST_CHARSET);

  @BeforeAll
  static void beforeAll() {
    TEST_EMBEDDED_CHANNEL.pipeline().addLast(TEST_JSON_RPC_RESPONSE_DECODER);
  }

  @Test
  void testCorrectJsonObjectDecoding() {
    String key = "value";
    String value = "test";
    TEST_EMBEDDED_CHANNEL.writeInbound(Unpooled.wrappedBuffer(("{ \"" + key + "\":\"" + value + "\" }").getBytes()));
    JsonValue jsonValue = TEST_EMBEDDED_CHANNEL.readInbound();
    assertEquals(JsonValue.ValueType.OBJECT, jsonValue.getValueType());
    JsonObject jsonObject = jsonValue.asJsonObject();
    JsonValue jsonKeyValue = jsonObject.get(key);
    assertEquals(JsonValue.ValueType.STRING, jsonKeyValue.getValueType());
  }

  @Test
  void testCorrectJsonArrayDecoding() {
    String firstElement = "somebody";
    String secondElement = "to";
    String thirdElement = "love";
    TEST_EMBEDDED_CHANNEL.writeInbound(Unpooled.wrappedBuffer(("[\""
      + firstElement + "\",\""
      + secondElement + "\",\""
      + thirdElement + "\"]").getBytes()));
    JsonValue jsonValue = TEST_EMBEDDED_CHANNEL.readInbound();
    assertEquals(JsonValue.ValueType.ARRAY, jsonValue.getValueType());
    JsonArray jsonArray = jsonValue.asJsonArray();
    assertEquals(3, jsonArray.size());
    assertTrue(jsonArray.get(0).toString().contains(firstElement));
    assertTrue(jsonArray.get(1).toString().contains(secondElement));
    assertTrue(jsonArray.get(2).toString().contains(thirdElement));
  }

  @Test
  void testIncorrectJsonDecoding() {
    EmbeddedChannel embeddedChannel = new EmbeddedChannel();
    JsonRpcResponseDecoder jsonRpcResponseDecoder = new JsonRpcResponseDecoder(TEST_REAL_JSONB,
      TestEnvironment.TEST_CHARSET);
    embeddedChannel.pipeline().addLast(jsonRpcResponseDecoder);
    DecoderException decoderException = assertThrows(DecoderException.class,
      () -> embeddedChannel.writeInbound(Unpooled.wrappedBuffer("incorrect".getBytes())));
    assertTrue(decoderException.getCause() instanceof JsonParsingException);
  }
}

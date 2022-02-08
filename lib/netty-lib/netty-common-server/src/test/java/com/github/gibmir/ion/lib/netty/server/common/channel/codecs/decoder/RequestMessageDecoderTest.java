package com.github.gibmir.ion.lib.netty.server.common.channel.codecs.decoder;

import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.message.Message;
import com.github.gibmir.ion.lib.netty.common.message.NettyBatchMessage;
import com.github.gibmir.ion.lib.netty.common.message.NettyExceptionMessage;
import com.github.gibmir.ion.lib.netty.common.message.NettyNotificationMessage;
import com.github.gibmir.ion.lib.netty.common.message.NettyRequestMessage;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.hamcrest.collection.IsArrayWithSize.emptyArray;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class RequestMessageDecoderTest {

  @Test
  void testDecodeWithIncorrectType() {
    RequestMessageDecoder decoder = new RequestMessageDecoder();
    EmbeddedChannel channel = new EmbeddedChannel(decoder);
    JsonValue jsonValue = mock(JsonValue.class);
    doAnswer(__ -> JsonValue.ValueType.NULL).when(jsonValue).getValueType();

    channel.writeInbound(jsonValue);
    NettyExceptionMessage message = (NettyExceptionMessage) channel.inboundMessages().remove();

    assertThat(message.getCode(), equalTo(Errors.INVALID_RPC.getError().getCode()));
    assertThat(message.getMessage(), containsString(Errors.INVALID_RPC.getError().getMessage()));
  }

  @Test
  void testDecodeIncorrectProtocol() {
    RequestMessageDecoder decoder = new RequestMessageDecoder();
    EmbeddedChannel channel = new EmbeddedChannel(decoder);
    JsonObject json = mock(JsonObject.class);
    doAnswer(__ -> JsonValue.ValueType.OBJECT).when(json).getValueType();

    JsonString incorrectProtocolJson = mock(JsonString.class);
    doAnswer(__ -> "33.0").when(incorrectProtocolJson).getString();

    doAnswer(__ -> incorrectProtocolJson).when(json).getJsonString(eq(SerializationProperties.PROTOCOL_KEY));
    doAnswer(__ -> json).when(json).asJsonObject();

    channel.writeInbound(json);
    NettyExceptionMessage message = (NettyExceptionMessage) channel.inboundMessages().remove();

    assertThat(message.getMessage(), containsString(incorrectProtocolJson.toString()));
    assertThat(message.getCode(), equalTo(Errors.INVALID_RPC.getError().getCode()));
    assertThat(message.getMessage(), containsString(Errors.INVALID_RPC.getError().getMessage()));
  }

  @Test
  void testDecodeNullMethod() {
    RequestMessageDecoder decoder = new RequestMessageDecoder();
    EmbeddedChannel channel = new EmbeddedChannel(decoder);
    JsonObject json = mock(JsonObject.class);
    doAnswer(__ -> JsonValue.ValueType.OBJECT).when(json).getValueType();

    JsonString protocolJson = mock(JsonString.class);
    doAnswer(__ -> "2.0").when(protocolJson).getString();

    doAnswer(__ -> protocolJson).when(json).getJsonString(eq(SerializationProperties.PROTOCOL_KEY));
    doAnswer(__ -> json).when(json).asJsonObject();

    channel.writeInbound(json);
    NettyExceptionMessage message = (NettyExceptionMessage) channel.inboundMessages().remove();

    assertThat(message.getMessage(), not(containsString(protocolJson.toString())));
    assertThat(message.getCode(), equalTo(Errors.INVALID_RPC.getError().getCode()));
    assertThat(message.getMessage(), containsString(Errors.INVALID_RPC.getError().getMessage()));
  }

  @Test
  void testDecodeNotificationWithoutArgs() {
    RequestMessageDecoder decoder = new RequestMessageDecoder();
    EmbeddedChannel channel = new EmbeddedChannel(decoder);
    JsonObject json = mock(JsonObject.class);
    doAnswer(__ -> JsonValue.ValueType.OBJECT).when(json).getValueType();

    JsonString protocolJson = mock(JsonString.class);
    doAnswer(__ -> "2.0").when(protocolJson).getString();

    JsonString methodJson = mock(JsonString.class);
    String methodName = "testMethod";
    doAnswer(__ -> methodName).when(methodJson).getString();

    doAnswer(__ -> protocolJson).when(json).getJsonString(eq(SerializationProperties.PROTOCOL_KEY));
    doAnswer(__ -> methodJson).when(json).getJsonString(eq(SerializationProperties.METHOD_KEY));
    doAnswer(__ -> json).when(json).asJsonObject();

    channel.writeInbound(json);
    NettyNotificationMessage message = (NettyNotificationMessage) channel.inboundMessages().remove();

    assertThat(message.getArgumentsJson(), is(nullValue()));
    assertThat(message.getMethodName(), containsString(methodName));
  }

  @Test
  void testDecodeNotificationWithArgs() {
    RequestMessageDecoder decoder = new RequestMessageDecoder();
    EmbeddedChannel channel = new EmbeddedChannel(decoder);
    JsonObject json = mock(JsonObject.class);
    doAnswer(__ -> JsonValue.ValueType.OBJECT).when(json).getValueType();

    JsonString protocolJson = mock(JsonString.class);
    doAnswer(__ -> "2.0").when(protocolJson).getString();

    JsonString methodJson = mock(JsonString.class);
    String methodName = "testMethod";
    doAnswer(__ -> methodName).when(methodJson).getString();

    JsonValue argsJson = mock(JsonString.class);
    String args = "arguments";
    doAnswer(__ -> args).when(argsJson).toString();

    doAnswer(__ -> protocolJson).when(json).getJsonString(eq(SerializationProperties.PROTOCOL_KEY));
    doAnswer(__ -> methodJson).when(json).getJsonString(eq(SerializationProperties.METHOD_KEY));
    doAnswer(__ -> argsJson).when(json).get(eq(SerializationProperties.PARAMS_KEY));
    doAnswer(__ -> json).when(json).asJsonObject();

    channel.writeInbound(json);
    NettyNotificationMessage message = (NettyNotificationMessage) channel.inboundMessages().remove();

    assertThat(message.getArgumentsJson(), equalTo(args));
    assertThat(message.getMethodName(), containsString(methodName));
  }

  @Test
  void testDecodeRequestWithoutArgs() {
    RequestMessageDecoder decoder = new RequestMessageDecoder();
    EmbeddedChannel channel = new EmbeddedChannel(decoder);
    JsonObject json = mock(JsonObject.class);
    doAnswer(__ -> JsonValue.ValueType.OBJECT).when(json).getValueType();

    JsonString protocolJson = mock(JsonString.class);
    doAnswer(__ -> "2.0").when(protocolJson).getString();

    JsonString methodJson = mock(JsonString.class);
    String methodName = "testMethod";
    doAnswer(__ -> methodName).when(methodJson).getString();

    JsonString idJson = mock(JsonString.class);
    String id = "testMethod";
    doAnswer(__ -> id).when(idJson).getString();

    doAnswer(__ -> methodJson).when(json).getJsonString(eq(SerializationProperties.METHOD_KEY));
    doAnswer(__ -> protocolJson).when(json).getJsonString(eq(SerializationProperties.PROTOCOL_KEY));
    doAnswer(__ -> idJson).when(json).getJsonString(eq(SerializationProperties.ID_KEY));
    doAnswer(__ -> json).when(json).asJsonObject();

    channel.writeInbound(json);
    NettyRequestMessage message = (NettyRequestMessage) channel.inboundMessages().remove();

    assertThat(message.getId(), equalTo(id));
    assertThat(message.getArgumentsJson(), is(nullValue()));
    assertThat(message.getMethodName(), containsString(methodName));
  }

  @Test
  void testDecodeEmptyBatch() {
    RequestMessageDecoder decoder = new RequestMessageDecoder();
    EmbeddedChannel channel = new EmbeddedChannel(decoder);

    JsonArray json = mock(JsonArray.class);
    doAnswer(__ -> JsonValue.ValueType.ARRAY).when(json).getValueType();
    doAnswer(__ -> json).when(json).asJsonArray();

    channel.writeInbound(json);
    NettyBatchMessage message = (NettyBatchMessage) channel.inboundMessages().remove();
    assertThat(message, not(nullValue()));
    assertThat(message.getMessages(), is(emptyArray()));
  }

  public static class TestException extends RuntimeException {

  }

  @Test
  void testDecodeBatchWithException() {
    RequestMessageDecoder decoder = new RequestMessageDecoder();
    EmbeddedChannel channel = new EmbeddedChannel(decoder);

    JsonArray json = mock(JsonArray.class);

    doAnswer(__ -> JsonValue.ValueType.ARRAY).when(json).getValueType();
    int size = 1;
    doAnswer(__ -> size).when(json).size();
    doThrow(TestException.class).when(json).get(0);
    doAnswer(__ -> json).when(json).asJsonArray();

    channel.writeInbound(json);
    NettyBatchMessage batch = (NettyBatchMessage) channel.inboundMessages().remove();
    assertThat(batch, not(nullValue()));
    assertThat(batch.getMessages(), arrayWithSize(size));
    NettyExceptionMessage message = (NettyExceptionMessage) batch.getMessages()[0];
    assertThat(message.getCode(), equalTo(Errors.INVALID_RPC.getError().getCode()));
    assertThat(message.getMessage(), containsString(Errors.INVALID_RPC.getError().getMessage()));
    assertThat(message.getMessage(), containsString(TestException.class.getName()));

  }

  @Test
  void testDecodeBatchWithOneArg() {
    RequestMessageDecoder decoder = new RequestMessageDecoder();
    EmbeddedChannel channel = new EmbeddedChannel(decoder);

    JsonArray json = mock(JsonArray.class);

    doAnswer(__ -> JsonValue.ValueType.ARRAY).when(json).getValueType();
    int size = 1;
    doAnswer(__ -> size).when(json).size();

    JsonObject batchPartJson = mock(JsonObject.class);
    doAnswer(__ -> JsonValue.ValueType.NULL).when(batchPartJson).getValueType();
    doAnswer(__ -> batchPartJson).when(batchPartJson).asJsonObject();
    doAnswer(__ -> batchPartJson).when(json).get(eq(0));
    doAnswer(__ -> json).when(json).asJsonArray();

    channel.writeInbound(json);
    NettyBatchMessage batch = (NettyBatchMessage) channel.inboundMessages().remove();
    assertThat(batch, not(nullValue()));
    assertThat(batch.getMessages(), arrayWithSize(size));
    NettyExceptionMessage message = (NettyExceptionMessage) batch.getMessages()[0];
    assertThat(message.getCode(), equalTo(Errors.INVALID_RPC.getError().getCode()));
    assertThat(message.getMessage(), containsString(Errors.INVALID_RPC.getError().getMessage()));
  }
}

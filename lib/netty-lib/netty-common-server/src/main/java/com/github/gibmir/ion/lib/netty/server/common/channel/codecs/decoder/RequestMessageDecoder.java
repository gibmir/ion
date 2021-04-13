package com.github.gibmir.ion.lib.netty.server.common.channel.codecs.decoder;

import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.error.JsonRpcError;
import com.github.gibmir.ion.api.message.Message;
import com.github.gibmir.ion.lib.netty.common.message.NettyBatchMessage;
import com.github.gibmir.ion.lib.netty.common.message.NettyExceptionMessage;
import com.github.gibmir.ion.lib.netty.common.message.NettyNotificationMessage;
import com.github.gibmir.ion.lib.netty.common.message.NettyRequestMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import java.util.List;

/**
 * Decodes {@link JsonValue} into {@link Message} for further processing.
 */
public class RequestMessageDecoder extends MessageToMessageDecoder<JsonValue> {
  private static final Logger LOGGER = LoggerFactory.getLogger(RequestMessageDecoder.class);

  @Override
  protected void decode(ChannelHandlerContext ctx, JsonValue json, List<Object> out) {
    out.add(decodeMessage(json));
  }

  private static Message decodeMessage(JsonValue json) {
    switch (json.getValueType()) {
      case ARRAY:
        //if json is array - it contains batch
        return decodeBatch(json.asJsonArray());
      case OBJECT:
        //if json is object - it contains named/positional request/notification
        return decodeRequest(json.asJsonObject());
      default:
        //other value types - incorrect json
        JsonRpcError error = Errors.INVALID_RPC.getError();
        return NettyExceptionMessage.withoutId(error.getCode(),
          String.format("%s Incorrect json type", error.getMessage()));
    }
  }

  private static Message decodeBatch(JsonArray jsonArray) {
    int batchSize = jsonArray.size();
    Message[] batchMessages = new Message[batchSize];
    for (int i = 0; i < batchSize; i++) {
      try {
        JsonObject jsonObject = jsonArray.get(i).asJsonObject();
        batchMessages[i] = decodeRequest(jsonObject);
      } catch (Exception e) {
        LOGGER.error("Unexpected exception occurred while decoding batch ", e);
        JsonRpcError error = Errors.INVALID_RPC.getError();
        batchMessages[i] = NettyExceptionMessage.withoutId(error.getCode(), error.getMessage() + e.getMessage());
      }
    }
    return new NettyBatchMessage(batchMessages);
  }

  private static Message decodeRequest(JsonObject jsonObject) {
    String id = resolveId(jsonObject);
    String argumentsJson = resolveArgumentsJson(jsonObject);
    JsonString protocolJson = jsonObject.getJsonString(SerializationProperties.PROTOCOL_KEY);
    JsonString methodJson = jsonObject.getJsonString(SerializationProperties.METHOD_KEY);

    if (protocolJson == null || /*if not json-rpc 2.0*/!protocolJson.getString().equals("2.0")) {
      JsonRpcError error = Errors.INVALID_RPC.getError();
      String message = String.format("%s Protocol [%s] is incorrect for request with id [%s] ", error.getMessage(),
        protocolJson, id);
      LOGGER.error(message);
      return NettyExceptionMessage.create(id, error.getCode(), message);
    }
    if (methodJson == null) {
      JsonRpcError error = Errors.INVALID_RPC.getError();
      String message = String.format("%s Method is not present for request with id [%s] ", error.getMessage(), id);
      LOGGER.error(message);
      return NettyExceptionMessage.create(id, error.getCode(), message);
    }
    String method = methodJson.getString();
    if (id == null) {
      return new NettyNotificationMessage(method, argumentsJson);
    }
    return new NettyRequestMessage(id, method, argumentsJson);
  }

  private static String resolveId(JsonObject json) {
    JsonString idJson = json.getJsonString(SerializationProperties.ID_KEY);
    if (idJson == null) {
      //notification
      return null;
    } else {
      //request
      return idJson.getString();
    }
  }

  private static String resolveArgumentsJson(JsonObject json) {
    JsonValue argumentsJson = json.get(SerializationProperties.PARAMS_KEY);
    if (argumentsJson != null) {
      return argumentsJson.toString();
    } else {
      //procedure without args
      return null;
    }
  }
}

package com.github.gibmir.ion.api.server.cache.processor;

import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.batch.BatchResponseDto;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.error.JsonRpcError;
import com.github.gibmir.ion.api.dto.response.transfer.notification.NotificationResponse;
import com.github.gibmir.ion.api.message.BatchMessage;
import com.github.gibmir.ion.api.message.ExceptionMessage;
import com.github.gibmir.ion.api.message.Message;
import com.github.gibmir.ion.api.message.NotificationMessage;
import com.github.gibmir.ion.api.message.RequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ServerProcessor {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServerProcessor.class);
  private final ProcedureProcessorRegistry procedureProcessorRegistry;

  public ServerProcessor(ProcedureProcessorRegistry procedureProcessorRegistry) {
    this.procedureProcessorRegistry = procedureProcessorRegistry;
  }

  public JsonRpcResponse process(Message message) {
    switch (message.resolveType()) {
      case BATCH:
        return processBatch(message.asBatch());
      case REQUEST:
        return processRequest(message.asRequest());
      case EXCEPTION:
        return processException(message.asException());
      case NOTIFICATION:
        return processNotification(message.asNotification());
      default:
        return ErrorResponse.withNullId(Errors.INTERNAL_RPC_ERROR.getError()
          .appendMessage(" Can't describe request message"));
    }
  }

  private JsonRpcResponse processBatch(BatchMessage batchMessage) {
    //todo parallel batch processing
    List<JsonRpcResponse> responses = new ArrayList<>();
    for (Message message : batchMessage.getMessages()) {
      switch (message.resolveType()) {
        case REQUEST:
          responses.add(processRequest(message.asRequest()));
          break;
        case EXCEPTION:
          responses.add(processException(message.asException()));
          break;
        case NOTIFICATION:
          processNotification(message.asNotification());
          break;
        case BATCH:
        default:
          LOGGER.error("Batch part has incorrect format.");
      }
    }
    return new BatchResponseDto(responses.toArray(JsonRpcResponse[]::new));
  }

  private JsonRpcResponse processRequest(RequestMessage requestMessage) {
    String id = requestMessage.getId();
    String procedure = requestMessage.getMethodName();
    JsonRpcRequestProcessor processor = procedureProcessorRegistry.getProcedureProcessorFor(procedure);
    if (processor != null) {
      return processor.processRequest(id, procedure, requestMessage.getArgumentsJson());
    } else {
      JsonRpcError error = Errors.REQUEST_METHOD_NOT_FOUND.getError()
        .appendMessage(String.format("[%s] not present for request with id [%s]", procedure, id));
      return ErrorResponse.fromJsonRpcError(id, error);
    }
  }

  private NotificationResponse processNotification(NotificationMessage notificationMessage) {
    String procedure = notificationMessage.getMethodName();
    JsonRpcRequestProcessor processor = procedureProcessorRegistry.getProcedureProcessorFor(procedure);
    if (processor != null) {
      processor.processNotification(procedure, notificationMessage.getArgumentsJson());
    } else {
      LOGGER.error("[{}] not present for notification", procedure);
    }
    return NotificationResponse.INSTANCE;
  }

  private ErrorResponse processException(ExceptionMessage exceptionMessage) {
    return ErrorResponse.withId(exceptionMessage.getId(),
      new JsonRpcError(exceptionMessage.getCode(), exceptionMessage.getMessage()));
  }

  public void process(JsonValue jsonValue, Jsonb jsonb, Charset charset, Consumer<byte[]> responseConsumer) {
    switch (jsonValue.getValueType()) {
      case OBJECT:
        processObject((JsonObject) jsonValue, jsonb,
          jsonRpcResponse -> responseConsumer.accept(jsonb.toJson(jsonRpcResponse).getBytes(charset)));
        return;
      case ARRAY:
        processBatch((JsonArray) jsonValue, jsonb, charset, responseConsumer);
        return;
      default:
        responseConsumer.accept(jsonb.toJson(ErrorResponse.withNullId(Errors.INVALID_RPC.getError())).getBytes(charset));
    }
  }

  public void processBatch(JsonArray jsonArray, Jsonb jsonb, Charset charset, Consumer<byte[]> responseConsumer) {
    int batchSize = jsonArray.size();
    List<JsonRpcResponse> jsonRpcResponseList = new ArrayList<>(batchSize);
    for (JsonValue jsonValue : jsonArray) {
      try {
        //process each batch element
        processObject((JsonObject) jsonValue, jsonb, jsonRpcResponseList::add);
      } catch (Exception e) {
        jsonRpcResponseList.add(ErrorResponse.withNullId(e));
      }
    }
    responseConsumer.accept(jsonb.toJson(jsonRpcResponseList).getBytes(charset));
  }

  public void processObject(JsonObject jsonObject, Jsonb jsonb, Consumer<JsonRpcResponse> responseConsumer) {
    JsonValue idValue = jsonObject.get(SerializationProperties.ID_KEY);
    if (idValue == null) {
      processNotification(jsonObject, jsonb);
      return;
    }
    String id = ((JsonString) idValue).getString();
    if (jsonObject.get(SerializationProperties.PROTOCOL_KEY) == null) {
      JsonRpcError jsonRpcError = Errors.INVALID_RPC.getError().appendMessage("Protocol was not present");
      responseConsumer.accept(ErrorResponse.fromJsonRpcError(id, jsonRpcError));
      return;
    }
    JsonValue methodValue = jsonObject.get(SerializationProperties.METHOD_KEY);
    if (methodValue == null) {
      JsonRpcError jsonRpcError = Errors.INVALID_RPC.getError().appendMessage("Method was not present");
      responseConsumer.accept(ErrorResponse.fromJsonRpcError(id, jsonRpcError));
      return;
    }
    LOGGER.info("Starting incoming request processing. id [{}]; method [{}]", idValue, methodValue);
    procedureProcessorRegistry.process(id, ((JsonString) methodValue).getString(), jsonObject,
      jsonb, responseConsumer);
  }

  public void processNotification(JsonObject jsonObject, Jsonb jsonb) {
    if (jsonObject.get(SerializationProperties.PROTOCOL_KEY) == null) {
      LOGGER.error("Exception [{}] occurred while processing notification request.",
        Errors.INVALID_RPC.getError().appendMessage("Protocol was not present"));
      return;
    }
    JsonValue methodValue = jsonObject.get(SerializationProperties.METHOD_KEY);
    if (methodValue == null) {
      LOGGER.error("Exception [{}] occurred while processing notification request.",
        Errors.INVALID_RPC.getError().appendMessage("Method was not present"));
      return;
    }
    LOGGER.info("Starting incoming notification processing. method [{}]", methodValue);
    procedureProcessorRegistry.process(((JsonString) methodValue).getString(), jsonObject,
      jsonb);
  }
}

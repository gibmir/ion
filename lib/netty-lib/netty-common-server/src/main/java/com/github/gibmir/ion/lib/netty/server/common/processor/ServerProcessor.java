package com.github.gibmir.ion.lib.netty.server.common.processor;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.batch.BatchResponseDto;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.error.JsonRpcError;
import com.github.gibmir.ion.api.dto.response.transfer.notification.NotificationResponse;
import com.github.gibmir.ion.api.message.BatchMessage;
import com.github.gibmir.ion.api.message.ExceptionMessage;
import com.github.gibmir.ion.api.message.Message;
import com.github.gibmir.ion.api.message.MessageType;
import com.github.gibmir.ion.api.message.NotificationMessage;
import com.github.gibmir.ion.api.message.RequestMessage;
import com.github.gibmir.ion.api.server.processor.request.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.server.processor.request.registry.ProcedureProcessorRegistry;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public final class ServerProcessor {
  private final Logger logger;
  private final ProcedureProcessorRegistry procedureProcessorRegistry;

  public ServerProcessor(final Logger logger, final ProcedureProcessorRegistry procedureProcessorRegistry) {
    this.logger = logger;
    this.procedureProcessorRegistry = procedureProcessorRegistry;
  }

  /**
   * @param message incoming message request
   * @return request processing result
   */
  public JsonRpcResponse process(final Message message) {
    MessageType messageType = message.resolveType();
    logger.debug("Processing [{}] message", messageType);
    if (messageType == MessageType.BATCH) {
      return processBatch(message.asBatch());
    } else if (messageType == MessageType.REQUEST) {
      return processRequest(message.asRequest());
    } else if (messageType == MessageType.EXCEPTION) {
      return processException(message.asException());
    } else if (messageType == MessageType.NOTIFICATION) {
      return processNotification(message.asNotification());
    } else {
      return ErrorResponse.withNullId(Errors.INTERNAL_RPC_ERROR.getError()
        .appendMessage(" Can't describe request message"));
    }
  }

  private JsonRpcResponse processBatch(final BatchMessage batchMessage) {
    //todo parallel batch processing
    List<JsonRpcResponse> responses = new ArrayList<>();
    for (Message message : batchMessage.getMessages()) {
      MessageType messageType = message.resolveType();
      if (messageType == MessageType.REQUEST) {
        responses.add(processRequest(message.asRequest()));
      } else if (messageType == MessageType.EXCEPTION) {
        responses.add(processException(message.asException()));
      } else if (messageType == MessageType.NOTIFICATION) {
        processNotification(message.asNotification());
      } else {
        logger.error("Batch part has incorrect format [{}].", messageType);
      }
    }
    return new BatchResponseDto(responses.toArray(JsonRpcResponse[]::new));
  }

  private JsonRpcResponse processRequest(final RequestMessage requestMessage) {
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

  private NotificationResponse processNotification(final NotificationMessage notificationMessage) {
    String procedure = notificationMessage.getMethodName();
    JsonRpcRequestProcessor processor = procedureProcessorRegistry.getProcedureProcessorFor(procedure);
    if (processor != null) {
      processor.processNotification(procedure, notificationMessage.getArgumentsJson());
    } else {
      logger.error("[{}] not present for notification", procedure);
    }
    return NotificationResponse.INSTANCE;
  }

  private ErrorResponse processException(final ExceptionMessage exceptionMessage) {
    return ErrorResponse.fromJsonRpcError(exceptionMessage.getId(),
      new JsonRpcError(exceptionMessage.getCode(), exceptionMessage.getMessage()));
  }

}

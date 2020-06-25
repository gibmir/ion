package com.github.gibmir.ion.api.dto.processor.exception;

import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;

public class JsonRpcProcessingException extends RuntimeException {
  public JsonRpcProcessingException(ErrorResponse errorResponse) {
    super(errorResponse.toString());
  }

  public JsonRpcProcessingException(Throwable cause) {
    super(cause);
  }
}

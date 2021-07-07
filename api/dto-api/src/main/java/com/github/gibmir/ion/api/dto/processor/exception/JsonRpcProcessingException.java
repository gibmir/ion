package com.github.gibmir.ion.api.dto.processor.exception;

import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;

public class JsonRpcProcessingException extends RuntimeException {
  /**
   * Creates exception from {@link ErrorResponse}.
   *
   * @param errorResponse json-rpc error representation
   */
  public JsonRpcProcessingException(final ErrorResponse errorResponse) {
    super(errorResponse.toString());
  }

  /**
   * Creates exception with specified cause.
   *
   * @param cause exception cause
   */
  public JsonRpcProcessingException(final Throwable cause) {
    super(cause);
  }
}

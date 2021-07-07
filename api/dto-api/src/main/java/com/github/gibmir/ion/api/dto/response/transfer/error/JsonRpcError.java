package com.github.gibmir.ion.api.dto.response.transfer.error;

import javax.json.bind.annotation.JsonbProperty;

public class JsonRpcError {
  @JsonbProperty("code")
  private int code;
  @JsonbProperty("message")
  private String message;

  public JsonRpcError() {
  }

  public JsonRpcError(final int code, final String message) {
    this.code = code;
    this.message = message;
  }

  /**
   * @return error code
   */
  public int getCode() {
    return code;
  }

  /**
   * Sets error code.
   *
   * @param code error code
   */
  public void setCode(final int code) {
    this.code = code;
  }

  /**
   * @return error message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets error message.
   *
   * @param message error message
   */
  public void setMessage(final String message) {
    this.message = message;
  }

  /**
   * Append message to <b>this</b> error.
   *
   * @param message error message to append
   * @return error with appended message
   */
  public JsonRpcError appendMessage(final String message) {
    this.message += message;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return '{'
      + " code: " + code + ','
      + " message: " + message
      + '}';
  }
}

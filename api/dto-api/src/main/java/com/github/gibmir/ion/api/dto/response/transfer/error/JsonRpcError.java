package com.github.gibmir.ion.api.dto.response.transfer.error;

import javax.json.bind.annotation.JsonbProperty;

public class JsonRpcError {
  @JsonbProperty("code")
  private int code;
  @JsonbProperty("message")
  private String message;

  public JsonRpcError() {
  }

  public JsonRpcError(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public JsonRpcError appendMessage(String message) {
    this.message += message;
    return this;
  }

  @Override
  public String toString() {
    return '{' +
      " code: " + code + ',' +
      " message: " + message +
      '}';
  }
}

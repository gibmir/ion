package com.github.gibmir.ion.lib.netty.common.configuration.group.dto;

public class RequestDto {
  private String requestString;

  public RequestDto() {
  }

  public RequestDto(String requestString) {
    this.requestString = requestString;
  }

  public String getRequestString() {
    return requestString;
  }

  public void setRequestString(String requestString) {
    this.requestString = requestString;
  }

  @Override
  public String toString() {
    return "RequestDto{" +
      "requestString='" + requestString + '\'' +
      '}';
  }
}

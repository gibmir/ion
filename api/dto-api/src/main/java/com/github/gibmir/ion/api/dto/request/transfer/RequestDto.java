package com.github.gibmir.ion.api.dto.request.transfer;

import java.util.Map;

public class RequestDto extends AbstractJsonRpcRequest {
  private String id;

  public RequestDto() {
    super();
  }

  private RequestDto(final String id, final String procedureName, final Object args) {
    super(procedureName, args);
    this.id = id;
  }

  /**
   * Static factory for named method parameters.
   *
   * @param id         request id
   * @param methodName method name
   * @param namedArgs  arguments
   * @return request
   */
  public static RequestDto named(final String id, final String methodName, final Map<String, Object> namedArgs) {
    return new RequestDto(id, methodName, namedArgs);
  }

  /**
   * Static factory for positional method parameters.
   *
   * @param id             request id
   * @param methodName     method name
   * @param positionalArgs arguments
   * @return notification
   */
  public static RequestDto positional(final String id, final String methodName, final Object[] positionalArgs) {
    return new RequestDto(id, methodName, positionalArgs);
  }

  /**
   * @return request id
   */
  public String getId() {
    return id;
  }

  /**
   * Sets request id.
   *
   * @param id request id
   */
  public void setId(final String id) {
    this.id = id;
  }
}

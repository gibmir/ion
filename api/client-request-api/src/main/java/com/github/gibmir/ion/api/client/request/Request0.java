package com.github.gibmir.ion.api.client.request;

import java.util.concurrent.CompletableFuture;

/**
 * Represents request without args.
 *
 * @param <R> response type
 */
public interface Request0<R> {

  /**
   * Makes a request call.
   *
   * @param id request id
   * @return future response
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * {"jsonrpc": "2.0", "method": "myMethod", "id": 1}
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * {"jsonrpc": "2.0", "method": "myMethod", "result": 19, "id": 1}
   * }
   * </pre>
   */
  CompletableFuture<R> call(String id);

  /**
   * Makes a notification call.
   *
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * {"jsonrpc": "2.0", "method": "myMethod"}
   * }
   * </pre>
   * There is no response for specification.
   */
  void notificationCall();
}

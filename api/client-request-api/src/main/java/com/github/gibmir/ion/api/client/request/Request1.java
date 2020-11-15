package com.github.gibmir.ion.api.client.request;

import java.util.concurrent.CompletableFuture;

/**
 * Represents request with one arg.
 *
 * @param <T> argument type
 * @param <R> response type
 */
//todo encapsulate id
public interface Request1<T, R> {

  /**
   * Makes a positional call.
   *
   * @param id  request id
   * @param arg request argument
   * @return response future
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * {"jsonrpc": "2.0", "method": "myMethod", "params": [42], "id": 1}
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * {"jsonrpc": "2.0", "result": 19, "id": 1}
   * }
   * </pre>
   */
  CompletableFuture<R> positionalCall(String id, T arg);

  /**
   * Makes a named call.
   *
   * @param id  request id
   * @param arg request argument
   * @return response future
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * {"jsonrpc": "2.0", "method": "myMethod", "params": {"subtrahend": 23}, "id": 1}
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * {"jsonrpc": "2.0", "result": 19, "id": 1}
   * }
   * </pre>
   */
  CompletableFuture<R> namedCall(String id, T arg);

  /**
   * Makes a notification call.
   *
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * {"jsonrpc": "2.0", "method": "myMethod", "params": [42]}
   * }
   * </pre>
   * There is no response for specification.
   */
  void notificationCall(T arg);
}

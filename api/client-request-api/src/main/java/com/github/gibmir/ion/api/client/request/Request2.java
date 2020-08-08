package com.github.gibmir.ion.api.client.request;

import java.util.concurrent.CompletableFuture;

/**
 * Represents request with two arguments.
 *
 * @param <T1> first argument type
 * @param <T2> second argument type
 * @param <R>  response type
 */
public interface Request2<T1, T2, R> {

  /**
   * Makes a positional call.
   *
   * @param id   request id
   * @param arg1 first argument
   * @param arg2 second argument
   * @return response future
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * {"jsonrpc": "2.0", "method": "subtract", "params": [42, 23], "id": 1}
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * {"jsonrpc": "2.0", "result": 19, "id": 1}
   * }
   * </pre>
   */
  CompletableFuture<R> positionalCall(String id, T1 arg1, T2 arg2);

  /**
   * Makes a named call.
   *
   * @param id   request id
   * @param arg1 first argument
   * @param arg2 second argument
   * @return response future
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * {"jsonrpc": "2.0", "method": "subtract", "params": {"subtrahend": 23, "minuend": 42}, "id": 3}
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * {"jsonrpc": "2.0", "result": 19, "id": 3}
   * }
   * </pre>
   */
  CompletableFuture<R> namedCall(String id, T1 arg1, T2 arg2);

  /**
   * Makes a notification call.
   *
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * {"jsonrpc": "2.0", "method": "subtract", "params": [42, 23]}
   * }
   * </pre>
   * There is no response for specification.
   */
  void notificationCall(T1 arg1, T2 arg2);
}

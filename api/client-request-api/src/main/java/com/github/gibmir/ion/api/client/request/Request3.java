package com.github.gibmir.ion.api.client.request;

import java.util.concurrent.CompletableFuture;

/**
 * Represents request with three arguments.
 *
 * @param <T1> first argument type
 * @param <T2> second argument type
 * @param <T3> third argument type
 * @param <R>  response type
 */
public interface Request3<T1, T2, T3, R> {

  /**
   * Makes a positional(array arguments) call.
   *
   * @param id   request id
   * @param arg1 first argument
   * @param arg2 second argument
   * @param arg3 third argument
   * @return response future
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * {"jsonrpc": "2.0", "method": "sum", "params": [3, 3, 3], "id": 1}
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * {"jsonrpc": "2.0", "result": 9, "id": 1}
   * }
   * </pre>
   */
  CompletableFuture<R> positionalCall(String id, T1 arg1, T2 arg2, T3 arg3);

  /**
   * Makes a named call.
   *
   * @param id   request id
   * @param arg1 first argument
   * @param arg2 second argument
   * @param arg3 third argument
   * @return response future
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * {"jsonrpc": "2.0", "method": "sum", "params": {"first": 3, "second": 3, "third": 3}, "id": 3}
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * {"jsonrpc": "2.0", "result": 9, "id": 3}
   * }
   * </pre>
   */
  CompletableFuture<R> namedCall(String id, T1 arg1, T2 arg2, T3 arg3);

  /**
   * Makes a positional(array arguments) notification call.
   *
   * @param arg1 first argument
   * @param arg2 second argument
   * @param arg3 third argument
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * {"jsonrpc": "2.0", "method": "subtract", "params": [42, 23, 33]}
   * }
   * </pre>
   * There is no response for notification.
   */
  void positionalNotificationCall(T1 arg1, T2 arg2, T3 arg3);

  /**
   * Makes a named(key-value arguments) notification call.
   *
   * @param arg1 first argument
   * @param arg2 second argument
   * @param arg3 third argument
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * {"jsonrpc": "2.0", "method": "subtract", "params": {"first": 3, "second": 3, "third": 3}}
   * }
   * </pre>
   * There is no response for notification.
   */
  void namedNotificationCall(T1 arg1, T2 arg2, T3 arg3);
}

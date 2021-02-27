package com.github.gibmir.ion.api.client.batch.request.builder;

import com.github.gibmir.ion.api.client.batch.request.BatchRequest;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;

public interface BatchRequestBuilder<B extends BatchRequestBuilder<B>> {
  /**
   * Add request without args to {@link BatchRequest the batch}.
   *
   * @param id                   request id (must be unique)
   * @param jsonRemoteProcedure0 executed procedure
   * @param responseCallback     callback for response
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "myMethod", "id": 1}]
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "myMethod", "result": 19, "id": 1}]
   * }
   * </pre>
   */
  <R> B addRequest(String id, Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0,
                   ResponseCallback<R> responseCallback);

  /**
   * Add request with positional(array) argument to {@link BatchRequest the batch}.
   *
   * @param id                   request id (must be unique)
   * @param jsonRemoteProcedure1 executed procedure
   * @param arg                  first argument
   * @param responseCallback     callback for response
   * @param <T>                  first argument type
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "myMethod", "params": [42], "id": 1}]
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "result": 19, "id": 1}]
   * }
   * </pre>
   */
  <T, R> B addPositionalRequest(String id, Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1, T arg,
                                ResponseCallback<R> responseCallback);

  /**
   * Add request with positional(array) arguments to {@link BatchRequest the batch}.
   *
   * @param id                   request id (must be unique)
   * @param jsonRemoteProcedure2 executed procedure
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param responseCallback     callback for response
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "subtract", "params": [42, 23], "id": 1}]
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "result": 19, "id": 1}]
   * }
   * </pre>
   */
  <T1, T2, R> B addPositionalRequest(String id, Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                     T1 arg1, T2 arg2, ResponseCallback<R> responseCallback);

  /**
   * Add request with positional(array) arguments to {@link BatchRequest the batch}.
   *
   * @param id                   request id (must be unique)
   * @param jsonRemoteProcedure3 executed procedure
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param arg3                 third argument
   * @param responseCallback     callback for response
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <T3>                 third argument type
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "sum", "params": [3, 3, 3], "id": 1}]
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "result": 9, "id": 1}]
   * }
   * </pre>
   */
  <T1, T2, T3, R> B addPositionalRequest(String id,
                                         Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                         T1 arg1, T2 arg2, T3 arg3, ResponseCallback<R> responseCallback);

  /**
   * Add request with named(key-value) argument to {@link BatchRequest the batch}.
   *
   * @param id                   request id (must be unique)
   * @param jsonRemoteProcedure1 executed procedure
   * @param responseCallback     callback for response
   * @param arg                  argument
   * @param <T>                  argument type
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "myMethod", "params": {"subtrahend": 23}, "id": 1}]
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "result": 19, "id": 1}]
   * }
   * </pre>
   */
  <T, R> B addNamedRequest(String id, Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1, T arg,
                           ResponseCallback<R> responseCallback);

  /**
   * Add request with named(key-value) arguments to {@link BatchRequest the batch}.
   *
   * @param id                   request id (must be unique)
   * @param jsonRemoteProcedure2 executed procedure
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param responseCallback     callback for response
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "subtract", "params": {"subtrahend": 23, "minuend": 42}, "id": 3}]
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "result": 19, "id": 3}]
   * }
   * </pre>
   */
  <T1, T2, R> B addNamedRequest(String id, Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                T1 arg1, T2 arg2, ResponseCallback<R> responseCallback);

  /**
   * Add request with named(key-value) arguments to {@link BatchRequest the batch}.
   *
   * @param id                   request id (must be unique)
   * @param jsonRemoteProcedure3 executed procedure
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param arg3                 third argument
   * @param responseCallback     callback for response
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <T3>                 third argument type
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "sum", "params": {"first": 3, "second": 3, "third": 3}, "id": 3}]
   * }
   * </pre>
   * Example response:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "result": 9, "id": 3}]
   * }
   * </pre>
   */
  <T1, T2, T3, R> B addNamedRequest(String id,
                                    Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                    T1 arg1, T2 arg2, T3 arg3, ResponseCallback<R> responseCallback);

  /**
   * Add notification without args to {@link BatchRequest the batch}.
   *
   * @param jsonRemoteProcedure0 executed procedure
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "myMethod"}]
   * }
   * </pre>
   * There is no response for notification.
   */
  <R> B addNotification(Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0);

  /**
   * Add notification with positional(array) argument to {@link BatchRequest the batch}.
   *
   * @param jsonRemoteProcedure1 executed procedure
   * @param arg                  first argument
   * @param <T>                  first argument type
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "myMethod", "params": [42]}]
   * }
   * </pre>
   * There is no response for notification.
   */
  <T, R> B addPositionalNotification(Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1, T arg);

  /**
   * Add notification with positional(array) arguments to {@link BatchRequest the batch}.
   *
   * @param jsonRemoteProcedure2 executed procedure
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "subtract", "params": [42, 23]}]
   * }
   * </pre>
   * There is no response for notification.
   */
  <T1, T2, R> B addPositionalNotification(Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                          T1 arg1, T2 arg2);

  /**
   * Add notification with positional(array) arguments to {@link BatchRequest the batch}.
   *
   * @param jsonRemoteProcedure3 executed procedure
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param arg3                 third argument
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <T3>                 third argument type
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "sum", "params": [3, 3, 3]}]
   * }
   * </pre>
   * There is no response for notification.
   */
  <T1, T2, T3, R> B addPositionalNotification(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                              T1 arg1, T2 arg2, T3 arg3);

  /**
   * Add notification with named(key-value) argument to {@link BatchRequest the batch}.
   *
   * @param jsonRemoteProcedure1 executed procedure
   * @param arg                  argument
   * @param <T>                  argument type
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "myMethod", "params": {"subtrahend": 23}}]
   * }
   * </pre>
   * There is no response for notification.
   */
  <T, R> B addNamedNotification(Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1, T arg);

  /**
   * Add notification with named(key-value) arguments to {@link BatchRequest the batch}.
   *
   * @param jsonRemoteProcedure2 executed procedure
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "subtract", "params": {"subtrahend": 23, "minuend": 42}}]
   * }
   * </pre>
   * There is no response for notification.
   */
  <T1, T2, R> B addNamedNotification(Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                     T1 arg1, T2 arg2);

  /**
   * Add notification with named(key-value) arguments to {@link BatchRequest the batch}.
   *
   * @param jsonRemoteProcedure3 executed procedure
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param arg3                 third argument
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <T3>                 third argument type
   * @param <R>                  return type
   * @return current builder
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request:
   * <pre>{@code
   * [{"jsonrpc": "2.0", "method": "sum", "params": {"first": 3, "second": 3, "third": 3}}]
   * }
   * </pre>
   * There is no response for notification.
   */
  <T1, T2, T3, R> B addNamedNotification(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                         T1 arg1, T2 arg2, T3 arg3);

  /**
   * @return configured batch request
   */
  BatchRequest build();
}

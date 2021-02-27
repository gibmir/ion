package com.github.gibmir.ion.api.client.batch.request;

/**
 * Represents a batch requests.
 */
public interface BatchRequest {

  /**
   * Makes a batch call.
   * <p>
   * If exception occurred while sending batch then it will be thrown.
   * <p>
   *
   * @implSpec You must follow the json-rpc 2.0 specification.
   * Example request from specification:
   * <pre>{@code
   *  [
   *    {"jsonrpc": "2.0", "method": "sum", "params": [1,2,4], "id": "1"},
   *    {"jsonrpc": "2.0", "method": "notify_hello", "params": [7]},
   *    {"jsonrpc": "2.0", "method": "subtract", "params": [42,23], "id": "2"},
   *    {"foo": "boo"},
   *    {"jsonrpc": "2.0", "method": "foo.get", "params": {"name": "myself"}, "id": "5"},
   *    {"jsonrpc": "2.0", "method": "get_data", "id": "9"}
   *  ]
   * }
   * </pre>
   * and response for example request must look like:
   * <pre>{@code
   *  [
   *    {"jsonrpc": "2.0", "result": 7, "id": "1"},
   *    {"jsonrpc": "2.0", "result": 19, "id": "2"},
   *    {"jsonrpc": "2.0", "error": {"code": -32600, "message": "Invalid Request"}, "id": null},
   *    {"jsonrpc": "2.0", "error": {"code": -32601, "message": "Method not found"}, "id": "5"},
   *    {"jsonrpc": "2.0", "result": ["hello", 5], "id": "9"}
   *  ]
   * }
   * </pre>
   */
  void call();
}

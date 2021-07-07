package com.github.gibmir.ion.api.dto.properties;

/**
 * Contains json keys.
 * Check json keys for example:
 *
 * <pre>{@code
 * [{"jsonrpc": "2.0", "method": "subtract", "params": [42, 23], "id": 1}]
 * }</pre>
 */
public final class SerializationProperties {
  /**
   * Represents json-rpc id.
   */
  public static final String ID_KEY = "id";
  /**
   * Represents json-rpc protocol.
   */
  public static final String PROTOCOL_KEY = "jsonrpc";
  /**
   * Represents json-rpc method.
   */
  public static final String METHOD_KEY = "method";
  /**
   * Represents json-rpc params.
   */
  public static final String PARAMS_KEY = "params";
  /**
   * Represents json-rpc result.
   */
  public static final String RESULT_KEY = "result";
  /**
   * Represents json-rpc error.
   */
  public static final String ERROR_KEY = "error";

  private SerializationProperties() {
  }
}

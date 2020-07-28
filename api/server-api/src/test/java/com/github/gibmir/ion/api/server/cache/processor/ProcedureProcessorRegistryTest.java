package com.github.gibmir.ion.api.server.cache.processor;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import org.junit.jupiter.api.Test;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProcedureProcessorRegistryTest {

  public static final String TEST_PROCEDURE_NAME = "testProcedureName";
  public static final JsonRpcRequestProcessor TEST_JSON_RPC_REQUEST_PROCESSOR = new JsonRpcRequestProcessor() {
    @Override
    public void process(String id, String procedureName, JsonObject jsonObject, Jsonb jsonb,
                        Consumer<JsonRpcResponse> responseConsumer) {

    }

    @Override
    public void process(String procedureName, JsonObject jsonObject, Jsonb jsonb) {

    }
  };


  @Test
  void testGetProcedureProcessor() {
    HashMap<String, JsonRpcRequestProcessor> processorMap = new HashMap<>();
    ProcedureProcessorRegistry processorRegistry = new SimpleProcedureProcessorRegistry(processorMap);
    assertThat(processorRegistry.getProcedureProcessorFor(TEST_PROCEDURE_NAME), is(nullValue()));

    assertDoesNotThrow(() -> processorMap.put(TEST_PROCEDURE_NAME, TEST_JSON_RPC_REQUEST_PROCESSOR));

    assertThat(processorRegistry.getProcedureProcessorFor(TEST_PROCEDURE_NAME), equalTo(TEST_JSON_RPC_REQUEST_PROCESSOR));
  }

  @Test
  void testRegister() {
    Map<String, JsonRpcRequestProcessor> processorMap = new HashMap<>();

    ProcedureProcessorRegistry processorRegistry = new SimpleProcedureProcessorRegistry(processorMap);
    processorRegistry.register(TEST_PROCEDURE_NAME, TEST_JSON_RPC_REQUEST_PROCESSOR);

    assertThat(processorMap.isEmpty(), is(false));
    assertThat(processorMap.get(TEST_PROCEDURE_NAME), equalTo(TEST_JSON_RPC_REQUEST_PROCESSOR));
    assertThat(processorRegistry.getProcedureProcessorFor(TEST_PROCEDURE_NAME), equalTo(TEST_JSON_RPC_REQUEST_PROCESSOR));
  }

  @Test
  void testUnregister() {
    Map<String, JsonRpcRequestProcessor> processorMap = new HashMap<>();
    ProcedureProcessorRegistry processorRegistry = new SimpleProcedureProcessorRegistry(processorMap);
    assertDoesNotThrow(() -> processorRegistry.unregister(TEST_PROCEDURE_NAME));
    processorRegistry.register(TEST_PROCEDURE_NAME, TEST_JSON_RPC_REQUEST_PROCESSOR);

    assertThat(processorMap.isEmpty(), is(false));
    assertThat(processorMap.get(TEST_PROCEDURE_NAME), equalTo(TEST_JSON_RPC_REQUEST_PROCESSOR));
    assertThat(processorRegistry.getProcedureProcessorFor(TEST_PROCEDURE_NAME), equalTo(TEST_JSON_RPC_REQUEST_PROCESSOR));

    assertDoesNotThrow(() -> processorRegistry.unregister(TEST_PROCEDURE_NAME));

    assertThat(processorMap.isEmpty(), is(true));
    assertThat(processorMap.get(TEST_PROCEDURE_NAME), is(nullValue()));
    assertThat(processorRegistry.getProcedureProcessorFor(TEST_PROCEDURE_NAME), is(nullValue()));
  }
}
